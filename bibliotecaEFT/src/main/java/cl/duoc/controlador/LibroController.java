package cl.duoc.controlador;

import cl.duoc.dao.LibroDAO;
import cl.duoc.model.Libro;

import java.util.List;

public class LibroController {

    private final LibroDAO libroDAO;

    public LibroController() {
        libroDAO = new LibroDAO();
    }

    public boolean validarLibro(Libro libro) {
        if (libro == null) {
            return false;
        }

        if (libro.getTitulo() == null
                || libro.getTitulo().isBlank()) {
            return false;
        }

        if (libro.getAutor() == null
                || libro.getAutor().isBlank()) {
            return false;
        }

        if (libro.getIsbn() == null
                || libro.getIsbn().isBlank()) {
            return false;
        }

        if (libro.getEditorial() == null
                || libro.getEditorial().isBlank()) {
            return false;
        }

        return libro.getStock() >= 0
                && libro.getIdCategoria() > 0;
    }

    public boolean guardarLibro(Libro libro) {
        if (!validarLibro(libro)) {
            return false;
        }

        return libroDAO.guardar(libro);
    }

    public List<Libro> listarLibros() {
        return libroDAO.listarTodos();
    }

    public boolean actualizarLibro(Libro libro) {
        if (!validarLibro(libro) || libro.getId() <= 0) {
            return false;
        }

        return libroDAO.actualizar(libro);
    }

    public boolean eliminarLibro(int id) {
        if (id <= 0) {
            return false;
        }

        return libroDAO.eliminar(id);
    }
}