package org.turfwars.turfz.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.database.queries.QueryKill;
import org.turfwars.turfz.player.LocalPlayer;
import org.turfwars.turfz.utilities.BungeeUtil;
import org.turfwars.turfz.utilities.ItemUtil;

public class PlayerListener implements Listener {

    public PlayerListener (){
        Bukkit.getPluginManager ().registerEvents (this, TurfZ.getInstance ());
    }

    @EventHandler
    public void onPlayerLogin (final PlayerJoinEvent event){
        final LocalPlayer localPlayer = new LocalPlayer (event.getPlayer ());
        TurfZ.getPlayerRegistry ().register (event.getPlayer (), localPlayer);
        TurfZ.getScoreboardTask ().drawScoreboard (localPlayer);

        if (!localPlayer.isPlaying ()){
            localPlayer.getBukkitPlayer ().getInventory ().clear ();
            localPlayer.getBukkitPlayer ().teleport (TurfZ.getLocationManager ().getRandomPlayerSpawn ());
            ItemUtil.addStartingContents (localPlayer.getBukkitPlayer ());

            localPlayer.setPlayingStatus (true);
        }
    }

    @EventHandler
    public void onPlayerDisconnect (final PlayerQuitEvent event){
        final LocalPlayer localPlayer = TurfZ.getPlayerRegistry ().getPlayer (event.getPlayer ());
        // The big thing to worry about here is whether or not a player is playing or not
        // Since the player will leave the server when he respawns, the value will never change until he rejoins the server
        // So when he disconnects and hasn't died yet, he will always be playing
        localPlayer.save ();
    }

    @EventHandler
    public void onPlayerDeath (final PlayerDeathEvent event){
        final LocalPlayer deadPlayer = TurfZ.getPlayerRegistry ().getPlayer (event.getEntity ());
        if (event.getEntity ().getKiller () != null){
            if (event.getEntity ().getKiller ().getType () == EntityType.PLAYER){
                final LocalPlayer killer = TurfZ.getPlayerRegistry ().getPlayer (event.getEntity ().getKiller ());

                // Query the kill and death
                TurfZ.getDatabaseManager ().getConsumer ().queueQuery (new QueryKill (killer, deadPlayer));

                // Reset the scoreboard so the player has a fresh scoreboard to use
                resetScoreboard (deadPlayer);
                // Update the local objects of the two players
                killer.setKills (killer.getKills () + 1);
                deadPlayer.setDeaths (deadPlayer.getOverallDeaths () + 1);
            }
        }

        deadPlayer.setPlayingStatus (false);
    }

    @EventHandler
    public void onPlayerRespawn (final PlayerRespawnEvent event){
        BungeeUtil.connectPlayerToServer (event.getPlayer (), TurfZ.getConfigRegistry ().getConfig ().getString ("hub-server"));
    }

    private void resetScoreboard (final LocalPlayer localPlayer){
        localPlayer.getScoreboard ().resetScores (Bukkit.getOfflinePlayer (ChatColor.GREEN + "Kills:"));
        localPlayer.getScoreboard ().resetScores (Bukkit.getOfflinePlayer (ChatColor.GREEN + "Zombie Kills:"));
    }
}
