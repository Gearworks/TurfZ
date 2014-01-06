package org.turfwars.turfz.utilities;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    /**
     * Method used only convert the tier configs into item stack lists
     *
     * @param config
     * @return
     */
    public static List<ItemStack> convertTierToStackList (final YamlConfiguration config){
        final List<ItemStack> toReturn = new ArrayList<ItemStack> ();
        for (final int itemID : config.getIntegerList ("items")){
            toReturn.add (new ItemStack (itemID));
        }

        return toReturn;
    }
}
