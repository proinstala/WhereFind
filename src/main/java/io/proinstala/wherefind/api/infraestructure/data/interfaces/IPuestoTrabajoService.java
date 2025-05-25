
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar los puestos de trabajo.
 * 
 * Proporciona operaciones básicas como obtener un puesto por su identificador
 * y listar todos los puestos disponibles en el sistema.
 * 
 * @author David
 */
public interface IPuestoTrabajoService {
    
    /**
     * Obtiene un puesto de trabajo por su identificador único.
     *
     * @param id el ID del puesto de trabajo a buscar
     * @return el puesto de trabajo correspondiente al ID, o null si no se encuentra
     */
    PuestoTrabajoDTO getPuestoById(int id);
    
    /**
     * Busca puesto de trabajo cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link PuestoTrabajoDTO} que contienen el nombre 
     * especificado o que coinciden parcialmente con él.
     *
     * @param nombre el nombre o parte del nombre del puesto a buscar.
     * @return una lista de {@link PuestoTrabajoDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    List<PuestoTrabajoDTO> findPuestos(String nombre);
    
    /**
     * Obtiene una lista con todos los puestos de trabajo disponibles.
     *
     * @return una lista de objetos PuestoTrabajoDTO
     */
    List<PuestoTrabajoDTO> getAllPuestos();
}
