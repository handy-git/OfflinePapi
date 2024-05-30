package cn.handyplus.offline.papi.hook;

import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.offline.papi.OfflinePapi;
import cn.handyplus.offline.papi.enter.OfflinePapiEnter;
import cn.handyplus.offline.papi.param.OfflineParam;
import cn.handyplus.offline.papi.service.OfflinePapiService;
import cn.handyplus.offline.papi.util.ConfigUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<String> papiList = ConfigUtil.CONFIG.getStringList("papi");
        if (CollUtil.isEmpty(papiList)) {
            return null;
        }
        OfflineParam offlineParam = this.getOfflineParam(placeholder, 1, papiList);
        if (offlineParam == null) {
            return null;
        }
        // 如果是me查询自己
        if ("me".equalsIgnoreCase(offlineParam.getPlayerName())) {
            offlineParam.setPlayerName(player.getName());
        }
        // 如果玩家在线.直接获取实时变量
        Optional<Player> onlinePlayerOpt = BaseUtil.getOnlinePlayer(offlineParam.getPlayerName());
        if (onlinePlayerOpt.isPresent()) {
            String value = PlaceholderApiUtil.set(onlinePlayerOpt.get(), offlineParam.getPapi());
            // 如果可以获取到的话就返回实时变量
            if (!value.equals(offlineParam.getPapi())) {
                return value;
            }
        }
        // 玩家不在线在获取离线变量
        Optional<OfflinePapiEnter> offlinePapiEnterOptional = OfflinePapiService.getInstance().findByPlayerNameAndPapi(offlineParam.getPlayerName(), offlineParam.getPapi());
        String defaultValue = ConfigUtil.CONFIG.getString("defaultValue." + offlineParam.getPapiType());
        return offlinePapiEnterOptional.map(OfflinePapiEnter::getVault).orElse(defaultValue);
    }

    /**
     * 获取变量信息
     *
     * @param placeholder 变量
     * @param number      数量
     * @param list        现有配置的变量
     * @return OfflineParam
     */
    private OfflineParam getOfflineParam(String placeholder, int number, List<String> list) {
        String playerName;
        String[] placeholderStr = placeholder.split("_");
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < number && number <= placeholderStr.length; i++) {
            strList.add(placeholderStr[i]);
        }
        playerName = CollUtil.listToStr(strList, "_");
        // 递归到尾直接返回null
        if (playerName.equals(placeholder)) {
            return null;
        }
        String papi = placeholder.replaceFirst(playerName + "_", "");
        // 判断是否包含配置的变量,如果不包含递归处理下一个
        if (!list.contains("%" + papi + "%")) {
            return getOfflineParam(placeholder, number + 1, list);
        }
        return OfflineParam.builder().playerName(playerName).papiType(papi).papi("%" + papi + "%").build();
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
