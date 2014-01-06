package org.turfwars.turfz.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.ChestTier;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.utilities.Messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestTask implements Runnable {

    private final List<LocalChest> chests = new ArrayList<LocalChest> ();

    public void run (){
        loadChests ();

        for (final LocalChest chest : chests){
            if (chest.getLocation ().getBlock ().getType () == Material.CHEST){
                addItems ((Chest) chest.getLocation ().getBlock ().getState (), chest.getChestTier ());
            }else{
                Messaging.info (chest.getLocation ().getBlock ().toString ());
            }
        }

        // Clean up the list
        chests.clear ();
    }

    /**
     * Will add items based on the chance set in the tier config
     *
     * @param chest
     * @param chestTier
     */
    private void addItems (final Chest chest, final ChestTier chestTier){
        if (chest != null && chestTier != null){
            // Clean the chest so that way fresh loot can be added in and the chest doesn't get overloaded with crap
            if (chest.getInventory ().getContents ().length > 0) chest.getInventory ().clear ();

            for (final ItemStack itemStack : chestTier.getItems ()){
                // Chance of spawning in each item based on the percentage set in the configuration
                if (random.nextInt (100) + 1 <= chestTier.getPercentage ()){
                    // No duplicates of any item inside a chest
                    if (!chest.getInventory ().contains (itemStack)){
                        chest.getInventory ().addItem (itemStack);
                    }
                }
            }
        }
    }

    /**
     * Will load the list from the MySQL server so that way we can use less memory by only temporarily storing the chest data
     */
    private void loadChests () {
        final Connection conn = TurfZ.getDatabaseManager ().getConnection ();

        if (conn != null){
            try{
                final PreparedStatement ps = conn.prepareStatement ("SELECT * FROM chests");
                final ResultSet rs = ps.executeQuery ();
                while (rs.next ()){
                    // Make sure the tier exists before we actually start adding items that may not be there
                    if (TurfZ.getTierRegistry ().getChestTier (rs.getString ("tier")) != null){
                        // Location of the chest
                        final Location location = new Location (TurfZ.getMainWorld (), rs.getDouble ("x"), rs.getDouble ("y"), rs.getDouble ("z"));
                        // Load the chest into the chest list
                        chests.add (new LocalChest (location, TurfZ.getTierRegistry ().getChestTier (rs.getString ("tier"))));
                    }else{
                        Messaging.severe (String.format ("%s is an unknown tier", rs.getString ("tier")));
                    }
                }
            }catch(SQLException e){
                e.printStackTrace ();
            }
        }
    }

    private static final Random random = new Random ();
}
