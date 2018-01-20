package searchpp.database;

import searchpp.model.config.Api;
import searchpp.utils.ConfigLoader;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;

public class DBConnection
{
    private static DBConnection _connection;

    /**
     * Get a open connection to db
     * If the connection fails, the program will exit
     * @return the open connection object
     */
    public static DBConnection getConnection()
    {
        //Check if the actual connection is open
        if(_connection == null || !_connection.isOpen())
        {
            //if not, create a new connection
            _connection = new DBConnection();
        }
        return _connection;
    }

    private Connection _sqlCon;

    private DBConnection()
    {
        openConnection();
    }

    /**
     * Open a new Connection
     */
    private void openConnection()
    {
        try
        {
            //Open new db connection with dbname, user and password from config
            _sqlCon = DriverManager.getConnection("jdbc:mysql://" + ConfigLoader.getConfig("db", Api.clientID) +
                                                          "?user=" + ConfigLoader.getConfig("db", Api.accessKey) +
                                                          "&password=" + ConfigLoader.getConfig("db", Api.secretKey));
        }
        catch(SQLException ex)
        {
            //Open connection failed. Print the error
            System.err.println("FATAL ERR: DBConnection.openConnection");
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check, if the connection to database is open
     * @return true if open, false if closed
     */
    private boolean isOpen()
    {
        try {
            return !(_sqlCon == null || _sqlCon.isClosed());
        }
        catch(SQLException ex)
        {
            System.err.println("ERR DBConnection.isOpen: +" + ex.getMessage());
            return false;
        }
    }

    /**
     * Execute a sql statement (Insert, Update, Delete)
     * @param sql the sql statement to be executed
     * @return true if the statement was executed successful, otherwise false
     */
    public boolean execute(String sql)
    {
        Statement stmt;
        try
        {
            stmt = _sqlCon.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR DBConnection.execute: +" + ex.getMessage());
            return false;
        }
    }

    /**
     * Execute a sql Insert and return the created auto-id
     * @param sql the statement to be executed
     * @return the id that was created, if successful, otherwise -1
     */
    public int insert(String sql)
    {
        Statement stmt;
        try
        {
            stmt = _sqlCon.createStatement();
            //Enable returning the generated keys
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            int id = -1;
            //Get generated key
            ResultSet key = stmt.getGeneratedKeys();
            if(key.first())
            {
                id = key.getInt(1);
            }
            key.close();
            stmt.close();
            return id;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR DBConnection.insert: +" + ex.getMessage());
            return -1;
        }
    }

    /**
     * Execute a sql Insert with parameters and return the created auto-id
     * @param sql the statement to be executed
     * @param parameter the objects to be used as parameter
     * @return the id that was created, -2 if there was no id, -1 if the execution failed
     */
    public int insert(String sql, Object... parameter)
    {
        PreparedStatement stmt;
        try
        {
            //Enable returning the generated keys
            stmt = _sqlCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < parameter.length; ++i)
            {
                if (parameter[i] instanceof String) {
                    stmt.setString(i+1, (String)parameter[i]);
                } else if (parameter[i] instanceof LocalDate) {
                    LocalDate date = (LocalDate)parameter[i];
                    stmt.setDate(i+1, java.sql.Date.valueOf(date));
                } else if (parameter[i] instanceof Date) {
                    Date date = (Date)parameter[i];
                    stmt.setTimestamp(i+1, new java.sql.Timestamp(date.getTime()));
                } else if (parameter[i] instanceof Integer) {
                    stmt.setInt(i+1, (Integer)parameter[i]);
                } else if (parameter[i] instanceof Float) {
                    stmt.setFloat(i+1, (Float)parameter[i]);
                } else if (parameter[i] instanceof Double) {
                    stmt.setDouble(i+1, (Double)parameter[i]);
                } else {
                    throw new IllegalArgumentException("Unkown type");
                }
            }
            int id = -2;
            stmt.executeUpdate();
            ResultSet key = stmt.getGeneratedKeys();
            if(key.first())
            {
                id = key.getInt(1);
            }
            key.close();
            stmt.close();
            return id;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR DBConnection.insert: +" + ex.getMessage());
            return -1;
        }
    }

    /**
     * Execute a sql query
     * @param sql the query to execute
     * @return the results of the query or null, if the execution failed
     */
    public ResultSet query(String sql)
    {
        Statement stmt;
        try
        {
            stmt = _sqlCon.createStatement();
            stmt.closeOnCompletion();
            ResultSet res = stmt.executeQuery(sql);
            return res;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR DBConnection.query: +" + ex.getMessage());
            return null;
        }
    }
}
