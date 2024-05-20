package cn.handyplus.offline.papi.command.admin;

import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.offline.papi.service.OfflinePapiService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * 删除数据
 *
 * @author handy
 */
public class DeleteCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "delete";
    }

    @Override
    public String permission() {
        return "offlinePapi.delete";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        OfflinePapiService.getInstance().delete();
        MessageUtil.sendMessage(sender, "&a删除全部数据成功");
    }

}