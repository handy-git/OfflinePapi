package cn.handyplus.offline.papi.job;

import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.offline.papi.OfflinePapi;
import cn.handyplus.offline.papi.enter.OfflinePapiEnter;
import cn.handyplus.offline.papi.hook.PlaceholderApiUtil;
import cn.handyplus.offline.papi.service.OfflinePapiService;
import cn.handyplus.offline.papi.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author handy
 */
public class PapiDataJob {

    private static final Semaphore GET_PAPI_DATA_JOB_TASK_LOCK = new Semaphore(1);

    /**
     * 30s后初始化定时任务
     */
    public static void init() {
        new BukkitRunnable() {
            @Override
            public void run() {
                getPapiDataJob();
            }
        }.runTaskTimerAsynchronously(OfflinePapi.getInstance(), 20 * 60, ConfigUtil.CONFIG.getLong("task", 300) * 20);
    }

    /**
     * 拉取数据
     */
    private static void getPapiDataJob() {
        if (!GET_PAPI_DATA_JOB_TASK_LOCK.tryAcquire()) {
            return;
        }
        try {
            boolean msg = ConfigUtil.CONFIG.getBoolean("msg");
            long start = System.currentTimeMillis();
            if (msg) {
                MessageApi.sendConsoleMessage("同步papi变量数据开始");
            }
            List<String> papiList = ConfigUtil.CONFIG.getStringList("papi");
            if (CollUtil.isEmpty(papiList)) {
                return;
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                for (String papi : papiList) {
                    OfflinePapiEnter offlinePapiEnter = new OfflinePapiEnter();
                    offlinePapiEnter.setPlayerName(onlinePlayer.getName());
                    offlinePapiEnter.setPlayerUuid(onlinePlayer.getUniqueId().toString());
                    offlinePapiEnter.setPapi(papi);
                    // 判断值是否存在
                    String papiValue = PlaceholderApiUtil.set(onlinePlayer, papi);
                    if (StrUtil.isEmpty(papiValue) || papiValue.equals(papi)) {
                        continue;
                    }
                    offlinePapiEnter.setVault(papiValue);
                    OfflinePapiService.getInstance().saveOrUpdate(offlinePapiEnter);
                }
            }
            if (msg) {
                MessageApi.sendConsoleMessage("同步papi变量数据结束,耗时:" + (System.currentTimeMillis() - start) / 1000);
            }
        } finally {
            GET_PAPI_DATA_JOB_TASK_LOCK.release();
        }
    }

}
