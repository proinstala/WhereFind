
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.TipoEmplazamientoDTO;
import java.util.List;


/**
 * Interfaz que define los métodos para gestionar los tipos de emplazamiento.
 * 
 * Proporciona operaciones básicas como obtener un tipo de emplazamiento por su identificador
 * y listar todos los tipos de emplazamiento disponibles en el sistema.
 * 
 * @author David
 */
public interface ITipoEmplazamientoService {
    
    /**
     * Obtiene un tipo de emplazamiento por su identificador único.
     *
     * @param id el ID del tipo de emplazamiento a buscar
     * @return el tipo de emplazamiento correspondiente al ID, o null si no se encuentra
     */
    TipoEmplazamientoDTO getTipoEmplazamientoById(int id);
    
    /**
     * Busca tipos de emplazamiento cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link TipoEmplazamientoDTO} que contienen el nombre 
     * especificado o que coinciden parcialmente con él.
     *
     * @param nombre el nombre o parte del nombre del tipo de emplazamiento a buscar.
     * @return una lista de {@link TipoEmplazamientoDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    List<TipoEmplazamientoDTO> findTiposEmplazamiento(String nombre);
    
    /**
     * Obtiene una lista con todos los tipos de emplazamiento disponibles.
     *
     * @return una lista de objetos TipoEmplazamientoDTO
     */
    List<TipoEmplazamientoDTO> getAllTiposEmplazamiento();
}
