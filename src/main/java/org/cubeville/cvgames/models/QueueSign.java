package org.cubeville.cvgames.models;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.cubeville.cvgames.enums.ArenaStatus;
import org.cubeville.cvgames.managers.SignManager;
import org.cubeville.cvgames.models.Arena;

public class QueueSign {

	private Sign sign;
	private Arena arena;
	private String gameName;

	public QueueSign(Sign sign, Arena arena, String gameName) {
		this.sign = sign;
		this.arena = arena;
		this.gameName = gameName;
		this.sign.setLine(0, arena.getName());
		this.displayStatus(arena.getStatus());
		this.displayFill();
	}


	public void displayStatus(ArenaStatus status) {
		switch (status) {
			case OPEN:
				this.sign.setLine(2, "§a§lOPEN");
				break;
			case IN_QUEUE:
				this.sign.setLine(2, "§e§IN_QUEUE");
				break;
			case IN_USE:
				this.sign.setLine(2, "§7§lIN USE");
				break;
			case CLOSED:
				this.sign.setLine(2, "§c§lCLOSED");
				break;
		}
		this.sign.update();
	}

	public void displayFill() {
		this.sign.setLine(1,"§l" + arena.getQueue().size() + "/" + arena.getQueue().getMaxPlayers());
		this.sign.update();
	}

	public String getArenaName() {
		return arena.getName();
	}

	public void onRightClick(Player p) {
		this.arena.getQueue().join(p, gameName);
		SignManager.updateArenaSignsFill(getArenaName());
	}
}
