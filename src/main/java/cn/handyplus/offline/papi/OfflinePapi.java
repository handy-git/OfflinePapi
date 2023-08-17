package cn.handyplus.offline.papi;

import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.db.SqlManagerUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.offline.papi.constants.OfflineConstants;
import cn.handyplus.offline.papi.hook.PlaceholderUtil;
import cn.handyplus.offline.papi.job.PapiDataJob;
import cn.handyplus.offline.papi.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class OfflinePapi extends JavaPlugin {
    private static OfflinePapi INSTANCE;
    public static boolean USE_PAPI = true;

    @Override
    public void onEnable() {
        INSTANCE = this;
        InitApi initApi = InitApi.getInstance(this);
        // 加载配置文件
        ConfigUtil.init();
        // 加载Placeholder
        new PlaceholderUtil(this).register();
        initApi.initCommand("cn.handyplus.offline.papi.command")
                .initListener("cn.handyplus.offline.papi.listener")
                .enableSql("cn.handyplus.offline.papi.enter")
                .addMetrics(18120)
                .checkVersion(ConfigUtil.CONFIG.getBoolean(BaseConstants.IS_CHECK_UPDATE), OfflineConstants.PLUGIN_VERSION_URL);

        // 初始化定时任务
        PapiDataJob.init();

        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "已成功载入服务器！");
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "Author:handy QQ群:1064982471");
    }

    @Override
    public void onDisable() {
        // 关闭数据源
        SqlManagerUtil.getInstance().close();
        MessageUtil.sendConsoleMessage("§a已成功卸载！");
        MessageUtil.sendConsoleMessage("§aAuthor:handy QQ群:1064982471");
    }

    public static OfflinePapi getInstance() {
        return INSTANCE;
    }

}