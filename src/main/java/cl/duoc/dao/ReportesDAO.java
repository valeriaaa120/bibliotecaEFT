package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    public List<Object[]> librosMasPrestados() {

        List<Object[]> lista = new ArrayList<>();

        String sql =
                "SELECT l.id, l.titulo, l.autor, COUNT(p.id) AS total " +
                        "FROM libros l " +
                        "INNER JOIN prestamos p ON l.id = p.id_libro " +
                        "GROUP BY l.id, l.titulo, l.autor " +
                        "ORDER BY total DESC";

        try (Connection con = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("total")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error en libros más prestados: "
                    + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> historialEstudiante(int idEstudiante) {

        List<Object[]> lista = new ArrayList<>();

        String sql =
                "SELECT p.id, e.nombre AS estudiante, l.titulo, " +
                        "p.fecha_prestamo, p.fecha_devolucion, p.devuelto " +
                        "FROM prestamos p " +
                        "INNER JOIN estudiantes e ON p.id_estudiante = e.id " +
                        "INNER JOIN libros l ON p.id_libro = l.id " +
                        "WHERE e.id = ? " +
                        "ORDER BY p.fecha_prestamo DESC";

        try (Connection con = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("estudiante"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_prestamo"),
                            rs.getDate("fecha_devolucion"),
                            rs.getBoolean("devuelto") ? "Sí" : "No"
                    });
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en historial del estudiante: "
                    + e.getMessage());
        }

        return lista;
    }

    public List<Object[]> prestamosActuales() {

        List<Object[]> lista = new ArrayList<>();

        String sql =
                "SELECT p.id, e.nombre AS estudiante, l.titulo, " +
                        "p.fecha_prestamo, p.fecha_devolucion, " +
                        "CASE WHEN p.fecha_devolucion < CURRENT_DATE " +
                        "THEN 'Atrasado' ELSE 'Pendiente' END AS estado " +
                        "FROM prestamos p " +
                        "INNER JOIN estudiantes e ON p.id_estudiante = e.id " +
                        "INNER JOIN libros l ON p.id_libro = l.id " +
                        "WHERE p.devuelto = FALSE " +
                        "ORDER BY p.fecha_devolucion ASC";

        try (Connection con = ConexionDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("estudiante"),
                        rs.getString("titulo"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion"),
                        rs.getString("estado")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error en préstamos actuales: "
                    + e.getMessage());
        }

        return lista;
    }
}
