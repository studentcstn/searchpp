package searchpp.database;

import searchpp.model.config.Api;
import searchpp.utils.ConfigLoader;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;

public class DBConnection
{
    private static DBConnection _connection;

    public static DBConnection getConnection() throws SQLException
    {
        if(_connection == null || !_connection.isOpen())
        {
            _connection = new DBConnection();
        }
        return _connection;
    }

    private Connection _sqlCon;

    private DBConnection() throws SQLException
    {
        _sqlCon = DriverManager.getConnection("jdbc:mysql://" + ConfigLoader.getConfig("db", Api.clientID) +
                "?user=" + ConfigLoader.getConfig("db", Api.accessKey) +
                "&password=" + ConfigLoader.getConfig("db", Api.secretKey));
    }

    private boolean isOpen()
    {
        try {
            return _sqlCon == null || _sqlCon.isClosed();
        }
        catch(SQLException ex)
        {
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
            return true;
        }
        catch(SQLException ex)
        {
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
            return true;
        }
        catch(SQLException ex)
        {
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
            return true;
        }
        catch (Exception ex)
        {
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
            return id;
        }
        catch(SQLException ex)
        {
            return -1;
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return _sqlCon.prepareStatement(sql);
    }

    public ResultSet query(String sql)
    {
        Statement stmt;
        try
        {
            stmt = _sqlCon.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            return res;
        }
        catch(SQLException ex)
        {
            return null;
        }
    }
}
