
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
public class ArticuloDTO {
    
    private int id;
    private String nombre;
    private String descripcion;
    private String referencia;
    private MarcaDTO marca;
    private String modelo;
    private int stockMinimo;
    private String imagen;
    private boolean activo;
    
    private int stockActual;
    
    @Builder.Default
    private List<ArticuloProveedorDTO> listaProveedores = new ArrayList<>();
   
}
