package org.turfwars.turfz.database.queries;

import org.turfwars.turfz.player.LocalPlayer;

public class QueryKill implements Query{

    private LocalPlayer killer;
    private LocalPlayer deadPlayer;

    public QueryKill (final LocalPlayer killer, final LocalPlayer deadPlayer){
        this.killer = killer;
        this.deadPlayer = deadPlayer;
    }

    public String [] getQuery(){
        return new String [] { String.format ("UPDATE players SET kills = kills + 1 WHERE playername = '%s'", killer.getBukkitPlayer ().getName ()), // Update kills this life
                               String.format ("UPDATE players SET total_kills = total_kills + 1 WHERE playername = '%s'", killer.getBukkitPlayer ()), // Update total kills ever
                               String.format ("UPDATE players SET total_deaths = total_deaths + 1 WHERE playername = '%s'", deadPlayer.getBukkitPlayer ().getName ())}; // Update total deaths
    }
}
