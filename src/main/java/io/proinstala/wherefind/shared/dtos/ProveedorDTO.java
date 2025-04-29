
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
public class ProveedorDTO {
    
    private int id;
    private String nombre;
    private String descripcion;
    private String paginaWeb;
    private String urlImagen;
    private boolean activo;
    private DireccionDTO direccion;
    
    @Builder.Default
    private List<ContactoDTO> listaContactos = new ArrayList<>();
}
