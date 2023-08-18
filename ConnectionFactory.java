import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static String login = "root";
    private static String senha = "admin";
   
    private static String url = "jdbc:mysql://localhost:3306/locadoradvd";

    public static Connection conn;

   

    public static Connection getConnection() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(url, login, senha);
                return conn;
            }else{
                return conn;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }
}