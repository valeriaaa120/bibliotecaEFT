package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;
import cl.duoc.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // GUARDAR LIBRO
    public boolean guardar(Libro libro) {
        String sql = "INSERT INTO libros " +
                "(titulo, autor, isbn, editorial, stock, id_categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion =
                     ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar libro: "
                    + e.getMessage());
            return false;
        }
    }

    // LISTAR LIBROS
    public List<Libro> listarTodos() {
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT id, titulo, autor, isbn, editorial, " +
                "stock, id_categoria FROM libros";

        try (Connection conexion =
                     ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();

                libro.setId(rs.getInt("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setStock(rs.getInt("stock"));
                libro.setIdCategoria(
                        rs.getInt("id_categoria")
                );

                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros: "
                    + e.getMessage());
        }

        return libros;
    }

    // ACTUALIZAR LIBRO
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, " +
                "isbn = ?, editorial = ?, stock = ?, " +
                "id_categoria = ? WHERE id = ?";

        try (Connection conexion =
                     ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: "
                    + e.getMessage());
            return false;
        }
    }

    // ELIMINAR LIBRO
    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conexion =
                     ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: "
                    + e.getMessage());
            return false;
        }
    }
}