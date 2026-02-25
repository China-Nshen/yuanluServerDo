package yuan.plugins.serverDo.bukkit.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import yuan.plugins.serverDo.ShareLocation;

/**
 * 跨服传送触发事件。
 */
@Getter
@ToString
public final class CrossServerTeleportEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();

	/** 执行本次传送请求的玩家(命令发送者/操控者) */
	private final @NonNull Player operator;
	/** 被移动的玩家名 */
	private final @NonNull String mover;
	/** 目标玩家名(若按坐标跨服则为null) */
	private final String          targetPlayer;
	/** 目标坐标(若按玩家跨服则为null) */
	private final ShareLocation   targetLocation;

	public CrossServerTeleportEvent(@NonNull Player operator, @NonNull String mover, String targetPlayer, ShareLocation targetLocation) {
		this.operator = operator;
		this.mover = mover;
		this.targetPlayer = targetPlayer;
		this.targetLocation = targetLocation;
	}

	@Override
	public @NonNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static @NonNull HandlerList getHandlerList() {
		return HANDLERS;
	}
}

