package org.turfwars.turfz.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.player.LocalPlayer;
import org.turfwars.turfz.utilities.Messaging;

public class EntityListener implements Listener {

    /**
     * Constructor will register the listener
     */
    public EntityListener (){
        Bukkit.getPluginManager ().registerEvents (this, TurfZ.getInstance ());
    }

    /**
     * Will make sure that only zombies/pig zombies will spawn
     *
     * @param event
     */
    @EventHandler
    public void onCreatureSpawn (final CreatureSpawnEvent event){
        if (event.getEntityType () != EntityType.ZOMBIE){
            event.setCancelled (true);
            return;
        }
    }

    /**
     * Method will handle updating player stats through the localplayer object
     *
     * @param event
     */
    @EventHandler
    public void onPlayerKillEntity (final EntityDeathEvent event){
        if (event.getEntity ().getKiller () == null) return;
        if (event.getEntity ().getKiller () instanceof Player){
            final LocalPlayer killer = TurfZ.getPlayerRegistry ().getPlayer (event.getEntity ().getKiller ());

            if (event.getEntity ().getType () == EntityType.PLAYER){
                killer.setKills (killer.getKills () + 1);
            }else if (event.getEntity ().getType () == EntityType.ZOMBIE || event.getEntity ().getType () == EntityType.PIG_ZOMBIE){
                killer.setZombieKills (killer.getZombieKills () + 1);
            }
        }
    }

    /**
     * Stop zombies from burning in the sunlight
     *
     * @param event
     */
    @EventHandler
    public void onEntityCombust (final EntityCombustEvent event){
        if (event.getEntityType () == EntityType.ZOMBIE){
            event.setCancelled (true);
        }
    }

    /**
     * Will stop any entity from dropping exp
     *
     * @param event
     */
    @EventHandler
    public void onExpDrop (final EntityDeathEvent event){
        event.setDroppedExp (0);
    }


}
