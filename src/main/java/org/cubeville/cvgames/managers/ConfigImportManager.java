package org.cubeville.cvgames.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.cubeville.cvgames.CVGames;

import java.util.List;
import java.util.Objects;

public class ConfigImportManager {

	public static void importConfiguration(String gameName) {
		FileConfiguration config = CVGames.getInstance().getConfig();
		if (config.getConfigurationSection("arenas") == null) {
			return;
		}

		for (String arenaName : Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)) {

			ConfigurationSection arenaConfig = config.getConfigurationSection("arenas." + arenaName);
			assert arenaConfig != null;

			if (arenaConfig.isList("game")) {
				List<String> games = arenaConfig.getStringList("game");
				if (!games.contains(gameName)) continue;
			} else {
				String game = arenaConfig.getString("game");
				if (game == null || !game.equals(gameName)) continue;
			}

			// now we know the game is set up properly, we can add the arena and set its game
			ArenaManager.addArena(arenaName);
			ArenaManager.addArenaGame(arenaName, gameName);


			if (!arenaConfig.contains("variables")) continue;
			parseArenaVariables("variables", arenaConfig, arenaName);
		}
	}

	private static void parseArenaVariables(String sectionPath, ConfigurationSection arenaConfig, String arenaName) {
		ConfigurationSection config = arenaConfig.getConfigurationSection(sectionPath);
		if (config == null) { return; }

		for (String var : Objects.requireNonNull(config).getKeys(false)) {
			String path = sectionPath + "." + var;
			if (config.isConfigurationSection(var)) {
				parseArenaVariables(path, arenaConfig, arenaName);
			} else {
				ArenaManager.getArena(arenaName).setVarFromValue(path, arenaName);
			}
		}
	}
}
