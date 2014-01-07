package org.turfwars.turfz;

import net.minecraft.server.v1_7_R1.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.turfwars.turfz.commands.CmdCreate;
import org.turfwars.turfz.commands.CmdTest;
import org.turfwars.turfz.database.DatabaseManager;
import org.turfwars.turfz.listener.EntityListener;
import org.turfwars.turfz.listener.PlayerListener;
import org.turfwars.turfz.persistence.ConfigRegistry;
import org.turfwars.turfz.persistence.chests.TierRegistry;
import org.turfwars.turfz.persistence.locations.LocationManager;
import org.turfwars.turfz.player.PlayerRegistry;
import org.turfwars.turfz.tasks.ChestTask;
import org.turfwars.turfz.tasks.ScoreboardTask;
import org.turfwars.turfz.tasks.SpawningTask;
import org.turfwars.turfz.utilities.Messaging;

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

    private static TurfZ instance;

    @Override
    public void onLoad (){
        instance = this;

        // Config needs to be the first thing initialized because other parts need this to populate members of their respective classes
        configRegistry = new ConfigRegistry ();
        // Database second so that way anything using MySQL has access to it
        databaseManager = new DatabaseManager ();

        playerRegistry = new PlayerRegistry ();

        // Tasks
        tierRegistry = new TierRegistry ();
        spawningTask = new SpawningTask ();
        chestTask = new ChestTask ();
        scoreboardTask = new ScoreboardTask ();
    }

    @Override
    public void onEnable (){
        System.out.println ("TurfZ v0.0.10a has been loaded!");

        locationManager = new LocationManager ();

        if (spawningTask == null) Messaging.severe ("It's the spawning task itself");

        // Register the consumer so it can query everything in it's queue
        Bukkit.getScheduler ().runTaskTimerAsynchronously (this, databaseManager.getConsumer (), 120L, 120L);
        // Register the task that will spawn a zombie every 15 seconds
        Bukkit.getScheduler ().runTaskTimerAsynchronously (this, spawningTask, 0L, 20L * 15);
        // Register the task that will spawn items in chest every 20 seconds TODO change time
        Bukkit.getScheduler ().runTaskTimerAsynchronously (this, chestTask, 0L, 20L * 20);
        // Update the scoreboard every two seconds
        Bukkit.getScheduler ().runTaskTimerAsynchronously (this, scoreboardTask, 0L, 20L * 2);

        new EntityListener ();
        new PlayerListener ();

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

    public static TurfZ getInstance (){
        return instance;
    }

    /**
     *
     * @return the world that is used to play TurfZ
     */
    public static World getMainWorld (){
        return Bukkit.getWorld (instance.configRegistry.getConfig ().getString ("world"));
    }

}
