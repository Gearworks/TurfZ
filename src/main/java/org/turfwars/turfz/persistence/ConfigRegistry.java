package org.turfwars.turfz.persistence;

import org.bukkit.configuration.file.YamlConfiguration;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.locations.spawns.ZombieSpawn;
import org.turfwars.turfz.utilities.Messaging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigRegistry {

    // All the config files, I will use this for the config.yml as well to keep a uniform system
    private YamlConfiguration mainConfig;
    private YamlConfiguration zombieConfig;
    private YamlConfiguration chestConfig;
    private YamlConfiguration spawnConfig;

    private final File dataFolder = TurfZ.getInstance ().getDataFolder ();

    /**
     * Will load all the files in the data folder and populate the members of the class
     * We will also not deal with creation of configs as it seems unnecessary if we can just make it ourselfs
     *
     * in need of a better system
     */
    public ConfigRegistry (){
        for (final File configFile : dataFolder.listFiles ()){
            if (configFile.getName ().equals ("config.yml")){
                mainConfig = YamlConfiguration.loadConfiguration (configFile);
                Messaging.info ("Loading config.yml into the system...");
                continue;
            }

            if (configFile.getName ().equals ("ZombieConfig.yml")){
                zombieConfig = YamlConfiguration.loadConfiguration (configFile);
                Messaging.info ("Loading ZombieConfig.yml into the system...");
                continue;
            }

            if (configFile.getName ().equals ("chests.yml")){
                chestConfig = YamlConfiguration.loadConfiguration (configFile);
                Messaging.info ("Loading chest.yml into the system...");
                continue;
            }

            if (configFile.getName ().equals ("spawns.yml")){
                spawnConfig = YamlConfiguration.loadConfiguration (configFile);
                Messaging.info ("Loading spawns.yml into the system...");
                continue;
            }
        }
    }

    /**
     *
     * @return the main config of the plugin
     */
    public YamlConfiguration getConfig (){
        return mainConfig;
    }

    /**
     *
     * @return the spawns configuration for the plugin
     */
    public YamlConfiguration getZombieConfig (){
        return zombieConfig;
    }

    /**
     *
     * @return all the chests configuration and locations
     */
    public YamlConfiguration getChestConfig (){
        return chestConfig;
    }

    /**
     *
     * @return all the spawns in the game (players, zombies, etc).
     */
    public YamlConfiguration getSpawnConfig (){
        return spawnConfig;
    }
}
