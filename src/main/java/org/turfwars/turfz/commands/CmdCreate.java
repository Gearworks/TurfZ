package org.turfwars.turfz.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.locations.spawns.PlayerSpawn;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;
import org.turfwars.turfz.utilities.ConfigUtil;
import org.turfwars.turfz.utilities.Messaging;

public class CmdCreate implements CommandExecutor {

    public boolean onCommand (final CommandSender sender, final Command command, String label, String[] args){
        if (sender instanceof Player){
            final Player player = (Player) sender;

            if (!player.hasPermission ("turfz.create")){
                Messaging.sendPermsMessage (player);
                return true;
            }

            // TODO create the list of things administrators can create
            if (args[0].equalsIgnoreCase ("list")){

            }

            if (args[0].equalsIgnoreCase ("zspawn")){
                if (args.length != 2){
                    Messaging.sendCreateMessage (player, "Usage: /create zspawn [radius]");
                    return true;
                }

                // We need to create the spawn, at it to the map and then queue it to query
                final ZombieSpawn createdSpawn = new ZombieSpawn (player.getLocation (), Integer.parseInt (args[1]));
                TurfZ.getLocationManager ().getZombieSpawns ().add (createdSpawn);
                Messaging.sendCreateMessage (player, "You have created a zombie spawn at your location! Don't forget to save!");
            }

            if (args[0].equalsIgnoreCase ("pspawn")){
                if (args.length != 3){
                    Messaging.sendCreateMessage (player, "Usage: /create pspawn [name] [radius]");
                    return true;
                }

                // We need to create the spawn, at it to the map and then queue it to query
                final PlayerSpawn createdSpawn = new PlayerSpawn (args[0], player.getLocation (), Integer.parseInt (args[2]));
                TurfZ.getLocationManager ().getPlayerSpawns ().add (createdSpawn);
                Messaging.sendCreateMessage (player, "You have created a player spawn at your location! Don't forget to save!");
            }

            if (args[0].equalsIgnoreCase ("chest")){
                if (args.length != 2){
                    Messaging.sendCreateMessage (player, "Usage: /create chest [tier]");
                    return true;
                }

                if (TurfZ.getTierRegistry ().getChestTier (args[1]) == null){
                    Messaging.sendCreateMessage (player, "Unknown tier, unable to create the chest");
                    return true;
                }

                TurfZ.getChests ().add (new LocalChest (player.getLocation (), TurfZ.getTierRegistry ().getChestTier (args[1])));
                Messaging.sendCreateMessage (player, "You have created a chest at your location! Don't forget to save!");

                // Place a block at the location of the player
                player.getLocation ().getBlock ().setType (Material.CHEST);
            }
        }

        return true;
    }
}
