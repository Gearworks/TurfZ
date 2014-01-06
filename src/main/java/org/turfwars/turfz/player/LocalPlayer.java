package org.turfwars.turfz.player;

import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;

import java.util.HashMap;

public class LocalPlayer {

    private final Player bukkitPlayer;

    private int kills;
    private int deaths;
    private String bestTimeString;

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
        this.bestTimeString = (String) playerMap.get ("best_time");
    }

    /**
     *
     * @return the number of kills the player has gotten in total
     */
    public int getKills (){
        return kills;
    }

    /**
     *
     * @return the number of deaths the player has gotten in total
     */
    public int getDeaths (){
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
     * @param kills
     */
    public void setKills (int kills){
        this.kills = kills;
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
     * @return the player this object belongs to
     */
    public Player getBukkitPlayer (){
        return bukkitPlayer;
    }
}
