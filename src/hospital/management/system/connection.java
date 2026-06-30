package hospital.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class connection {

    private static final Logger LOGGER = Logger.getLogger(connection.class.getName());

    Connection connect;
    Statement statement;

    public connection() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            connect = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/hospital_management_system?useSSL=false&serverTimezone=UTC",
                    "root",
                    "Admin123"
            );

            statement = connect.createStatement();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
        }
    }
}
