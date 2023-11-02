package cn.handyplus.offline.papi.listener;

import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.offline.papi.job.PapiDataJob;
import cn.handyplus.offline.papi.util.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * 获取数据
 *
 * @author handy
 * @since 1.0.7
 */
@HandyListener
public class PlayerQuitEventListener implements Listener {

    /**
     * 玩家被服务器踢出事件.
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        this.getPapi(event.getPlayer());
    }

    /**
     * 玩家离开服务器事件.
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        this.getPapi(event.getPlayer());
    }

    /**
     * 获取数据
     *
     * @param player 事件
     */
    private void getPapi(Player player) {
        // 是否开启离线同步
        if (!ConfigUtil.CONFIG.getBoolean("quitSync", false)) {
            return;
        }
        List<String> papiList = ConfigUtil.CONFIG.getStringList("papi");
        if (CollUtil.isEmpty(papiList)) {
            return;
        }
        PapiDataJob.buildPlayerPapiEnter(papiList, player);
    }

}