package org.turfwars.turfz.player;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.database.queries.QuerySavePlayer;
import org.turfwars.turfz.tasks.effects.BleedTask;
import org.turfwars.turfz.utilities.Messaging;

import java.util.HashMap;

public class LocalPlayer {

    private final Player bukkitPlayer;

    private int kills;
    private int deaths;
    private int zombieKills;
    private int timePlayed;

    private Runnable bleedingTask;

    private Scoreboard scoreboard;
    private Objective objective;

    private boolean isBleeding;
    private boolean isPlaying;

    public LocalPlayer (final Player bukkitPlayer){
        this.bukkitPlayer = bukkitPlayer;
        load ();
    }

    /**
     * Will populate members of the class by loading information from the MySQL database
     */
    public void load (){
        HashMap<String, Object> playerMap = TurfZ.getDatabaseManager ().getPlayerQuery (this);

        this.isPlaying = (((Integer) playerMap.get ("playing")) == 1 ? true : false);
        this.kills = (Integer) playerMap.get ("kills");
        this.deaths = (Integer) playerMap.get ("total_deaths");
        this.zombieKills = (Integer) playerMap.get ("zombie_kills");
        this.timePlayed = (Integer) playerMap.get ("playing_time");
    }

    /**
     * Will save any value to the MySQL
     */
    public void save (){
        // Kills and deaths queued in PlayerListener
        TurfZ.getDatabaseManager ().getConsumer ().queueQuery (new QuerySavePlayer (this));
    }

    /**
     *
     * @return the amount of time someone has played on the server (in seconds)
     */
    public int getTimePlayed (){
        return timePlayed;
    }

    /**
     *
     * @return the number of kills the player has gotten in their current life
     */
    public int getKills (){
        return kills;
    }

    /**
     *
     * @return the number of zombie kills a player has gotten in their current life
     */
    public int getZombieKills (){
        return zombieKills;
    }

    /**
     *
     * @return the number of deaths the player has gotten in total
     */
    public int getOverallDeaths (){
        return deaths;
    }

    /**
     *
     * @return true if the player is already bleeding, otherwise false
     */
    public boolean isBleeding (){
        return isBleeding;
    }

    /**
     * Playing status is used to determine whether or not the player needs to be created or loaded
     *
     * @return true if the player is currently on a life, otherwise false
     */
    public boolean isPlaying (){
        return isPlaying;
    }

    /**
     *
     * @return the player's scoreboard
     */
    public Scoreboard getScoreboard (){
        return scoreboard;
    }

    /**
     *
     * @return the player's scoreboard objective
     */
    public Objective getObjective (){
        return objective;
    }

    /**
     * Updates every second to update the players total time in seconds
     */
    public void tickTimePlayed (){
        timePlayed++;
    }

    /**
     *
     * @param objective
     */
    public void setObjective (final Objective objective){
        this.objective = objective;
    }

    /**
     *
     * @param playing
     */
    public void setPlayingStatus (boolean playing){
        this.isPlaying = playing;
    }

    /**
     *
     * @param runnable
     */
    public void setBleedingTask (final Runnable runnable){
        this.bleedingTask = runnable;
    }

    /**
     *
     * @param zombieKills
     */
    public void setZombieKills (int zombieKills){
        this.zombieKills = zombieKills;
    }

    /**
     *
     * @param kills
     */
    public void setKills (int kills){
        this.kills = kills;
    }


    /**
     * Will set the scoreboard object so we can access it from anywhere
     *
     * @param scoreboard
     */
    public void setScoreboard (final Scoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    /**
     *
     * @param deaths
     */
    public void setDeaths (int deaths){
        this.deaths = deaths;
    }

    /**
     *
     * @param bleedingStatus
     */
    public void setBleedingStatus (boolean bleedingStatus){
        isBleeding = bleedingStatus;
    }

    /**
     * Stop bleeding task (ie if a player bandages)
     */
    public void stopBleeding (){
        ((BleedTask) bleedingTask).stopBleeding ();
    }

    /**
     *
     * @return the player this object belongs to
     */
    public Player getBukkitPlayer (){
        return bukkitPlayer;
    }
}
