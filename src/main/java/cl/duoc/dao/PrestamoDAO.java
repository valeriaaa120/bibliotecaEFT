package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;
import cl.duoc.model.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    // Registrar un préstamo y descontar una unidad del stock
    public synchronized boolean registrarPrestamo(int idEstudiante, int idLibro) {

        String sqlStock =
                "UPDATE libros SET stock = stock - 1 " +
                        "WHERE id = ? AND stock > 0";

        String sqlPrestamo =
                "INSERT INTO prestamos " +
                        "(id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) " +
                        "VALUES (?, ?, ?, ?, ?)";

        Connection conexion = null;

        try {
            conexion = ConexionDB.getInstance().getConnection();
            conexion.setAutoCommit(false);

            // Verificar que exista stock y descontar un ejemplar
            try (PreparedStatement psStock =
                         conexion.prepareStatement(sqlStock)) {

                psStock.setInt(1, idLibro);

                int filas = psStock.executeUpdate();

                if (filas == 0) {
                    conexion.rollback();
                    return false;
                }
            }

            // Registrar el préstamo con plazo de 14 días
            LocalDate fechaPrestamo = LocalDate.now();
            LocalDate fechaDevolucion = fechaPrestamo.plusDays(14);

            try (PreparedStatement psPrestamo =
                         conexion.prepareStatement(sqlPrestamo)) {

                psPrestamo.setInt(1, idEstudiante);
                psPrestamo.setInt(2, idLibro);
                psPrestamo.setDate(3, Date.valueOf(fechaPrestamo));
                psPrestamo.setDate(4, Date.valueOf(fechaDevolucion));
                psPrestamo.setBoolean(5, false);

                psPrestamo.executeUpdate();
            }

            conexion.commit();
            return true;

        } catch (SQLException e) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException error) {
                    System.err.println(
                            "Error al deshacer el préstamo: "
                                    + error.getMessage()
                    );
                }
            }

            System.err.println(
                    "Error al registrar préstamo: " + e.getMessage()
            );

            return false;

        } finally {

            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(
                            "Error al cerrar conexión: " + e.getMessage()
                    );
                }
            }
        }
    }

    // Obtener todos los préstamos
    public List<Prestamo> listarTodos() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql =
                "SELECT id, id_estudiante, id_libro, " +
                        "fecha_prestamo, fecha_devolucion, devuelto " +
                        "FROM prestamos ORDER BY id DESC";

        try (Connection conexion =
                     ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Prestamo prestamo = new Prestamo();

                prestamo.setId(rs.getInt("id"));
                prestamo.setIdEstudiante(
                        rs.getInt("id_estudiante")
                );
                prestamo.setIdLibro(
                        rs.getInt("id_libro")
                );

                Date fechaPrestamo =
                        rs.getDate("fecha_prestamo");

                if (fechaPrestamo != null) {
                    prestamo.setFechaPrestamo(
                            fechaPrestamo.toLocalDate()
                    );
                }

                Date fechaDevolucion =
                        rs.getDate("fecha_devolucion");

                if (fechaDevolucion != null) {
                    prestamo.setFechaDevolucion(
                            fechaDevolucion.toLocalDate()
                    );
                }

                prestamo.setDevuelto(
                        rs.getBoolean("devuelto")
                );

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al listar préstamos: " + e.getMessage()
            );
        }

        return prestamos;
    }

    // Registrar la devolución y devolver una unidad al stock
    public synchronized boolean registrarDevolucion(int idPrestamo) {

        String sqlPrestamo =
                "SELECT id_libro, devuelto FROM prestamos " +
                        "WHERE id = ? FOR UPDATE";

        String sqlDevolucion =
                "UPDATE prestamos SET devuelto = TRUE " +
                        "WHERE id = ? AND devuelto = FALSE";

        String sqlStock =
                "UPDATE libros SET stock = stock + 1 WHERE id = ?";

        Connection conexion = null;

        try {
            conexion = ConexionDB.getInstance().getConnection();
            conexion.setAutoCommit(false);

            int idLibro;
            boolean devuelto;

            // Buscar y bloquear el préstamo
            try (PreparedStatement ps =
                         conexion.prepareStatement(sqlPrestamo)) {

                ps.setInt(1, idPrestamo);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        conexion.rollback();
                        return false;
                    }

                    idLibro = rs.getInt("id_libro");
                    devuelto = rs.getBoolean("devuelto");
                }
            }

            // Evitar registrar la misma devolución dos veces
            if (devuelto) {
                conexion.rollback();
                return false;
            }

            try (PreparedStatement ps =
                         conexion.prepareStatement(sqlDevolucion)) {

                ps.setInt(1, idPrestamo);

                if (ps.executeUpdate() != 1) {
                    conexion.rollback();
                    return false;
                }
            }

            // Reponer el ejemplar al inventario
            try (PreparedStatement ps =
                         conexion.prepareStatement(sqlStock)) {

                ps.setInt(1, idLibro);

                if (ps.executeUpdate() != 1) {
                    conexion.rollback();
                    return false;
                }
            }

            conexion.commit();
            return true;

        } catch (SQLException e) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException error) {
                    System.err.println(
                            "Error al deshacer devolución: "
                                    + error.getMessage()
                    );
                }
            }

            System.err.println(
                    "Error al registrar devolución: " + e.getMessage()
            );

            return false;

        } finally {

            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(
                            "Error al cerrar conexión: " + e.getMessage()
                    );
                }
            }
        }
    }
}