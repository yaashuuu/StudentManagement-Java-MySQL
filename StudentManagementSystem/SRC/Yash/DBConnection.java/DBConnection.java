package yash;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/StudentManagement";
            String user = "root"; // 🔁 Your MySQL username
            String password = "laptop"; // 🔁 Replace with your MySQL password

            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
