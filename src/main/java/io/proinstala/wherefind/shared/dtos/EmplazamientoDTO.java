
package io.proinstala.wherefind.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmplazamientoDTO {
    
    private int id;
    private String nombre;
    private String descripcion;
    private TipoEmplazamientoDTO tipoEmplazamiento;
}
