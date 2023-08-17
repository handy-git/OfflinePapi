package cn.handyplus.offline.papi.command.admin;

import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.offline.papi.OfflinePapi;
import cn.handyplus.offline.papi.job.PapiDataJob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 初始化
 *
 * @author handy
 * @since 1.0.7
 */
public class InitCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "init";
    }

    @Override
    public String permission() {
        return "offlinePapi.init";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // 初始化离线玩家数据
                PapiDataJob.getPapiDataJob(false);
            }
        }.runTaskAsynchronously(OfflinePapi.getInstance());
    }

}