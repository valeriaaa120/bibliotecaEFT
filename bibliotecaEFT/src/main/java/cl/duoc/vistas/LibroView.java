package cl.duoc.vistas;

import cl.duoc.controlador.LibroController;
import cl.duoc.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibroView extends JFrame {

    private final LibroController controlador;

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JTextField txtIdCategoria;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnListar;
    private JButton btnSeleccionar;
    private JButton btnActualizar;
    private JButton btnEliminar;

    public LibroView() {
        controlador = new LibroController();

        setTitle("Biblioteca Escolar - Gestión de libros");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarLibros();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel lblTitulo = new JLabel(
                "GESTIÓN DE LIBROS",
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // FORMULARIO
        JPanel panelFormulario = new JPanel(
                new GridLayout(7, 2, 8, 8)
        );
        panelFormulario.setBorder(
                BorderFactory.createTitledBorder("Datos del libro")
        );

        txtId = new JTextField();
        txtId.setEditable(false);

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtEditorial = new JTextField();
        txtStock = new JTextField();
        txtIdCategoria = new JTextField();

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtAutor);

        panelFormulario.add(new JLabel("ISBN:"));
        panelFormulario.add(txtIsbn);

        panelFormulario.add(new JLabel("Editorial:"));
        panelFormulario.add(txtEditorial);

        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(txtStock);

        panelFormulario.add(new JLabel("ID de categoría:"));
        panelFormulario.add(txtIdCategoria);

        // BOTONES
        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnListar = new JButton("Listar libros");
        btnSeleccionar = new JButton("Seleccionar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 8, 8));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnListar);
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA
        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID", "Título", "Autor", "ISBN",
                        "Editorial", "Stock", "ID Categoría"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        tablaLibros.setAutoCreateRowSorter(true);

        JScrollPane scrollTabla = new JScrollPane(tablaLibros);
        scrollTabla.setBorder(
                BorderFactory.createTitledBorder("Libros registrados")
        );

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.add(panelSuperior, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        add(panelPrincipal);

        // CONEXIÓN DE LOS BOTONES
        btnGuardar.addActionListener(e -> guardarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnListar.addActionListener(e -> cargarLibros());
        btnSeleccionar.addActionListener(e -> seleccionarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());

        // Permite seleccionar una fila con doble clic.
        tablaLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    seleccionarLibro();
                }
            }
        });
    }

    // GUARDAR LIBRO
    private void guardarLibro() {
        try {
            Libro libro = obtenerLibroDelFormulario();

            boolean guardado = controlador.guardarLibro(libro);

            if (guardado) {
                JOptionPane.showMessageDialog(
                        this, "Libro guardado correctamente."
                );
                limpiarFormulario();
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo guardar el libro. " +
                                "Revisa los datos y la base de datos."
                );
            }

        } catch (NumberFormatException e) {
            mostrarErrorNumerico();
        }
    }

    // LISTAR LIBROS
    private void cargarLibros() {
        modeloTabla.setRowCount(0);

        List<Libro> libros = controlador.listarLibros();

        for (Libro libro : libros) {
            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    libro.getIdCategoria()
            });
        }
    }

    // SELECCIONAR LIBRO DE LA TABLA
    private void seleccionarLibro() {
        int filaVista = tablaLibros.getSelectedRow();

        if (filaVista == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un libro de la tabla."
            );
            return;
        }

        int fila = tablaLibros.convertRowIndexToModel(filaVista);

        txtId.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 0)
        ));
        txtTitulo.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 1)
        ));
        txtAutor.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 2)
        ));
        txtIsbn.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 3)
        ));
        txtEditorial.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 4)
        ));
        txtStock.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 5)
        ));
        txtIdCategoria.setText(String.valueOf(
                modeloTabla.getValueAt(fila, 6)
        ));
    }

    // ACTUALIZAR LIBRO
    private void actualizarLibro() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero selecciona un libro de la tabla."
            );
            return;
        }

        try {
            Libro libro = obtenerLibroDelFormulario();
            libro.setId(Integer.parseInt(txtId.getText().trim()));

            boolean actualizado =
                    controlador.actualizarLibro(libro);

            if (actualizado) {
                JOptionPane.showMessageDialog(
                        this, "Libro actualizado correctamente."
                );
                limpiarFormulario();
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el libro. " +
                                "Revisa los datos."
                );
            }

        } catch (NumberFormatException e) {
            mostrarErrorNumerico();
        }
    }

    // ELIMINAR LIBRO
    private void eliminarLibro() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero selecciona un libro de la tabla."
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar este libro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());

            boolean eliminado = controlador.eliminarLibro(id);

            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this, "Libro eliminado correctamente."
                );
                limpiarFormulario();
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el libro. " +
                                "Puede tener préstamos asociados."
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "El ID del libro no es válido."
            );
        }
    }

    // OBTENER LOS DATOS DEL FORMULARIO
    private Libro obtenerLibroDelFormulario() {
        Libro libro = new Libro();

        libro.setTitulo(txtTitulo.getText().trim());
        libro.setAutor(txtAutor.getText().trim());
        libro.setIsbn(txtIsbn.getText().trim());
        libro.setEditorial(txtEditorial.getText().trim());
        libro.setStock(
                Integer.parseInt(txtStock.getText().trim())
        );
        libro.setIdCategoria(
                Integer.parseInt(txtIdCategoria.getText().trim())
        );

        return libro;
    }

    // LIMPIAR FORMULARIO
    private void limpiarFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        txtIdCategoria.setText("");

        tablaLibros.clearSelection();
        txtTitulo.requestFocus();
    }

    private void mostrarErrorNumerico() {
        JOptionPane.showMessageDialog(
                this,
                "El stock y el ID de categoría deben ser números enteros.",
                "Datos inválidos",
                JOptionPane.WARNING_MESSAGE
        );
    }
}