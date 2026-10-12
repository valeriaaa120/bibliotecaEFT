package cl.duoc.dao;

import cl.duoc.conexion.ConexionDB;
import cl.duoc.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String correo, String contrasena) {

        String sql = "SELECT id, nombre, rut, correo, contraseña, rol " +
                "FROM usuarios WHERE correo = ? AND contraseña = ?";

        try (Connection conexion =
                     ConexionDB.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRut(rs.getString("rut"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContrasena(rs.getString("contraseña"));
                    usuario.setRol(rs.getString("rol"));

                    return usuario;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar el usuario: "
                    + e.getMessage());
        }

        return null;
    }
}
