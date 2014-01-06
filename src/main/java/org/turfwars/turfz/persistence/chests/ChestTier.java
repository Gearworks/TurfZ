package org.turfwars.turfz.persistence.chests;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChestTier {

    private final String name;
    private final int percentage;
    private final List<ItemStack> items = new ArrayList<ItemStack> ();

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
    public List<ItemStack> getItems (){
        return items;
    }

    /**
     * Adds all items from a list into the item list of this class
     *
     * @param itemList
     */
    public void addItems (final List<ItemStack> itemList){
        for (final ItemStack itemStack : itemList){
            items.add (itemStack);
        }
    }

    /**
     *
     * @return the percentage for each item to be spawned
     */
    public int getPercentage (){
        return percentage;
    }
}
