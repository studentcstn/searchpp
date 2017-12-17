package searchpp.database;

import java.sql.*;
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
        _sqlCon = DriverManager.getConnection("jdbc:mysql://localhost/searchpp?"+
        "user=spp&password=1234");
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
