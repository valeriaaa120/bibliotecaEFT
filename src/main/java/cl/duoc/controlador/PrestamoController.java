package cl.duoc.controlador;

import cl.duoc.dao.PrestamoDAO;
import cl.duoc.model.Prestamo;

import java.util.List;

public class PrestamoController {

    private final PrestamoDAO prestamoDAO;

    public PrestamoController() {
        prestamoDAO = new PrestamoDAO();
    }

    // Registrar un préstamo
    public boolean registrarPrestamo(int idEstudiante, int idLibro) {

        if (idEstudiante <= 0 || idLibro <= 0) {
            return false;
        }

        return prestamoDAO.registrarPrestamo(idEstudiante, idLibro);
    }

    // Mostrar todos los préstamos
    public List<Prestamo> listarPrestamos() {
        return prestamoDAO.listarTodos();
    }

    // Registrar la devolución de un libro
    public boolean registrarDevolucion(int idPrestamo) {

        if (idPrestamo <= 0) {
            return false;
        }

        return prestamoDAO.registrarDevolucion(idPrestamo);
    }
}
