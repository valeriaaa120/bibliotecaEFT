package cl.duoc.vistas;

import cl.duoc.controlador.PrestamoController;
import cl.duoc.model.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PrestamoView extends JFrame {

    private JTextField txtIdEstudiante;
    private JTextField txtIdLibro;
    private JTextField txtIdPrestamo;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;

    private final PrestamoController controlador;

    public PrestamoView() {

        controlador = new PrestamoController();

        setTitle("Gestión de préstamos");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 8, 8));
        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        panelFormulario.add(new JLabel("ID del estudiante:"));
        txtIdEstudiante = new JTextField();
        panelFormulario.add(txtIdEstudiante);

        panelFormulario.add(new JLabel("ID del libro:"));
        txtIdLibro = new JTextField();
        panelFormulario.add(txtIdLibro);

        panelFormulario.add(new JLabel("ID del préstamo:"));
        txtIdPrestamo = new JTextField();
        panelFormulario.add(txtIdPrestamo);

        JButton btnRegistrar = new JButton("Registrar préstamo");
        JButton btnDevolver = new JButton("Registrar devolución");

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnDevolver);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID préstamo",
                        "ID estudiante",
                        "ID libro",
                        "Fecha préstamo",
                        "Fecha límite",
                        "Devuelto",
                        "Estado"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPrestamos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaPrestamos);

        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnSeleccionar = new JButton("Seleccionar préstamo");

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnActualizar);
        panelInferior.add(btnSeleccionar);

        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
        btnActualizar.addActionListener(e -> listarPrestamos());
        btnSeleccionar.addActionListener(e -> seleccionarPrestamo());

        listarPrestamos();
    }

    private void registrarPrestamo() {

        try {
            int idEstudiante = Integer.parseInt(
                    txtIdEstudiante.getText().trim()
            );

            int idLibro = Integer.parseInt(
                    txtIdLibro.getText().trim()
            );

            boolean registrado = controlador.registrarPrestamo(
                    idEstudiante, idLibro
            );

            if (registrado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Préstamo registrado correctamente. " +
                                "La fecha límite es dentro de 14 días."
                );

                txtIdEstudiante.setText("");
                txtIdLibro.setText("");

                listarPrestamos();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el préstamo. " +
                                "Verifica los ID y que el libro tenga stock."
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa números válidos para el ID del estudiante y del libro."
            );
        }
    }

    private void registrarDevolucion() {

        try {
            int idPrestamo = Integer.parseInt(
                    txtIdPrestamo.getText().trim()
            );

            boolean devuelto = controlador.registrarDevolucion(idPrestamo);

            if (devuelto) {
                JOptionPane.showMessageDialog(
                        this,
                        "Devolución registrada correctamente."
                );

                txtIdPrestamo.setText("");
                listarPrestamos();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la devolución. " +
                                "Verifica el ID o si ya fue devuelto."
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa un ID de préstamo válido."
            );
        }
    }

    private void seleccionarPrestamo() {

        int fila = tablaPrestamos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un préstamo de la tabla."
            );
            return;
        }

        txtIdPrestamo.setText(
                modeloTabla.getValueAt(fila, 0).toString()
        );
    }

    private void listarPrestamos() {

        modeloTabla.setRowCount(0);

        List<Prestamo> prestamos = controlador.listarPrestamos();

        for (Prestamo prestamo : prestamos) {

            String estado;

            if (prestamo.isDevuelto()) {
                estado = "Devuelto";
            } else if (prestamo.getFechaDevolucion() != null
                    && prestamo.getFechaDevolucion()
                    .isBefore(LocalDate.now())) {
                estado = "Atrasado";
            } else {
                estado = "Pendiente";
            }

            modeloTabla.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getIdEstudiante(),
                    prestamo.getIdLibro(),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    prestamo.isDevuelto() ? "Sí" : "No",
                    estado
            });
        }
    }
}
