package org.turfwars.turfz.player;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.turfwars.turfz.TurfZ;

import java.util.HashMap;

public class LocalPlayer {

    private final Player bukkitPlayer;

    private int kills;
    private int deaths;
    private int zombieKills;
    private String bestTimeString;

    private Scoreboard scoreboard;
    private boolean isBleeding;

    public LocalPlayer (final Player bukkitPlayer){
        this.bukkitPlayer = bukkitPlayer;
        load ();
    }

    /**
     * Will populate members of the class by loading information from the MySQL database
     */
    public void load (){
        HashMap<String, Object> playerMap = TurfZ.getDatabaseManager ().getPlayerQuery (this);
        this.kills = (Integer) playerMap.get ("kills");
        this.deaths = (Integer) playerMap.get ("deaths");
        this.zombieKills = (Integer) playerMap.get ("zombie_kills");
        this.bestTimeString = (String) playerMap.get ("best_time");
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
     * @return the longest the player has lived
     */
    public String getBestTimeString (){
        return bestTimeString;
    }

    /**
     *
     * @return true if the player is already bleeding, otherwise false
     */
    public boolean isBleeding (){
        return isBleeding;
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
     * @param bestTimeString
     */
    public void setBestTimeString (String bestTimeString){
        this.bestTimeString = bestTimeString;
    }

    /**
     *
     * @param bleedingStatus
     */
    public void setBleedingStatus (boolean bleedingStatus){
        isBleeding = bleedingStatus;
    }

    /**
     *
     * @return the player this object belongs to
     */
    public Player getBukkitPlayer (){
        return bukkitPlayer;
    }
}
