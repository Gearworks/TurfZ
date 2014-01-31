package org.turfwars.turfz;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.turfwars.turfz.commands.CmdCreate;
import org.turfwars.turfz.commands.CmdTest;
import org.turfwars.turfz.database.DatabaseManager;
import org.turfwars.turfz.listener.EffectListener;
import org.turfwars.turfz.listener.EntityListener;
import org.turfwars.turfz.listener.PlayerListener;
import org.turfwars.turfz.persistence.ConfigRegistry;
import org.turfwars.turfz.persistence.ItemMetaData;
import org.turfwars.turfz.persistence.chests.LocalChest;
import org.turfwars.turfz.persistence.chests.TierRegistry;
import org.turfwars.turfz.persistence.locations.LocationManager;
import org.turfwars.turfz.player.PlayerRegistry;
import org.turfwars.turfz.tasks.ChestTask;
import org.turfwars.turfz.tasks.ScoreboardTask;
import org.turfwars.turfz.tasks.SpawningTask;
import org.turfwars.turfz.utilities.ConfigUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurfZ extends JavaPlugin {

    // All the managers for each individual part of the plugin

    // Used to register local players
    private PlayerRegistry playerRegistry;
    // Used to handle anything that goes through MySQL
    private DatabaseManager databaseManager;
    // Object used to store any utility locations of the plugin
    private LocationManager locationManager;
    // Used to handle all the configs
    private ConfigRegistry configRegistry;
    // Will load all the tiers using yml files
    private TierRegistry tierRegistry;
    // Task responsible for dealing with the spawning of zombies
    private SpawningTask spawningTask;
    // Task that will add items to chests depending on their assigned tier
    private ChestTask chestTask;
    // Task will update scoreboard for all players
    private ScoreboardTask scoreboardTask;

    // This will contain any items that are in need of name or lore changing
    private static final Map<Material, ItemMetaData> itemMap = new HashMap<Material, ItemMetaData> ();
    // This will contain any items derived from the Ink material that are in need of name or lore changing
    private static final Map<Integer, ItemMetaData> inkMap = new HashMap<Integer, ItemMetaData> ();

    private static TurfZ instance;

    @Override
    public void onLoad (){
        //instance = this;
    }

    @Override
    public void onEnable (){
        loadNameItemMaps ();
        populateMembers ();

        System.out.println ("TurfZ v0.0.10a has been loaded!");

        // Register the consumer so it can query everything in it's queue
        Bukkit.getScheduler ().runTaskTimer (this, databaseManager.getConsumer (), 120L, 120L);
        // Register the task that will spawn a spawns every 15 seconds
        //Bukkit.getScheduler ().runTaskTimer (this, spawningTask, 0L, 20L * 15);
        // Register the task that will spawn items in chest every 20 seconds TODO change time
        Bukkit.getScheduler ().runTaskTimer (this, chestTask, 0L, 20L * 20);
        // Update the scoreboard every two seconds
        Bukkit.getScheduler ().runTaskTimer (this, scoreboardTask, 0L, 20L * 2);
        // Will update the time played for everybody
        //Bukkit.getScheduler ().runTaskTimer (this, new TimeTask (), 0L, 20L);

        new EntityListener ();
        new PlayerListener ();
        new EffectListener ();

        // Register commands
        getCommand ("test").setExecutor (new CmdTest ());
        getCommand ("create").setExecutor (new CmdCreate ());
    }

    @Override
    public void onDisable (){
        // Need to query anything before it gets lost into an empty void of nothingness
        if (databaseManager.getConsumer ().getQueueSize () > 0){
            databaseManager.getConsumer ().run ();
        }

        ConfigUtil.saveChestToConfig ();
        ConfigUtil.saveZombieSpawnsToConfig ();
    }

    private void populateMembers (){
        instance = this;

        // Config needs to be the first thing initialized because other parts need this to populate members of their respective classes
        configRegistry = new ConfigRegistry ();
        // Database second so that way anything using MySQL has access to it
        databaseManager = new DatabaseManager ();
        locationManager = new LocationManager ();

        playerRegistry = new PlayerRegistry ();

        // Tasks
        tierRegistry = new TierRegistry ();
        //spawningTask = new SpawningTask ();
        chestTask = new ChestTask ();
        scoreboardTask = new ScoreboardTask ();
    }

    private void loadNameItemMaps (){
        getItemMap ().put (Material.PAPER, new ItemMetaData (new ItemStack (Material.PAPER), "\247cBandage", "Apply this to yourself in order to stop any bleeding!"));
        getInkMap ().put (1, new ItemMetaData (new ItemStack (Material.INK_SACK, 1, (short) 1), "\247cBlood bag", "Apply this to an ally to slowly regenerate their health!"));
    }

    public static DatabaseManager getDatabaseManager (){
        return instance.databaseManager;
    }

    public static ConfigRegistry getConfigRegistry (){
        return instance.configRegistry;
    }

    public static PlayerRegistry getPlayerRegistry (){
        return instance.playerRegistry;
    }

    public static LocationManager getLocationManager (){
        return instance.locationManager;
    }

    public static TierRegistry getTierRegistry (){
        return instance.tierRegistry;
    }

    public static ScoreboardTask getScoreboardTask (){
        return instance.scoreboardTask;
    }

    public static List<LocalChest> getChests (){
        return instance.chestTask.getChests ();
    }

    public static Map<Material, ItemMetaData> getItemMap (){
        return itemMap;
    }

    public static Map<Integer, ItemMetaData> getInkMap (){
        return inkMap;
    }

    public static TurfZ getInstance (){
        return instance;
    }

    public ItemStack checkNeededMeta (ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta ();

        if (itemStack.getType () == Material.INK_SACK){
            int durability = itemStack.getDurability ();
            if (getInkMap ().containsKey (durability)){
                final ItemMetaData itemMetaData = TurfZ.getInkMap ().get (durability);
                itemMeta.setDisplayName (itemMetaData.getDisplayName ());
                itemMeta.setLore (itemMetaData.getLore ());
            }
        }else{
            if (getItemMap ().containsKey (itemStack.getType ())){
                final ItemMetaData itemMetaData = TurfZ.getItemMap ().get (itemStack.getType ());
                itemMeta.setDisplayName (itemMetaData.getDisplayName ());
                itemMeta.setLore (itemMetaData.getLore ());
            }
        }

        itemStack.setItemMeta (itemMeta);
        return itemStack;
    }

    /**
     *
     * @return the world that is used to play TurfZ
     */
    public static World getMainWorld (){
        return Bukkit.getWorld (instance.configRegistry.getConfig ().getString ("world"));
    }

}
