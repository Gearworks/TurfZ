package org.turfwars.turfz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.utilities.ConfigUtil;

public class CmdTest implements CommandExecutor{

    public boolean onCommand (final CommandSender sender, final Command cmd, String label, String[] args){
        // insert stuff here

        return true;
    }
}
