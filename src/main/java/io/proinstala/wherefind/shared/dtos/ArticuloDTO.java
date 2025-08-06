
package io.proinstala.wherefind.shared.dtos;

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
    private int stockMinimo;
    private String imagen;
    private String marca;
    private String modelo;
}
