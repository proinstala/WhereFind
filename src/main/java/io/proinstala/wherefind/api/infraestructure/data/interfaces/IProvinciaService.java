
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.util.List;

/**
 *
 * @author David
 */
public interface IProvinciaService {
    
    public List<ProvinciaDTO> getAllProvincias();
}
