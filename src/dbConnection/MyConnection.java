package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {

    private static final String username = "root";
    private static final String password = "7004121683@vs";
    private static final String dataConn = "jdbc:mysql://localhost:3306/students_management";
    private static Connection con = null;

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(dataConn, username, password);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }
}
