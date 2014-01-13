package org.turfwars.turfz.utilities;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.turfwars.turfz.TurfZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemUtil {

    /**
     * Method used only convert the tier configs into item stack lists
     *
     * @param config
     * @return
     */
    public static HashMap<ItemStack, Integer> convertTierToStackList (final YamlConfiguration config){
        final HashMap<ItemStack, Integer> toReturn = new HashMap<ItemStack, Integer> ();
        for (final String serializedItem : config.getStringList ("items")){
            // Will split the item string from the percentage string
            final String [] itemString = serializedItem.split (":");
            // Will split the item id and data value
            final String [] itemInfo = itemString[0].split ("@");
            ItemStack itemStack;
            // Make sure there is even a split or not
            if (itemInfo.length > 1){
                itemStack = new ItemStack (Integer.parseInt (itemInfo[0]), 1, (short) Integer.parseInt (itemInfo[1]));
            }else{
                itemStack = new ItemStack (Integer.parseInt (itemInfo[0]));
            }
            // Will put the item into the map as well as the corresponding percentage
            toReturn.put (itemStack, Integer.parseInt (itemString[1]));
        }

        return toReturn;
    }

    public static void addStartingContents (final Player player){
        for (final String serializedItem : TurfZ.getConfigRegistry ().getConfig ().getStringList ("starting-items")){
            // Split the item from id and amount
            final String [] itemString = serializedItem.split (":");
            ItemStack itemStack;

            if (itemString.length > 1){
                itemStack = new ItemStack (Integer.parseInt (itemString[0]), Integer.parseInt (itemString[1]));
            }else{
                itemStack = new ItemStack (Integer.parseInt (itemString[0]));
            }

            player.getInventory ().addItem (itemStack);
        }
    }
}
