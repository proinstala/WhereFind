
package io.proinstala.wherefind.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDTO {
    
    private int id;
    private String calle;
    private String numero;
    private Integer codigoPostal;
    private LocalidadDTO localidad;
    private boolean activo;
}
