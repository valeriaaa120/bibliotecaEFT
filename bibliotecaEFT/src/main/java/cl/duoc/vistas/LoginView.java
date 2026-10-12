package cl.duoc.vistas;

import cl.duoc.controlador.LoginController;
import cl.duoc.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;

    private final LoginController controlador;

    public LoginView() {
        controlador = new LoginController();

        setTitle("Biblioteca Escolar - Inicio de sesión");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        JLabel lblTitulo = new JLabel(
                "Biblioteca Escolar",
                SwingConstants.CENTER
        );

        txtCorreo = new JTextField();
        txtContrasena = new JPasswordField();
        btnIniciarSesion = new JButton("Iniciar sesión");

        JPanel panelCorreo = new JPanel(new BorderLayout(5, 5));
        panelCorreo.add(new JLabel("Correo:"), BorderLayout.NORTH);
        panelCorreo.add(txtCorreo, BorderLayout.CENTER);

        JPanel panelContrasena = new JPanel(new BorderLayout(5, 5));
        panelContrasena.add(new JLabel("Contraseña:"), BorderLayout.NORTH);
        panelContrasena.add(txtContrasena, BorderLayout.CENTER);

        panel.add(lblTitulo);
        panel.add(panelCorreo);
        panel.add(panelContrasena);
        panel.add(btnIniciarSesion);

        add(panel);

        btnIniciarSesion.addActionListener(e -> iniciarSesion());
        getRootPane().setDefaultButton(btnIniciarSesion);
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        Usuario usuario = controlador.iniciarSesion(correo, contrasena);

        if (usuario != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "¡Bienvenida, " + usuario.getNombre()
                            + "!\nRol: " + usuario.getRol(),
                    "Inicio de sesión exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            MenuPrincipalView menu = new MenuPrincipalView(usuario);
            menu.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "Error de inicio de sesión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView ventana = new LoginView();
            ventana.setVisible(true);
        });
    }
}