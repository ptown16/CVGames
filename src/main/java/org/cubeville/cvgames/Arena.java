package org.cubeville.cvgames;

import org.bukkit.entity.Player;

public class Arena {

	private String name;
	private Game game;
	private GameQueue queue;
	private ArenaStatus status;


	public Arena(String name) {
		this.name = name;
	}

	public void setGame(Game game) {
		this.game = game;
		this.queue = new GameQueue(this);
		this.status = ArenaStatus.OPEN;
	}

	public Game getGame() {
		return game;
	}

	public String getName() {
		return name;
	}

	public void setStatus(ArenaStatus status) {
		SignManager.updateArenaSignsStatus(getName(), status);
		this.status = status;
	}

	public ArenaStatus getStatus() {
		return status;
	}

	public GameQueue getQueue() {
		return queue;
	}

	public void playerLogoutCleanup(Player p) {
		if (status == ArenaStatus.OPEN) {
			queue.whenPlayerLogout(p, this);
		} else {
			game.whenPlayerLogout(p, this);
		}
	}
}