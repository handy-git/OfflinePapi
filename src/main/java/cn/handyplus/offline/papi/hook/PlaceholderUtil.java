package cn.handyplus.offline.papi.hook;

import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.offline.papi.OfflinePapi;
import cn.handyplus.offline.papi.enter.OfflinePapiEnter;
import cn.handyplus.offline.papi.service.OfflinePapiService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * 变量扩展
 *
 * @author handy
 */
public class PlaceholderUtil extends PlaceholderExpansion {
    private final OfflinePapi plugin;

    public PlaceholderUtil(OfflinePapi plugin) {
        this.plugin = plugin;
    }

    /**
     * 变量前缀
     *
     * @return 结果
     */
    @Override
    public String getIdentifier() {
        return "offlinePapi";
    }

    /**
     * 注册变量
     *
     * @param player      玩家
     * @param placeholder 变量字符串
     * @return 变量
     */
    @Override
    public String onRequest(OfflinePlayer player, String placeholder) {
        if (player == null) {
            return null;
        }
        String[] placeholderStr = placeholder.split("_");
        if (placeholderStr.length < 2) {
            return "";
        }
        String playerName = placeholderStr[0];
        String papi = placeholder.replace(playerName + "_", "");

        String value;
        // 如果玩家在线.直接获取实时变量
        Player onlinePlayer = BaseUtil.getOnlinePlayer(playerName);
        if (onlinePlayer != null) {
            value = PlaceholderApiUtil.set(onlinePlayer, "%" + papi + "%");
            return plugin.getConfig().getString(placeholder, value);
        }
        // 玩家不在线在获取离线变量
        OfflinePapiEnter offlinePapiEnter = OfflinePapiService.getInstance().findByPlayerUuidAndPapi(playerName, "%" + papi + "%");
        if (offlinePapiEnter == null) {
            value = PlaceholderApiUtil.set(Bukkit.getOfflinePlayer(playerName), "%" + papi + "%");
        } else {
            value = offlinePapiEnter.getVault();
        }
        return plugin.getConfig().getString(placeholder, value);
    }

    /**
     * 因为这是一个内部类，
     * 你必须重写这个方法，让PlaceholderAPI知道不要注销你的扩展类
     *
     * @return 结果
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * 因为这是一个内部类，所以不需要进行这种检查
     * 我们可以简单地返回{@code true}
     *
     * @return 结果
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * 作者
     *
     * @return 结果
     */
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * 版本
     *
     * @return 结果
     */
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
