package org.turfwars.turfz.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.tasks.effects.BleedTask;

import java.util.Random;

public class EffectListener implements Listener {

    public EffectListener (){
        Bukkit.getPluginManager ().registerEvents (this, TurfZ.getInstance ());
    }

    @EventHandler
    public void onPlayerBleed (final EntityDamageByEntityEvent event){
        if (event.getEntity () instanceof Player){
            final int percent = TurfZ.getConfigRegistry ().getConfig ().getInt ("bleed.percent");
            if (random.nextInt (100) + 1 <= percent){
                BleedTask bleedTask = new BleedTask (event.getEntity (), TurfZ.getConfigRegistry ().getConfig ().getInt ("bleed.length"));
                bleedTask.start ();
            }
        }
    }

    private static final Random random = new Random ();
}
