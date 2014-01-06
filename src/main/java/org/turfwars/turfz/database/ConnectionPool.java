package org.turfwars.turfz.database;

import java.io.Closeable;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool implements Closeable {

    private final Vector<JDCConnection> connections;
    private final String url;
    private final String username;
    private final String password;
    private Lock lock = new ReentrantLock ();

    public ConnectionPool (String sHostname, int nPort, String sDatabase, String sUsername, String sPassword)
            throws ClassNotFoundException {
        Class.forName ("com.mysql.jdbc.Driver");

        this.url = ("jdbc:mysql://" + sHostname + ":" + nPort + "/" + sDatabase);
        this.username = sUsername;
        this.password = sPassword;
        this.connections = new Vector (10);

        new Thread (new ConnectionReaper (), "MySQL Connection Reaper Thread - TurfZ").start ();
    }

    public void close () {
        this.lock.lock ();

        Enumeration pConnections = this.connections.elements ();
        while (pConnections.hasMoreElements ()) {
            JDCConnection pConnection = (JDCConnection) pConnections.nextElement ();

            this.connections.remove (pConnection);
            pConnection.terminate ();
        }

        this.lock.unlock ();
    }

    public Connection getConnection ()
            throws SQLException {
        this.lock.lock ();
        try {
            Enumeration pConnections = this.connections.elements ();
            JDCConnection localJDCConnection1;
            while (pConnections.hasMoreElements ()) {
                JDCConnection pConnection = (JDCConnection) pConnections.nextElement ();

                if (pConnection.lease ()) {
                    if (pConnection.isValid ()) {
                        return pConnection;
                    }

                    this.connections.remove (pConnection);
                    pConnection.terminate ();
                }

            }

            JDCConnection pConnection = new JDCConnection (DriverManager.getConnection (this.url, this.username, this.password));

            pConnection.lease ();

            if (!pConnection.isValid ()) {
                pConnection.terminate ();
                throw new SQLException ("Failed to validate a brand new connection");
            }

            this.connections.add (pConnection);
            return pConnection;
        } finally {
            this.lock.unlock ();
        }
    }

    private void reapConnections () {
        this.lock.lock ();

        long lStale = System.currentTimeMillis () - 300000L;
        Iterator pIterator = this.connections.iterator ();
        while (pIterator.hasNext ()) {
            JDCConnection pConnection = (JDCConnection) pIterator.next ();

            if ((pConnection.inUse ()) && (lStale > pConnection.getLastUse ()) && (!pConnection.isValid ())) {
                pIterator.remove ();
            }
        }

        this.lock.unlock ();
    }

    private final class ConnectionReaper
            implements Runnable {
        private ConnectionReaper () {
        }

        public void run () {
            while (true) {
                try {
                    Thread.sleep (300000L);
                } catch (InterruptedException localInterruptedException) {
                }
                ConnectionPool.this.reapConnections ();
            }
        }
    }

    private final class JDCConnection implements Connection {
        private final Connection connection;
        private boolean inUse;
        private long timeStamp;
        private int networkTimeout;
        private String schema;

        JDCConnection (Connection pConnection) {
            this.connection = pConnection;
            this.inUse = false;
            this.timeStamp = 0L;
            this.networkTimeout = 30;
            this.schema = "default";
        }

        public Statement createStatement ()
                throws SQLException {
            return this.connection.createStatement ();
        }

        public PreparedStatement prepareStatement (String sql)
                throws SQLException {
            return this.connection.prepareStatement (sql);
        }

        public CallableStatement prepareCall (String sql)
                throws SQLException {
            return this.connection.prepareCall (sql);
        }

        public String nativeSQL (String sql)
                throws SQLException {
            return this.connection.nativeSQL (sql);
        }

        public void setAutoCommit (boolean autoCommit)
                throws SQLException {
            this.connection.setAutoCommit (autoCommit);
        }

        public boolean getAutoCommit ()
                throws SQLException {
            return this.connection.getAutoCommit ();
        }

        public void commit ()
                throws SQLException {
            this.connection.commit ();
        }

        public void rollback ()
                throws SQLException {
            this.connection.rollback ();
        }

        public void close ()
                throws SQLException {
            this.inUse = false;
            try {
                if (!this.connection.getAutoCommit ())
                    this.connection.setAutoCommit (true);
            } catch (SQLException pException) {
                ConnectionPool.this.connections.remove (this);
                terminate ();
            }
        }

        public boolean isClosed ()
                throws SQLException {
            return this.connection.isClosed ();
        }

        public DatabaseMetaData getMetaData ()
                throws SQLException {
            return this.connection.getMetaData ();
        }

        public void setReadOnly (boolean readOnly)
                throws SQLException {
            this.connection.setReadOnly (readOnly);
        }

        public boolean isReadOnly ()
                throws SQLException {
            return this.connection.isReadOnly ();
        }

        public void setCatalog (String catalog)
                throws SQLException {
            this.connection.setCatalog (catalog);
        }

        public String getCatalog ()
                throws SQLException {
            return this.connection.getCatalog ();
        }

        public void setTransactionIsolation (int level)
                throws SQLException {
            this.connection.setTransactionIsolation (level);
        }

        public int getTransactionIsolation ()
                throws SQLException {
            return this.connection.getTransactionIsolation ();
        }

        public SQLWarning getWarnings ()
                throws SQLException {
            return this.connection.getWarnings ();
        }

        public void clearWarnings ()
                throws SQLException {
            this.connection.clearWarnings ();
        }

        public Statement createStatement (int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return this.connection.createStatement (resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement (String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return this.connection.prepareStatement (sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall (String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return this.connection.prepareCall (sql, resultSetType, resultSetConcurrency);
        }

        public Map<String, Class<?>> getTypeMap ()
                throws SQLException {
            return this.connection.getTypeMap ();
        }

        public void setTypeMap (Map<String, Class<?>> map)
                throws SQLException {
            this.connection.setTypeMap (map);
        }

        public void setHoldability (int holdability)
                throws SQLException {
            this.connection.setHoldability (holdability);
        }

        public int getHoldability ()
                throws SQLException {
            return this.connection.getHoldability ();
        }

        public Savepoint setSavepoint ()
                throws SQLException {
            return this.connection.setSavepoint ();
        }

        public Savepoint setSavepoint (String name)
                throws SQLException {
            return this.connection.setSavepoint (name);
        }

        public void rollback (Savepoint savepoint)
                throws SQLException {
            this.connection.rollback (savepoint);
        }

        public void releaseSavepoint (Savepoint savepoint)
                throws SQLException {
            this.connection.releaseSavepoint (savepoint);
        }

        public Statement createStatement (int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return this.connection.createStatement (resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement (String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return this.connection.prepareStatement (sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public CallableStatement prepareCall (String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return this.connection.prepareCall (sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement (String sql, int autoGeneratedKeys)
                throws SQLException {
            return this.connection.prepareStatement (sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement (String sql, int[] columnIndexes)
                throws SQLException {
            return this.connection.prepareStatement (sql, columnIndexes);
        }

        public PreparedStatement prepareStatement (String sql, String[] columnNames)
                throws SQLException {
            return this.connection.prepareStatement (sql, columnNames);
        }

        public Clob createClob ()
                throws SQLException {
            return this.connection.createClob ();
        }

        public Blob createBlob ()
                throws SQLException {
            return this.connection.createBlob ();
        }

        public NClob createNClob ()
                throws SQLException {
            return this.connection.createNClob ();
        }

        public SQLXML createSQLXML ()
                throws SQLException {
            return this.connection.createSQLXML ();
        }

        public boolean isValid (int timeout)
                throws SQLException {
            return this.connection.isValid (timeout);
        }

        public void setClientInfo (String name, String value)
                throws SQLClientInfoException {
            this.connection.setClientInfo (name, value);
        }

        public void setClientInfo (Properties properties)
                throws SQLClientInfoException {
            this.connection.setClientInfo (properties);
        }

        public String getClientInfo (String name)
                throws SQLException {
            return this.connection.getClientInfo (name);
        }

        public Properties getClientInfo ()
                throws SQLException {
            return this.connection.getClientInfo ();
        }

        public Array createArrayOf (String typeName, Object[] elements)
                throws SQLException {
            return this.connection.createArrayOf (typeName, elements);
        }

        public Struct createStruct (String typeName, Object[] attributes)
                throws SQLException {
            return this.connection.createStruct (typeName, attributes);
        }

        public void setSchema (String schema)
                throws SQLException {
            this.schema = schema;
        }

        public String getSchema ()
                throws SQLException {
            return this.schema;
        }

        public void abort (Executor executor)
                throws SQLException {
        }

        public void setNetworkTimeout (Executor executor, int milliseconds)
                throws SQLException {
            this.networkTimeout = milliseconds;
        }

        public int getNetworkTimeout ()
                throws SQLException {
            return this.networkTimeout;
        }

        public <T> T unwrap (Class<T> _interface)
                throws SQLException {
            return this.connection.unwrap (_interface);
        }

        public boolean isWrapperFor (Class<?> _interface)
                throws SQLException {
            return this.connection.isWrapperFor (_interface);
        }

        long getLastUse () {
            return this.timeStamp;
        }

        boolean inUse () {
            return this.inUse;
        }

        boolean isValid () {
            try {
                return this.connection.isValid (1);
            } catch (SQLException localSQLException) {
            }
            return false;
        }

        synchronized boolean lease () {
            if (this.inUse) {
                return false;
            }

            this.inUse = true;
            this.timeStamp = System.currentTimeMillis ();
            return true;
        }

        void terminate () {
            try {
                this.connection.close ();
            } catch (SQLException localSQLException) {
            }
        }
    }
}
