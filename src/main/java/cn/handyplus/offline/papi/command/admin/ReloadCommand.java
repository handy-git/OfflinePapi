package cn.handyplus.offline.papi.command.admin;

import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.offline.papi.OfflinePapi;
import cn.handyplus.offline.papi.util.ConfigUtil;
import lombok.NoArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 重载配置
 *
 * @author handy
 */
public class ReloadCommand implements IHandyCommandEvent {

    public ReloadCommand(){

    }

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public String permission() {
        return "offlinePapi.reload";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ConfigUtil.init();
                MessageApi.sendMessage(sender, "&a重载配置成功");
            }
        }.runTaskAsynchronously(OfflinePapi.getInstance());
    }

}