package cn.handyplus.offline.papi.hook;

import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.offline.papi.OfflinePapi;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * 变量工具类
 *
 * @author handy
 */
public class PlaceholderApiUtil {

    /**
     * 替换变量
     *
     * @param player 玩家
     * @param str    字符串
     * @return 新字符串
     */
    public static String set(Player player, String str) {
        if (!OfflinePapi.USE_PAPI || player == null || StrUtil.isEmpty(str)) {
            return str;
        }
        // 是否包含变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            str = PlaceholderAPI.setPlaceholders(player, str);
        }
        return str;
    }

    /**
     * 替换变量
     *
     * @param playerUuid 玩家UUid
     * @param str        字符串
     * @return 新字符串
     */
    public static String set(UUID playerUuid, String str) {
        if (!OfflinePapi.USE_PAPI || playerUuid == null || StrUtil.isEmpty(str)) {
            return str;
        }
        // 是否包含变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerUuid);
            if (offlinePlayer == null) {
                return str;
            }
            str = PlaceholderAPI.setPlaceholders(offlinePlayer, str);
        }
        return str;
    }

}