package org.cubeville.cvgames.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.cvgames.managers.ArenaManager;
import org.cubeville.cvgames.managers.EditingManager;
import org.cubeville.cvgames.models.Arena;
import org.cubeville.cvgames.models.BaseGame;
import org.cubeville.cvgames.vartypes.GameVariable;
import org.cubeville.cvgames.vartypes.GameVariableList;
import org.cubeville.cvgames.vartypes.GameVariableObject;

import java.util.List;

public class SetEditingObjectVariable extends RunnableCommand {

    @Override
    public TextComponent execute(CommandSender sender, List<Object> baseParameters)
            throws Error {
        if (!(sender instanceof Player)) throw new Error("You cannot run this command from console!");
        Player player = (Player) sender;
        Arena arena = (Arena) baseParameters.get(0);
        String variable = ((String) baseParameters.get(1)).toLowerCase();
        if (!arena.hasVariable(variable)) throw new Error("That variable does not exist for the arena " + arena.getName());
        GameVariable gameVariable = arena.getGameVariable(variable);
        if (!(gameVariable instanceof GameVariableList)) throw new Error("The variable " + variable +" is not a list");
        int index;
        try {
            index = Integer.parseInt((String) baseParameters.get(2));
        } catch (NumberFormatException e) {
            throw new Error(baseParameters.get(2) + " is not a valid index!");
        }
        GameVariable editingVar = ((GameVariableList<?>) gameVariable).getVariableAtIndex(index - 1);
        if (editingVar == null) throw new Error("The list " + variable +" does not have an index of " + index);
        if (!(editingVar instanceof GameVariableObject)) throw new Error("The list " + variable + " is not a list of objects");

        String path = variable + "." + (index - 1);
        EditingManager.setEditObject(arena, player, (GameVariableObject) editingVar, path);
        return new TextComponent("§aEditing object number " + index + " in list " + variable + " for arena " + arena.getName());
    }

}
