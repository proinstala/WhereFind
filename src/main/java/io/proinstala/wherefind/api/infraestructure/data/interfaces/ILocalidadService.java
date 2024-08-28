
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.util.List;


public interface ILocalidadService {
    
    public List<LocalidadDTO> getAllLocalidades();
    
    public List<LocalidadDTO> getLocalidadesOfProvincia(ProvinciaDTO provincia);
}
