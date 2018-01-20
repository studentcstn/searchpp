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

    public static DBConnection getConnection()
    {
        if(_connection == null || !_connection.isOpen())
        {
            _connection = new DBConnection();
        }
        return _connection;
    }

    private Connection _sqlCon;

    private DBConnection()
    {
        openConnection();
    }

    private void openConnection()
    {
        try
        {
            _sqlCon = DriverManager.getConnection("jdbc:mysql://" + ConfigLoader.getConfig("db", Api.clientID) +
                                                          "?user=" + ConfigLoader.getConfig("db", Api.accessKey) +
                                                          "&password=" + ConfigLoader.getConfig("db", Api.secretKey));
        }
        catch(SQLException ex)
        {
            System.err.println("FATAL ERR: DBConnection.openConnection");
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

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

    public boolean executeDateParameter(String sql, Date date)
    {
        PreparedStatement stmt;
        try
        {
            stmt = _sqlCon.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
            stmt.executeUpdate();
            stmt.close();
            return true;
        }
        catch(SQLException ex)
        {
            System.err.println("ERR DBConnection.executeDateParameter: +" + ex.getMessage());
            return false;
        }
    }

    public boolean executeLocalDateParameter(String sql, LocalDate... date)
    {
        PreparedStatement stmt;
        try
        {
            stmt = _sqlCon.prepareStatement(sql);
            for(int i = 1; i <= date.length; ++i)
            {
                stmt.setDate(i, java.sql.Date.valueOf(date[i-1]));
            }
            stmt.executeUpdate();
            stmt.close();
            return true;
        }
        catch (Exception ex)
        {
            System.err.println("ERR DBConnection.executeLocalDateParameter: +" + ex.getMessage());
            return false;
        }
    }

    public int insert(String sql)
    {
        Statement stmt;
        try
        {
            stmt = _sqlCon.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            int id = -1;
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

    public int insert(String sql, String... parameter)
    {
        PreparedStatement stmt;
        try
        {
            stmt = _sqlCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < parameter.length; ++i)
            {
                stmt.setString(i+1, parameter[i]);
            }
            int id = -2;
            stmt.execute();
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
