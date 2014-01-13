package org.turfwars.turfz.utilities;

import org.bukkit.entity.Player;
import org.turfwars.turfz.TurfZ;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtil {

    public static void connectPlayerToServer (final Player player, final String server){
        ByteArrayOutputStream b = new ByteArrayOutputStream ();
        DataOutputStream out = new DataOutputStream (b);

        try{
            out.writeUTF ("Connect");
            out.writeUTF (server);
        }catch (IOException e){}

        player.sendPluginMessage (TurfZ.getInstance (), "BungeeCord", b.toByteArray ());
    }
}
