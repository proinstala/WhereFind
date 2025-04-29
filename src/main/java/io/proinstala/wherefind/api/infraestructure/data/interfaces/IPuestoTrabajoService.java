
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import java.util.List;

/**
 *
 * @author David
 */
public interface IPuestoTrabajoService {
    
    PuestoTrabajoDTO getPuestoById(int id);
    List<PuestoTrabajoDTO> getAllPuestos();
}
