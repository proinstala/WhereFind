
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
    
    /**
     * Busca provincias cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link ProvinciaDTO} que contienen el nombre 
     * especificado o que coinciden parcialmente con él.
     *
     * @param nombre el nombre o parte del nombre de la provincia a buscar.
     * @return una lista de {@link ProvinciaDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<ProvinciaDTO> findProvincias(String nombre);
}
