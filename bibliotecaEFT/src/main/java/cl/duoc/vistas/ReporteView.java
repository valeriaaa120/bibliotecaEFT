package cl.duoc.vistas;

import cl.duoc.controlador.ReporteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReporteView extends JFrame {

    private final ReporteController controlador;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtIdEstudiante;

    public ReporteView() {

        controlador = new ReporteController();

        setTitle("Reportes de biblioteca");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelSuperior = new JPanel(new FlowLayout());

        JButton btnLibros = new JButton("Libros más prestados");
        JButton btnHistorial = new JButton("Historial por estudiante");
        JButton btnPrestamos = new JButton("Préstamos actuales");

        txtIdEstudiante = new JTextField(8);

        panelSuperior.add(btnLibros);
        panelSuperior.add(new JLabel("ID estudiante:"));
        panelSuperior.add(txtIdEstudiante);
        panelSuperior.add(btnHistorial);
        panelSuperior.add(btnPrestamos);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        setLayout(new BorderLayout(10, 10));
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnLibros.addActionListener(e -> mostrarLibrosMasPrestados());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnPrestamos.addActionListener(e -> mostrarPrestamosActuales());
    }

    private void mostrarLibrosMasPrestados() {

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        modelo.setColumnIdentifiers(new Object[]{
                "ID libro", "Título", "Autor", "Total de préstamos"
        });

        List<Object[]> datos = controlador.obtenerLibrosMasPrestados();

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay préstamos registrados o no se encontraron resultados."
            );
        }
    }

    private void mostrarHistorial() {

        int idEstudiante;

        try {
            idEstudiante = Integer.parseInt(
                    txtIdEstudiante.getText().trim()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa un ID de estudiante válido."
            );
            return;
        }

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        modelo.setColumnIdentifiers(new Object[]{
                "ID préstamo", "Estudiante", "Libro",
                "Fecha préstamo", "Fecha límite", "Devuelto"
        });

        List<Object[]> datos =
                controlador.obtenerHistorialEstudiante(idEstudiante);

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Este estudiante no tiene préstamos registrados."
            );
        }
    }

    private void mostrarPrestamosActuales() {

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        modelo.setColumnIdentifiers(new Object[]{
                "ID préstamo", "Estudiante", "Libro",
                "Fecha préstamo", "Fecha límite", "Estado"
        });

        List<Object[]> datos = controlador.obtenerPrestamosActuales();

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay préstamos pendientes ni atrasados."
            );
        }
    }
}