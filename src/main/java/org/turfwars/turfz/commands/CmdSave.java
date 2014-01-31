package org.turfwars.turfz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.locations.spawns.PlayerSpawn;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CmdSave implements CommandExecutor {

    public boolean onCommand (final CommandSender sender, final Command cmd, String label, String[] args){
        if (args.length == 1){
            if (args[0].equalsIgnoreCase ("zspawns")){
                final List<String> toSave = new ArrayList<String> ();
                for (final ZombieSpawn zombieSpawn : TurfZ.getLocationManager ().getZombieSpawns ()){
                    final String value  = String.format ("%d %d %d %f %f %d", zombieSpawn.getLocation ().getBlockX (),
                            zombieSpawn.getLocation ().getBlockY (),
                            zombieSpawn.getLocation ().getBlockZ (),
                            zombieSpawn.getLocation ().getYaw (),
                            zombieSpawn.getLocation ().getPitch (),
                            zombieSpawn.getRadius ());
                    toSave.add (value);
                }

                TurfZ.getConfigRegistry ().getSpawnConfig ().set ("spawns.zombies", toSave);

                try{
                    TurfZ.getConfigRegistry ().getSpawnConfig ().save (TurfZ.getConfigRegistry ().getSpawnFile ());
                    sender.sendMessage ("You have saved zombie spawns");
                }catch (IOException e){
                    e.printStackTrace ();
                }
            }else if (args[0].equalsIgnoreCase ("pspawns")){
                final List<String> toSave = new ArrayList<String> ();
                for (final PlayerSpawn playerSpawn : TurfZ.getLocationManager ().getPlayerSpawns ()){
                    final String value = String.format ("%s %d %d %d %f %f %d", playerSpawn.getName (),
                            playerSpawn.getLocation ().getBlockX (),
                            playerSpawn.getLocation ().getBlockY (),
                            playerSpawn.getLocation ().getBlockZ (),
                            playerSpawn.getLocation ().getYaw (),
                            playerSpawn.getLocation ().getPitch (),
                            playerSpawn.getRadius ());

                    toSave.add (value);
                }

                TurfZ.getConfigRegistry ().getSpawnConfig ().set ("spawns.players", toSave);

                try{
                    TurfZ.getConfigRegistry ().getSpawnConfig ().save (TurfZ.getConfigRegistry ().getSpawnFile ());
                    sender.sendMessage ("You have saved player spawn!");
                }catch (IOException e){
                    e.printStackTrace ();
                }
            }else if (args[0].equalsIgnoreCase ("chests")){
                final List<String> toSave = new ArrayList<String> ();
                for (final LocalChest localChest : TurfZ.getChests ()){
                    toSave.add (String.format ("%d %d %d %s", localChest.getLocation ().getBlockX (),
                            localChest.getLocation ().getBlockY (),
                            localChest.getLocation ().getBlockZ (),
                            localChest.getChestTier ()));
                }

                TurfZ.getConfigRegistry ().getChestConfig ().set ("chests", toSave);

                try{
                    TurfZ.getConfigRegistry ().getChestConfig ().save (TurfZ.getConfigRegistry ().getChestFile ());
                    sender.sendMessage ("You have saved chests!");
                }catch (IOException e){
                    e.printStackTrace ();
                }
            }
        }else{
            sender.sendMessage ("save wut noob.");
        }
        return true;
    }
}
