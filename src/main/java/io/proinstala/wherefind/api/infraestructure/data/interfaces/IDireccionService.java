
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import java.util.List;


public interface IDireccionService {
    
    public DireccionDTO getDireccionById();
    public List<DireccionDTO> findDirecciones(String calle, int localidad, int provincia);
}
