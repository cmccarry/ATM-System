package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnect {

    private static String HOST = "127.0.0.1";
    private static int PORT = 3306;
    private static String DB_NAME = "atm";
    private static String USERNAME = "root";
    private static String PASSWORD = "Enter database password here";
    private static Connection connection ;
        
    public static Connection getConnect () {
        try {
        	connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST,PORT,DB_NAME),USERNAME,PASSWORD);
        } catch (SQLException ex) {
        	Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  connection;
    }
}
