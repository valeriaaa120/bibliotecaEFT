package cl.duoc.controlador;

import cl.duoc.dao.CategoriaDAO;
import cl.duoc.model.Categoria;

import java.util.List;

public class CategoriaController {

    private final CategoriaDAO categoriaDAO;

    public CategoriaController() {
        categoriaDAO = new CategoriaDAO();
    }

    public boolean validarCategoria(Categoria categoria) {
        return categoria != null
                && categoria.getNombre() != null
                && !categoria.getNombre().isBlank();
    }

    public boolean guardarCategoria(Categoria categoria) {
        if (!validarCategoria(categoria)) {
            return false;
        }

        return categoriaDAO.guardar(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarTodas();
    }

    public boolean actualizarCategoria(Categoria categoria) {
        if (!validarCategoria(categoria) || categoria.getId() <= 0) {
            return false;
        }

        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminarCategoria(int id) {
        if (id <= 0) {
            return false;
        }

        return categoriaDAO.eliminar(id);
    }
}