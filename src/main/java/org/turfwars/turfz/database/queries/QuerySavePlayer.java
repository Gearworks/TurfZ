package org.turfwars.turfz.database.queries;

import org.turfwars.turfz.player.LocalPlayer;

public class QuerySavePlayer implements Query {

    private final LocalPlayer localPlayer;

    public QuerySavePlayer (final LocalPlayer localPlayer){
        this.localPlayer = localPlayer;
    }

    public String [] getQuery (){
        return new String [] {
                String.format ("UPDATE players SET playing_time = %d WHERE playername = '%s'", localPlayer.getTimePlayed (), localPlayer.getBukkitPlayer ().getName ()), // Set the time played in seconds
                String.format ("UPDATE players SET playing = %d WHERE playername = '%s'", (localPlayer.isPlaying () ? 1 : 0),  localPlayer.getBukkitPlayer ().getName ()) // Will set the column to 1 if playing, other wise 0
        };
    }
}
