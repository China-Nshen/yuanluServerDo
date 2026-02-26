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
import yuan.plugins.serverDo.bukkit.Core;
import yuan.plugins.serverDo.bukkit.Main;
import yuan.plugins.serverDo.bukkit.event.CrossServerTeleportEvent;

import java.util.function.BiConsumer;

/**
 * warp命令
 *
 * @author yuanlu
 */
public final class CmdSpawn extends Cmd {

	/**
	 * spawn的warp名称
	 */
	public static final String NAME = "spawn";

	/** @param name 命令名 */
	CmdSpawn(String name) {
		super(name);
	}

	@Override
	protected boolean execute0(CommandSender sender, String[] args) {
		val player = (Player) sender;
		Core.listenCallBack(player, Channel.WARP, 2, (BiConsumer<String, String>) (name, server) -> {
			if (name.isEmpty()) {
				msg("not-found", sender, NAME);
			} else {
				msg("tp", sender, name, server);
				final boolean crossServer = Main.isCrossServerTarget(server);
				if (crossServer) {
					ShareData.getLogger().info("[CrossServerTeleportEvent] SPAWN name=" + name + ", server=" + server + ", operator=" + player.getName());
					Bukkit.getPluginManager().callEvent(new CrossServerTeleportEvent(player, player.getName(), "spawn:" + name + "@" + server, null));
					Main.openGermGui(player, "tpgui", "cross-server-teleport");
				}
				Core.listenCallBack(player, Channel.WARP, 3, (BoolConsumer) success -> {
					if (!success) {
						BC_ERROR.send(sender);
						Main.openGermGuiByCommand(player, "null", "basic.bungee-error");
					}
				});
				Main.send(player, Channel.Warp.s3C_tpWarp(name));
			}
		});
		Main.send(player, Channel.Warp.s2C_searchWarp(NAME));

		return true;
	}

}
