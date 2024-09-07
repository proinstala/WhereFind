
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar las localidades.
 */
public interface ILocalidadService {
    
    /**
     * Obtiene una lista de todas las localidades disponibles.
     *
     * Este método devuelve una lista de objetos {@link LocalidadDTO} que representan todas 
     * las localidades registradas en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * localidades disponibles.
     *
     * @return una lista de {@link LocalidadDTO} con todas las localidades, o una lista vacía si no hay localidades registradas.
     */
    public List<LocalidadDTO> getAllLocalidades();
    
    /**
     * Obtiene una lista de localidades que pertenecen a una provincia específica.
     *
     * <p>Este método devuelve una lista de objetos {@link LocalidadDTO} que representan las localidades 
     * que están asociadas con la provincia especificada. La provincia debe ser proporcionada como un objeto 
     * {@link ProvinciaDTO}. Las implementaciones deben manejar correctamente los casos en los que la provincia 
     * proporcionada sea nula o no tenga localidades asociadas.</p>
     *
     * @param provincia el objeto {@link ProvinciaDTO} que representa la provincia para la cual se desean obtener las localidades.
     * @return una lista de {@link LocalidadDTO} que pertenecen a la provincia especificada, o una lista vacía si no hay localidades asociadas a la provincia.
     */
    public List<LocalidadDTO> getLocalidadesOfProvincia(ProvinciaDTO provincia);
}
