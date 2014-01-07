package org.turfwars.turfz.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.database.queries.QueryKill;
import org.turfwars.turfz.player.LocalPlayer;

public class PlayerListener implements Listener {

    public PlayerListener (){
        Bukkit.getPluginManager ().registerEvents (this, TurfZ.getInstance ());
    }

    @EventHandler
    public void onPlayerLogin (final PlayerJoinEvent event){
        final LocalPlayer localPlayer = new LocalPlayer (event.getPlayer ());
        TurfZ.getPlayerRegistry ().register (event.getPlayer (), localPlayer);
        TurfZ.getScoreboardTask ().drawScoreboard (localPlayer);
    }

    @EventHandler
    public void onPlayerDeath (final PlayerDeathEvent event){
        if (event.getEntity ().getKiller ().getType () == EntityType.PLAYER){
            final LocalPlayer deadPlayer = TurfZ.getPlayerRegistry ().getPlayer (event.getEntity ());
            final LocalPlayer killer = TurfZ.getPlayerRegistry ().getPlayer (event.getEntity ().getKiller ());;

            // Query the kill and death
            TurfZ.getDatabaseManager ().getConsumer ().queueQuery (new QueryKill (killer, deadPlayer));

            // Reset the scoreboard so the player has a fresh scoreboard to use
            resetScoreboard (deadPlayer);
            // Update the local objects of the two players
            killer.setKills (killer.getKills () + 1);
            deadPlayer.setDeaths (deadPlayer.getOverallDeaths () + 1);
            // TODO - send the player back to the main hub
        }
    }

    private void resetScoreboard (final LocalPlayer localPlayer){
        localPlayer.getScoreboard ().resetScores (Bukkit.getOfflinePlayer (ChatColor.GREEN + "Kills:"));
        localPlayer.getScoreboard ().resetScores (Bukkit.getOfflinePlayer (ChatColor.GREEN + "Zombie Kills:"));
    }
}
