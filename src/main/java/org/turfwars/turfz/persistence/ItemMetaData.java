package org.turfwars.turfz.persistence;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemMetaData {

    private String name;
    private List<String> lore;

    public ItemMetaData (ItemStack itemStack, String name, String lore){
        if (name == null)
            this.name = itemStack.getItemMeta ().getDisplayName ();
        else
            this.name = name;

        if (lore == null){
            this.lore = itemStack.getItemMeta ().getLore ();
        }else{
            this.lore = new ArrayList<String> ();
            this.lore.add (lore);
        }
    }

    public String getDisplayName (){
        return name;
    }

    public List<String> getLore (){
        return lore;
    }
}
