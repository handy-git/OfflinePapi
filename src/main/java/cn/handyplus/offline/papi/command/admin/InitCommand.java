package cn.handyplus.offline.papi.command.admin;

import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.offline.papi.job.PapiDataJob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 初始化离线玩家数据
        PapiDataJob.getPapiDataJob(false);
    }

}