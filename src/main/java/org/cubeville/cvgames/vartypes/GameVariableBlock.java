package org.cubeville.cvgames.vartypes;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.cvgames.GameUtils;

import javax.annotation.Nullable;

public class GameVariableBlock extends GameVariable {

	private Block block;

	@Override
	public void setItem(Player player, String input, String arenaName) throws CommandExecutionException {
		Block b = player.getTargetBlock(null, 20);
		if (b.isEmpty()) throw new CommandExecutionException("You need to be looking at a block to execute this command");
		block = b;
	}

	@Override
	public void setItem(@Nullable String string, String arenaName) {
		if (string == null) {
			block = null;
		} else {
			block = GameUtils.parseBlockLocation(string).getBlock();
		}
	}

	public final String displayString() {
		return "Block";
	}

	@Override
	public Block getItem() {
		return block;
	}

	@Override
	public String itemString() {
		if (block == null) {
			return null;
		}
		return GameUtils.blockLocToString(block.getLocation());
	}

	@Override
	public boolean isValid() {
		return block != null;
	}

}