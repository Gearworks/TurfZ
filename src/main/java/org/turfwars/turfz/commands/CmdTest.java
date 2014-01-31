package org.turfwars.turfz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;
import org.turfwars.turfz.utilities.ConfigUtil;
import org.turfwars.turfz.utilities.Messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CmdTest implements CommandExecutor{

    public boolean onCommand (final CommandSender sender, final Command cmd, String label, String[] args){
        final List<String> toSave = new ArrayList<String> ();
        for (final ZombieSpawn zombieSpawn : TurfZ.getLocationManager ().getZombieSpawns ()){
            final String value  = String.format ("%d %d %d %f %f %d", zombieSpawn.getLocation ().getBlockX (),
                    zombieSpawn.getLocation ().getBlockY (),
                    zombieSpawn.getLocation ().getBlockZ (),
                    zombieSpawn.getLocation ().getYaw (),
                    zombieSpawn.getLocation ().getPitch (),
                    zombieSpawn.getRadius ());
            Messaging.info (value);
            toSave.add (value);
        }

        TurfZ.getConfigRegistry ().getSpawnConfig ().set ("spawns.zombies", toSave);

        try{
            TurfZ.getConfigRegistry ().getSpawnConfig ().save (TurfZ.getConfigRegistry ().getSpawnFile ());
        }catch (IOException e){
            e.printStackTrace ();
        }
        return true;
    }
}
