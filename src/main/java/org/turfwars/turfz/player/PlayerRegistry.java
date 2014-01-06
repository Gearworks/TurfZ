package org.turfwars.turfz.player;

import org.bukkit.entity.Player;
import org.turfwars.turfz.utilities.Messaging;

import java.util.HashMap;
import java.util.Map;

public class PlayerRegistry {

    // Will be used to store all LocalPlayer instances, with the player it belongs to being the key
    private final Map<Player, LocalPlayer> playerMap = new HashMap<Player, LocalPlayer> ();

    /**
     * Empty constructor
     */
    public PlayerRegistry (){}

    /**
     * Will register the player into the player map with his corresponding LocalPLayer
     *
     * @param player
     * @param localPlayer
     */
    public LocalPlayer register (final Player player, final LocalPlayer localPlayer){
        // If the player is null or is already registered we will simply return the method
        if (player == null || playerMap.containsKey (player)){
            Messaging.severe ("An error occurred registering %s", player.getName ());
            return null;
        }

        // LocalPlayer instance needs to be created prior to registering the player or else massive errors will concur
        playerMap.put (player, localPlayer);
        return localPlayer;
    }

    public LocalPlayer unregister (final Player player, final LocalPlayer localPlayer){
        // If the player is null or isn't registered we will simply return the method
        if (player == null || !playerMap.containsKey (player)) return null;

        playerMap.remove (player);
        return localPlayer;
    }

    /**
     *
     * @param player
     * @return the LocalPlayer instance assigned to that player from the player map
     */
    public LocalPlayer getPlayer (final Player player){
        // If the player is null or isn't registered we will simply return null
        if (player == null || !playerMap.containsKey (player)) return null;

        return playerMap.get (player);
    }
}
