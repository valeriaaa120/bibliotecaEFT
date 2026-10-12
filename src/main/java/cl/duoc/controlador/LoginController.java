package cl.duoc.controlador;

import cl.duoc.dao.UsuarioDAO;
import cl.duoc.model.Usuario;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String correo, String contrasena) {

        if (correo == null || correo.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            return null;
        }

        return usuarioDAO.validarLogin(correo, contrasena);
    }
}