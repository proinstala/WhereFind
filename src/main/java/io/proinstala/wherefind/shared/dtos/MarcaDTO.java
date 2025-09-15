
package io.proinstala.wherefind.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarcaDTO {
    
    private int id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private boolean activo;
    
}
