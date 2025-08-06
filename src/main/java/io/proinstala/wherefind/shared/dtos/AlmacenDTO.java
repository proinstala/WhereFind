
package io.proinstala.wherefind.shared.dtos;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlmacenDTO {
    
    private int id;
    private String nombre;
    private String descripcion;
    private DireccionDTO direccion;
    private boolean activo;
    
    @Builder.Default
    private List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
}
