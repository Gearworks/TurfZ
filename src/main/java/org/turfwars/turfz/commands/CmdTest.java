package org.turfwars.turfz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdTest implements CommandExecutor{

    public boolean onCommand (final CommandSender sender, final Command cmd, String label, String[] args){
        final Player player = (Player) sender;

        player.getWorld ().spawnEntity (player.getLocation (), EntityType.ZOMBIE);
        sender.sendMessage ("You have run the command");

        return true;
    }
}
