/**
 *
 */
package yuan.plugins.serverDo.bukkit.cmds;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yuan.plugins.serverDo.Channel;
import yuan.plugins.serverDo.Channel.Package.BoolConsumer;
import yuan.plugins.serverDo.ShareData;
import yuan.plugins.serverDo.Tool;
import yuan.plugins.serverDo.bukkit.Core;
import yuan.plugins.serverDo.bukkit.Main;
import yuan.plugins.serverDo.bukkit.event.CrossServerTeleportEvent;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * warp命令
 *
 * @author yuanlu
 */
public final class CmdWarp extends TabWarp {

	/** @param name 命令名 */
	CmdWarp(String name) {
		super(name);
	}

	@Override
	protected boolean execute0(CommandSender sender, String[] args) {
		val player = (Player) sender;
		if (args.length > 0) {
			val arg = args[0];
			Core.listenCallBack(player, Channel.WARP, 2, (BiConsumer<String, String>) (name, server) -> {
				if (name.isEmpty()) {
					msg("not-found", sender, arg);
				} else {
					msg("tp", sender, name, server);
					ShareData.getLogger().info("[CrossServerTeleportEvent] WARP name=" + name + ", server=" + server + ", operator=" + player.getName());
					Bukkit.getPluginManager().callEvent(new CrossServerTeleportEvent(player, player.getName(), "warp:" + name + "@" + server, null));
					Main.openGermGui(player, "tpgui", "cross-server-teleport");
					Core.listenCallBack(player, Channel.WARP, 3, (BoolConsumer) success -> {
						if (!success) {
							BC_ERROR.send(sender);
							Main.openGermGuiByCommand(player, "null", "basic.bungee-error");
						}
					});
					Core.BackHandler.recordLocation(player,server);
					Main.send(player, Channel.Warp.s3C_tpWarp(name));
				}
			});
			Main.send(player, Channel.Warp.s2C_searchWarp(arg));
		} else {
			Core.listenCallBack(player, Channel.WARP, 4, (BiConsumer<Collection<String>, Collection<String>>) (w1, w2) -> {
				val s1 = Tool.join(w1, msg("list-w1", 1).getMsg(), msg("list-element", 1).getMsg(), msg("list-delimiter", 1).getMsg());
				val s2 = Tool.join(w2, msg("list-w2", 1).getMsg(), msg("list-element", 1).getMsg(), msg("list-delimiter", 1).getMsg());
				msg("list", player, s1, s2);
			});
			Main.send(player, Channel.Warp.s4C_listWarp());
		}
		return true;
	}

}
