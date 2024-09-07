
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar las provincias.
 */
public interface IProvinciaService {
    
    /**
     * Obtiene una lista de todas las provincias disponibles.
     *
     * Este método devuelve una lista de objetos {@link ProvinciaDTO} que representan todas 
     * las provincias registradas en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * provincias disponibles.
     *
     * @return una lista de {@link ProvinciaDTO} con todas las provincias, o una lista vacía si no hay provincias registradas.
     */
    public List<ProvinciaDTO> getAllProvincias();
}
