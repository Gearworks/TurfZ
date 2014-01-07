package org.turfwars.turfz.database;

import org.bukkit.configuration.file.YamlConfiguration;
import org.turfwars.turfz.TurfZ;
import org.turfwars.turfz.database.tasks.Consumer;
import org.turfwars.turfz.player.LocalPlayer;
import org.turfwars.turfz.utilities.Messaging;

import java.sql.*;
import java.util.HashMap;

public class DatabaseManager {

    private ConnectionPool connectionPool;
    private Consumer consumer;

    private final YamlConfiguration config = TurfZ.getConfigRegistry ().getConfig ();

    /**
     * Sets up and tests the connection to the host using the values given in the config
     */
    public DatabaseManager (){
        try{
            connectionPool = new ConnectionPool (config.getString ("mysql.host"),
                                                 config.getInt ("mysql.port"),
                                                 config.getString ("mysql.database"),
                                                 config.getString ("mysql.user"),
                                                 config.getString ("mysql.pass"));

            Connection connection = connectionPool.getConnection ();
            if (connection == null){
                Messaging.severe ("Could not create a connection to the MySQL server");
                return;
            }

            connection.close ();
        } catch (NullPointerException exception){
            Messaging.severe("Error while initializing: %s", new Object[] { exception.getMessage() });
            exception.printStackTrace();
        } catch (Exception exception){
            Messaging.severe("Error while initializing: %s", new Object[] { exception.getMessage() });
            exception.printStackTrace();
        }

        consumer = new Consumer ();
    }

    /**
     * Made static to be easily accessed across the plugin to allow other classes to queue queries
     *
     * @return consumer
     */
    public Consumer getConsumer (){
        return consumer;
    }

    /**
     *
     * @return the connection to the MySQL server
     */
    public Connection getConnection (){
        try{
            return connectionPool.getConnection ();
        }catch (SQLException exception){
            Messaging.severe ("SQL Exception while fetching connection: %s", new Object[] { exception.getMessage () });
            exception.printStackTrace ();
        }

        return null;
    }

    /**
     * Will run a result set to check to see if we get any values back
     *
     * @param playerName
     * @return true if the player exists in the database, otherwise false
     */
    public boolean playerExists (final String playerName){
        final Connection conn = getConnection ();

        try{
            final ResultSet rs = conn.prepareStatement (String.format ("SELECT * FROM players WHERE playername = '%s'", playerName)).executeQuery ();
            if (rs.next ()){
                return true;
            }
        }catch(SQLException e){

        }

        return false;
    }

    /**
     * Needs to be done right away in order for the player loading to happen with nothing null
     *
     * @param localPlayer
     */
    public void createPlayer (final LocalPlayer localPlayer) throws SQLException{
        final PreparedStatement ps = getConnection ().prepareStatement (String.format ("INSERT INTO players (playername, kills, zombie_kills, deaths, best_time, avg_time) VALUES ('%s', 0, 0, 0, '0d0h0m0s', '0d0h0m0s')",
                localPlayer.getBukkitPlayer ().getName ()));

        ps.execute ();
    }

    /**
     * Will load all the player's stats into the map using the column name as the key
     *
     * @param localPlayer
     * @return a map with all the players objects loaded
     */
    public HashMap<String, Object> getPlayerQuery (final LocalPlayer localPlayer){
        final Connection conn = getConnection ();
        final HashMap<String, Object> toReturn = new HashMap<String, Object> ();

        try{
            // Need to create the player into the database
            if (!playerExists (localPlayer.getBukkitPlayer ().getName ()))
                createPlayer (localPlayer);

            final ResultSet rs =  conn.prepareStatement (String.format ("SELECT * FROM players WHERE playername = '%s'", localPlayer.getBukkitPlayer ().getName ())).executeQuery ();
            final ResultSetMetaData rsm = rs.getMetaData ();
            while (rs.next ()){
                for (int i = 1; i < rsm.getColumnCount (); i++){
                    toReturn.put (rsm.getColumnName (i), rs.getObject (i));
                }
            }
        }catch (SQLException e){
            e.printStackTrace ();
            return null;
        }

        return toReturn;
    }
}
