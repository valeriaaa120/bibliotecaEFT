package cl.duoc.controlador;

import cl.duoc.dao.EstudianteDAO;
import cl.duoc.model.Estudiante;

import java.util.List;

public class EstudianteController {

    private final EstudianteDAO estudianteDAO;

    public EstudianteController() {
        estudianteDAO = new EstudianteDAO();
    }

    public boolean validarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            return false;
        }

        if (estudiante.getNombre() == null
                || estudiante.getNombre().isBlank()) {
            return false;
        }

        if (estudiante.getRut() == null
                || estudiante.getRut().isBlank()) {
            return false;
        }

        if (estudiante.getCurso() == null
                || estudiante.getCurso().isBlank()) {
            return false;
        }

        if (estudiante.getCorreo() == null
                || estudiante.getCorreo().isBlank()) {
            return false;
        }

        return estudiante.getCorreo().contains("@");
    }

    public boolean guardarEstudiante(Estudiante estudiante) {
        if (!validarEstudiante(estudiante)) {
            return false;
        }

        return estudianteDAO.guardar(estudiante);
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.listarTodos();
    }

    public boolean actualizarEstudiante(Estudiante estudiante) {
        if (!validarEstudiante(estudiante)
                || estudiante.getId() <= 0) {
            return false;
        }

        return estudianteDAO.actualizar(estudiante);
    }

    public boolean eliminarEstudiante(int id) {
        if (id <= 0) {
            return false;
        }

        return estudianteDAO.eliminar(id);
    }
}
