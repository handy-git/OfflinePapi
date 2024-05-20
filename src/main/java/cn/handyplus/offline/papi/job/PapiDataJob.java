package cn.handyplus.offline.papi.job;

import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.offline.papi.enter.OfflinePapiEnter;
import cn.handyplus.offline.papi.hook.PlaceholderApiUtil;
import cn.handyplus.offline.papi.service.OfflinePapiService;
import cn.handyplus.offline.papi.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
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
        long task = ConfigUtil.CONFIG.getLong("task", 300);
        HandySchedulerUtil.runTaskTimerAsynchronously(() -> {
            getPapiDataJob(true);
        }, 20 * 60, task * 20);
    }

    /**
     * 拉取数据
     *
     * @param isOnline 是否在线
     */
    public static void getPapiDataJob(boolean isOnline) {
        if (!GET_PAPI_DATA_JOB_TASK_LOCK.tryAcquire()) {
            return;
        }
        try {
            boolean msg = ConfigUtil.CONFIG.getBoolean("msg");
            long start = System.currentTimeMillis();
            if (msg) {
                MessageUtil.sendConsoleMessage("同步papi变量数据开始");
            }
            List<String> papiList = ConfigUtil.CONFIG.getStringList("papi");
            if (CollUtil.isEmpty(papiList)) {
                return;
            }
            // 保存在线数据
            if (isOnline) {
                getOnlinePlayersPapiDataJob(papiList);
            } else {
                getOfflinePlayersPapiDataJob(papiList);
            }
            if (msg) {
                MessageUtil.sendConsoleMessage("同步papi变量数据结束,耗时:" + (System.currentTimeMillis() - start) / 1000);
            }
        } finally {
            GET_PAPI_DATA_JOB_TASK_LOCK.release();
        }
    }

    /**
     * 保存在线数据
     *
     * @param papiList 配置的变量
     */
    private static void getOnlinePlayersPapiDataJob(List<String> papiList) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        MessageUtil.sendConsoleMessage("本次同步在线玩家数量:" + onlinePlayers.size());
        if (onlinePlayers.isEmpty()) {
            return;
        }
        for (Player onlinePlayer : onlinePlayers) {
            buildOfflinePapiEnter(papiList, onlinePlayer.getName(), onlinePlayer.getUniqueId());
        }
    }

    /**
     * 保存离线数据
     *
     * @param papiList 配置的变量
     */
    private static void getOfflinePlayersPapiDataJob(List<String> papiList) {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        MessageUtil.sendConsoleMessage("本次同步离线玩家数量:" + offlinePlayers.length);
        if (offlinePlayers.length < 1) {
            return;
        }
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            buildOfflinePapiEnter(papiList, offlinePlayer.getName(), offlinePlayer.getUniqueId());
        }
    }

    /**
     * 构建数据
     *
     * @param papiList   变量列表
     * @param playerName 玩家名
     * @param playerUuid 玩家uid
     * @since 1.0.7
     */
    private static void buildOfflinePapiEnter(List<String> papiList, String playerName, UUID playerUuid) {
        if ("CMI-Fake-Operator".equalsIgnoreCase(playerName)) {
            return;
        }
        for (String papi : papiList) {
            OfflinePapiEnter offlinePapiEnter = new OfflinePapiEnter();
            offlinePapiEnter.setPlayerName(playerName);
            offlinePapiEnter.setPlayerUuid(playerUuid.toString());
            offlinePapiEnter.setPapi(papi);
            // 判断值是否存在
            String papiValue = PlaceholderApiUtil.set(playerUuid, papi);
            if (StrUtil.isEmpty(papiValue) || papiValue.equals(papi)) {
                continue;
            }
            offlinePapiEnter.setVault(papiValue);
            OfflinePapiService.getInstance().saveOrUpdate(offlinePapiEnter);
        }
    }

    /**
     * 构建数据
     *
     * @param papiList 变量列表
     * @since 1.0.7
     */
    public static void buildPlayerPapiEnter(List<String> papiList, Player player) {
        for (String papi : papiList) {
            OfflinePapiEnter offlinePapiEnter = new OfflinePapiEnter();
            offlinePapiEnter.setPlayerName(player.getName());
            offlinePapiEnter.setPlayerUuid(player.getUniqueId().toString());
            offlinePapiEnter.setPapi(papi);
            // 判断值是否存在
            String papiValue = PlaceholderApiUtil.set(player, papi);
            if (StrUtil.isEmpty(papiValue) || papiValue.equals(papi)) {
                continue;
            }
            offlinePapiEnter.setVault(papiValue);
            OfflinePapiService.getInstance().saveOrUpdate(offlinePapiEnter);
        }
    }

}
