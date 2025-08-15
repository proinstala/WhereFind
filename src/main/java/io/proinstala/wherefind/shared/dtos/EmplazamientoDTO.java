
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
public class EmplazamientoDTO {
    
    private int id;
    private String nombre;
    private String descripcion;
    private TipoEmplazamientoDTO tipoEmplazamiento;
    private AlmacenDTO almacen;
    
    @Builder.Default
    List<ExistenciaDTO> listaExistencias = new ArrayList<>();
}
