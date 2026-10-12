package cl.duoc.vistas;

import cl.duoc.controlador.CategoriaController;
import cl.duoc.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaView extends JFrame {

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private final CategoriaController controlador;
    private int idSeleccionado = 0;

    public CategoriaView() {
        controlador = new CategoriaController();

        setTitle("Gestión de categorías");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarCategorias();
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel formulario = new JPanel(new GridLayout(1, 2, 8, 8));
        formulario.add(new JLabel("Nombre de la categoría:"));

        txtNombre = new JTextField();
        formulario.add(txtNombre);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnListar = new JButton("Actualizar lista");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnListar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        add(principal);

        btnGuardar.addActionListener(e -> guardarCategoria());
        btnActualizar.addActionListener(e -> actualizarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnListar.addActionListener(e -> cargarCategorias());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarCategoria();
            }
        });
    }

    private void guardarCategoria() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Escribe el nombre de la categoría."
            );
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);

        if (controlador.guardarCategoria(categoria)) {
            JOptionPane.showMessageDialog(
                    this, "Categoría guardada correctamente."
            );
            limpiarFormulario();
            cargarCategorias();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar la categoría. Revisa la consola.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarCategoria() {
        if (idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona una categoría de la tabla."
            );
            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Escribe el nombre de la categoría."
            );
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setId(idSeleccionado);
        categoria.setNombre(nombre);

        if (controlador.actualizarCategoria(categoria)) {
            JOptionPane.showMessageDialog(
                    this, "Categoría actualizada correctamente."
            );
            limpiarFormulario();
            cargarCategorias();
        } else {
            JOptionPane.showMessageDialog(
                    this, "No se pudo actualizar la categoría."
            );
        }
    }

    private void eliminarCategoria() {
        if (idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona una categoría de la tabla."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar esta categoría?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            if (controlador.eliminarCategoria(idSeleccionado)) {
                JOptionPane.showMessageDialog(
                        this, "Categoría eliminada correctamente."
                );
                limpiarFormulario();
                cargarCategorias();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar. Puede haber libros asociados."
                );
            }
        }
    }

    private void cargarCategorias() {
        modeloTabla.setRowCount(0);

        List<Categoria> categorias = controlador.listarCategorias();

        for (Categoria categoria : categorias) {
            modeloTabla.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getNombre()
            });
        }
    }

    private void seleccionarCategoria() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            idSeleccionado = Integer.parseInt(
                    modeloTabla.getValueAt(fila, 0).toString()
            );

            txtNombre.setText(
                    modeloTabla.getValueAt(fila, 1).toString()
            );
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        idSeleccionado = 0;
        tabla.clearSelection();
    }
}