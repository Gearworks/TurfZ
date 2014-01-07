package org.turfwars.turfz.persistence.chests;

import net.minecraft.util.org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.utilities.ItemUtil;
import org.turfwars.turfz.utilities.Messaging;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TierRegistry {

    private final Map<String, ChestTier> chestTiers = new HashMap<String, ChestTier> ();

    public TierRegistry (){
        final File dataFolder = TurfZ.getInstance ().getDataFolder ();
        final File tierDir = new File (dataFolder, "tiers");

        if (!tierDir.exists ()){
            tierDir.mkdirs ();
            Messaging.info ("Attempting to created the tier directory");
        }

        for (final File tierFile : tierDir.listFiles ()){
            if (!tierFile.getName ().endsWith (".yml")) continue;

            if (YamlConfiguration.loadConfiguration (tierFile) != null){
                final YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration (tierFile);

                // Create the new chest tier folder by giving it the name of the file
                ChestTier chestTier = new ChestTier (FilenameUtils.removeExtension (tierFile.getName ()), chestConfig.getInt ("percentage"));
                // Add all the items available to that tier into the actual object
                chestTier.addItems (ItemUtil.convertTierToStackList (chestConfig));
                // Load the object and add it into the map using the name of the tier as the key
                chestTiers.put (FilenameUtils.removeExtension (tierFile.getName ()), chestTier);
                Messaging.info (String.format ("Loading tier %s into the system...", FilenameUtils.removeExtension (tierFile.getName ())));
                Messaging.info ("" + chestTier.getItemMap ().toString ());
            }
        }
    }

    /**
     *
     * @param tierName
     * @return the chest tier with the given name, will return null if it does not exist
     */
    public ChestTier getChestTier (final String tierName){
        return chestTiers.get (tierName);
    }
}
