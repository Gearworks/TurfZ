package org.turfwars.turfz.persistence.chests;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChestTier {

    private final String name;
    private final int percentage;
    private final HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer> ();

    public ChestTier (final String name, final int percentage){
        this.name = name;
        this.percentage = percentage;
    }

    /**
     *
     * @return the name of the tier
     */
    public String getName (){
        return name;
    }

    /**
     *
     * @return all the available by this class
     */
    public HashMap<ItemStack, Integer> getItemMap (){
        return items;
    }

    /**
     * Adds all items from a map into the item map of this class
     *
     * @param itemMap
     */
    public void addItems (final HashMap<ItemStack, Integer> itemMap){
        items.putAll (itemMap);
    }

    /**
     *
     * @return the percentage for each item to be spawned
     */
    public int getPercentage (){
        return percentage;
    }
}
