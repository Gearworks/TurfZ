package org.turfwars.turfz.tasks;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.ItemMetaData;
import org.turfwars.turfz.persistence.chests.ChestTier;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.utilities.LocationUtil;
import org.turfwars.turfz.utilities.Messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestTask implements Runnable {

    private final List<LocalChest> chests = new ArrayList<LocalChest> ();

    public ChestTask (){
        loadChests ();
    }

    public void run (){
        for (final LocalChest chest : chests){
            Messaging.info (chest.getLocation ().getBlock ().toString ());
            if (chest.getLocation ().getBlock () != null && chest.getLocation ().getBlock ().getType () == Material.CHEST){
                addItems ((Chest) chest.getLocation ().getBlock ().getState (), chest.getChestTier ());
            }
        }
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

            for (final ItemStack itemKey : chestTier.getItemMap ().keySet ()){
                // Chance of spawning in each item based on the percentage set in the configuration
                if (random.nextInt (100) + 1 <= chestTier.getItemMap ().get (itemKey)){
                    chest.getInventory ().addItem (itemKey);
                }
            }
        }
    }

    /**
     * Will load the list from the MySQL server so that way we can use less memory by only temporarily storing the chest data
     */
    private void loadChests () {
        final List<String> chestCoords = TurfZ.getConfigRegistry ().getChestConfig ().getStringList ("chests");

        for (final String chestData : chestCoords){
            chests.add (LocationUtil.getLocalChest (chestData));
        }
    }

    public List<LocalChest> getChests (){
        return chests;
    }

    private static final Random random = new Random ();
}
