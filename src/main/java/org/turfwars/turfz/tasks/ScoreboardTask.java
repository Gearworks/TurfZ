package org.turfwars.turfz.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.player.LocalPlayer;
import org.turfwars.turfz.utilities.Messaging;

public class ScoreboardTask implements Runnable {

    public void run (){
        for (final Player player : Bukkit.getOnlinePlayers ()){
            drawScoreboard (TurfZ.getPlayerRegistry ().getPlayer (player));
        }
    }

    public void drawScoreboard (final LocalPlayer player){
        ScoreboardManager manager = Bukkit.getScoreboardManager ();
        Scoreboard board = manager.getNewScoreboard ();
        player.setScoreboard (board);
        Objective objective = board.registerNewObjective ("stats", "dummy");
        objective.setDisplaySlot (DisplaySlot.SIDEBAR);
        objective.setDisplayName (player.getBukkitPlayer ().getName ());

        Score score = objective.getScore (Bukkit.getOfflinePlayer (ChatColor.GREEN + "Kills:"));
        score.setScore (player.getKills ());
        score = objective.getScore (Bukkit.getOfflinePlayer (ChatColor.GREEN + "Zombie Kills:"));
        score.setScore (player.getZombieKills ());

        player.getBukkitPlayer ().setScoreboard (board);
    }

}
