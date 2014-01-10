package org.turfwars.turfz.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.player.LocalPlayer;
import org.turfwars.turfz.tasks.effects.BleedTask;

import java.util.Random;

public class EffectListener implements Listener {


    public EffectListener (){
        Bukkit.getPluginManager ().registerEvents (this, TurfZ.getInstance ());
    }

    @EventHandler
    public void onPlayerDamageByPlayer (final EntityDamageByEntityEvent event){
        if (event.getEntity () instanceof Player){
            final LocalPlayer localPlayer = TurfZ.getPlayerRegistry ().getPlayer ((Player) event.getEntity ());
            // Need to check if the damager is a player and he has rose red or a "blood bag"
            if (event.getDamager ().getType () == EntityType.PLAYER){
                final LocalPlayer damager = TurfZ.getPlayerRegistry ().getPlayer ((Player) event.getDamager ());

                if (damager.getBukkitPlayer ().getItemInHand ().getType () == Material.INK_SACK &&
                        damager.getBukkitPlayer ().getItemInHand ().getDurability () == 1){

                    event.setCancelled (true);
                    if (localPlayer.isBleeding ()){
                        damager.getBukkitPlayer ().sendMessage ("\2474The bleeding must stop the before applying a blood bag!");
                        return; // Player has a blood bag but cannot be applied, cancel damage event and don't go on
                    }

                    localPlayer.getBukkitPlayer ().addPotionEffect (new PotionEffect (PotionEffectType.REGENERATION, 20 * 20, 2));
                    damager.getBukkitPlayer ().getInventory ().removeItem (damager.getBukkitPlayer ().getItemInHand ());
                    damager.getBukkitPlayer ().sendMessage (String.format ("You have applied a blood bag to %s", localPlayer.getBukkitPlayer ().getName ()));
                    return; // Player has a blood bag and the player is not bleeding, apply blood bag and don't go on to avoid bleeding
                }
            }

            if (localPlayer.isBleeding ()) return;
            final int percent = TurfZ.getConfigRegistry ().getConfig ().getInt ("bleed.percent");
            if (random.nextInt (100) + 1 <= percent){
                BleedTask bleedTask = new BleedTask (event.getEntity (), TurfZ.getConfigRegistry ().getConfig ().getInt ("bleed.length"));
                bleedTask.start ();
                localPlayer.setBleedingStatus (true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUseBandage (final PlayerInteractEvent event){
        if (event.getAction () == Action.RIGHT_CLICK_AIR || event.getAction () == Action.RIGHT_CLICK_BLOCK){
            final LocalPlayer localPlayer = TurfZ.getPlayerRegistry ().getPlayer (event.getPlayer ());

            if (event.getItem () == null) return;

            if (event.getItem ().getType () == Material.PAPER){
                if (localPlayer.isBleeding ()){
                    localPlayer.stopBleeding ();
                    localPlayer.getBukkitPlayer ().sendMessage ("You have bandaged yourself and you are no longer bleeding.");
                    event.getPlayer ().getInventory ().removeItem (event.getPlayer ().getItemInHand ());
                    return;
                }else{
                    localPlayer.getBukkitPlayer ().sendMessage ("You are not bleeding!");
                }
            }
        }
    }

    private static final Random random = new Random ();
}
