
package cl.duoc.vistas;

import cl.duoc.controlador.EstudianteController;
import cl.duoc.model.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstudianteView extends JFrame {

    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private final EstudianteController controlador;

    private int idSeleccionado = 0;

    public EstudianteView() {
        controlador = new EstudianteController();

        setTitle("Gestión de estudiantes");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarEstudiantes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 8, 8));

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("RUT:"));
        txtRut = new JTextField();
        panelFormulario.add(txtRut);

        panelFormulario.add(new JLabel("Curso:"));
        txtCurso = new JTextField();
        panelFormulario.add(txtCurso);

        panelFormulario.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "RUT", "Curso", "Correo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tabla);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnListar = new JButton("Actualizar lista");

        JPanel panelBotones = new JPanel(new FlowLayout());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnListar);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnGuardar.addActionListener(e -> guardarEstudiante());
        btnActualizar.addActionListener(e -> actualizarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnListar.addActionListener(e -> cargarEstudiantes());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarEstudiante();
            }
        });
    }

    private void guardarEstudiante() {
        Estudiante estudiante = obtenerEstudianteFormulario();

        if (estudiante == null) {
            return;
        }

        if (controlador.guardarEstudiante(estudiante)) {
            JOptionPane.showMessageDialog(
                    this, "Estudiante guardado correctamente."
            );
            limpiarFormulario();
            cargarEstudiantes();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el estudiante. Revisa los datos "
                            + "y la consola de IntelliJ.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarEstudiante() {
        if (idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona un estudiante de la tabla."
            );
            return;
        }

        Estudiante estudiante = obtenerEstudianteFormulario();

        if (estudiante == null) {
            return;
        }

        estudiante.setId(idSeleccionado);

        if (controlador.actualizarEstudiante(estudiante)) {
            JOptionPane.showMessageDialog(
                    this, "Estudiante actualizado correctamente."
            );
            limpiarFormulario();
            cargarEstudiantes();
        } else {
            JOptionPane.showMessageDialog(
                    this, "No se pudo actualizar el estudiante."
            );
        }
    }

    private void eliminarEstudiante() {
        if (idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona un estudiante de la tabla."
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este estudiante?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controlador.eliminarEstudiante(idSeleccionado)) {
                JOptionPane.showMessageDialog(
                        this, "Estudiante eliminado correctamente."
                );
                limpiarFormulario();
                cargarEstudiantes();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar. Puede tener préstamos asociados."
                );
            }
        }
    }

    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);

        List<Estudiante> estudiantes = controlador.listarEstudiantes();

        for (Estudiante estudiante : estudiantes) {
            modeloTabla.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
            });
        }
    }

    private void seleccionarEstudiante() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            idSeleccionado = Integer.parseInt(
                    modeloTabla.getValueAt(fila, 0).toString()
            );

            txtNombre.setText(
                    modeloTabla.getValueAt(fila, 1).toString()
            );
            txtRut.setText(
                    modeloTabla.getValueAt(fila, 2).toString()
            );
            txtCurso.setText(
                    modeloTabla.getValueAt(fila, 3).toString()
            );
            txtCorreo.setText(
                    modeloTabla.getValueAt(fila, 4).toString()
            );
        }
    }

    private Estudiante obtenerEstudianteFormulario() {
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || rut.isEmpty()
                || curso.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Completa todos los campos."
            );
            return null;
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre);
        estudiante.setRut(rut);
        estudiante.setCurso(curso);
        estudiante.setCorreo(correo);

        return estudiante;
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        idSeleccionado = 0;
        tabla.clearSelection();
    }
}