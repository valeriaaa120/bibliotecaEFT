
package cl.duoc.controlador;


import cl.duoc.dao.ReportesDAO;

import java.util.List;

public class ReporteController {

    private final cl.duoc.dao.ReportesDAO reporteDAO;

    public ReporteController() {
        reporteDAO = new ReportesDAO();
    }

    // Reporte de libros más prestados
    public List<Object[]> obtenerLibrosMasPrestados() {
        return reporteDAO.librosMasPrestados();
    }

    // Historial de préstamos de un estudiante
    public List<Object[]> obtenerHistorialEstudiante(int idEstudiante) {

        if (idEstudiante <= 0) {
            return List.of();
        }

        return reporteDAO.historialEstudiante(idEstudiante);
    }

    // Listado de préstamos actuales
    public List<Object[]> obtenerPrestamosActuales() {
        return reporteDAO.prestamosActuales();
    }
}