package org.turfwars.turfz.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.utilities.Messaging;

public class TimeTask implements Runnable {

    public void run (){
        for (final Player player : Bukkit.getOnlinePlayers ()){
            TurfZ.getPlayerRegistry ().getPlayer (player).tickTimePlayed ();
        }
    }
}
