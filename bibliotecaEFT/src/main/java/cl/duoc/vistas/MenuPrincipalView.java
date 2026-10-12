
package cl.duoc.vistas;

import cl.duoc.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private final Usuario usuario;

    public MenuPrincipalView(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Biblioteca Escolar - Menú principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 50, 20, 50)
        );

        JLabel lblBienvenida = new JLabel(
                "Bienvenido/a, " + usuario.getNombre(),
                SwingConstants.CENTER
        );

        JLabel lblRol = new JLabel(
                "Rol: " + usuario.getRol(),
                SwingConstants.CENTER
        );

        JButton btnLibros = new JButton("Gestionar libros");
        JButton btnEstudiantes = new JButton("Gestionar estudiantes");
        JButton btnCategorias = new JButton("Categorías");
        JButton btnPrestamos = new JButton("Gestionar préstamos");
        JButton btnReportes = new JButton("Ver reportes");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");


        btnLibros.addActionListener(e -> {
            LibroView ventanaLibros = new LibroView();
            ventanaLibros.setVisible(true);
        });

        btnEstudiantes.addActionListener(e -> {
            EstudianteView ventana = new EstudianteView();
            ventana.setVisible(true);
        });

        btnCategorias.addActionListener(e -> {
            CategoriaView ventana = new CategoriaView();
            ventana.setVisible(true);
        });

        btnPrestamos.addActionListener(e -> {
            PrestamoView ventana = new PrestamoView();
            ventana.setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            ReporteView ventana = new ReporteView();
            ventana.setVisible(true);
        });

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        panel.add(lblBienvenida);
        panel.add(lblRol);
        panel.add(btnLibros);
        panel.add(btnEstudiantes);
        panel.add(btnCategorias);
        panel.add(btnPrestamos);
        panel.add(btnReportes);
        panel.add(btnCerrarSesion);

        add(panel);
    }
}
