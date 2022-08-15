package org.cubeville.cvgames.models;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.cubeville.cvgames.utils.GameUtils;

import java.util.*;

public abstract class TeamSelectorGame extends BaseGame {

    private HashMap<Player, Object> state;

    public TeamSelectorGame(String id, String arenaName) {
        super(id, arenaName);
    }

    @Override
    public void processPlayerMap(Map<Integer, List<Player>> playerTeamMap) {
        // Should map team index to player list
        // -1 means they do not have a team selected
        List<Set<Player>> teamPlayers = new ArrayList<>();
        for (int i = 0; i < playerTeamMap.size() - 1; i++) {
            if (playerTeamMap.containsKey(i)) {
                teamPlayers.add(new HashSet<>(playerTeamMap.get(i)));
            } else {
                teamPlayers.add(new HashSet<>());
            }
        }

        List<Player> unselectedPlayers = playerTeamMap.get(-1);
        Collections.shuffle(unselectedPlayers);

        for (Player player : unselectedPlayers) {
            // find the smallest team and add the unselected players to the smallest teams
            int smallestTeamSize = Integer.MAX_VALUE;
            int smallestTeamIndex = -1;
            for (int i = 0; i < teamPlayers.size(); i++) {
                if (teamPlayers.get(i).size() < smallestTeamSize) {
                    smallestTeamSize = teamPlayers.get(i).size();
                    smallestTeamIndex = i;
                }
            }
            teamPlayers.get(smallestTeamIndex).add(player);
        }

        onGameStart(teamPlayers);
    }

    public abstract void onGameStart(List<Set<Player>> teamPlayers);

    public List<HashMap<String, Object>> getTeamVariable() {
        return (List<HashMap<String, Object>>) getVariable("teams");
    }

    public void updateDefaultScoreboard(int currentTime, ArrayList<Integer[]> teamScores, String key) {
        updateDefaultScoreboard(currentTime, teamScores, key, false);
    }

    public void updateDefaultScoreboard(int currentTime, ArrayList<Integer[]> teamScores, String key, boolean isReverse) {
        Scoreboard scoreboard;
        ArrayList<String> scoreboardLines = new ArrayList<>();

        scoreboardLines.add("§bTime remaining: §f" +
                String.format("%d:%02d", currentTime / 60000, (currentTime / 1000) % 60)
        );
        scoreboardLines.add("   ");

        List<HashMap<String, Object>> teams = getTeamVariable();
        if (teams.size() == 1) {
            state.keySet().stream().sorted(Comparator.comparingInt(o -> (isReverse ? 1 : -1) * getState(o).getSortingValue())).forEach( p -> {
                int points = getState(p).getSortingValue();
                scoreboardLines.add("§a" + p.getDisplayName() + "§f: " + points + " " + key);
            });
            scoreboard = GameUtils.createScoreboard(arena, "§b§lFFA " + getId(), scoreboardLines);
        } else {
            for (int i = 0; i < teamScores.size(); i++) {
                String line = teams.get(i).get("name") + "§f: ";
                line += teamScores.get(i)[1];
                line += " ";
                line += key;
                scoreboardLines.add(line);
            }
            scoreboard = GameUtils.createScoreboard(arena, "§b§lTeam " + getId(), scoreboardLines);
        }
        sendScoreboardToArena(scoreboard);
    }
}