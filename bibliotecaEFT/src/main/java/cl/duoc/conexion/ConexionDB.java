package cl.duoc.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static ConexionDB instancia;

    private final String URL = "jdbc:mysql://localhost:3306/biblioteca";

    private final String USUARIO = "root";

    private final String PASSWORD = "Valeria2001@";

    private ConexionDB() {
    }

    public static synchronized ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }

        return instancia;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }
}