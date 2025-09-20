
package io.proinstala.wherefind.shared.dtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloProveedorDTO {
    
    private int id;
    private ArticuloDTO articulo;
    private ProveedorDTO proveedor;
    private double precio;
    private LocalDate fechaPrecio;
    private boolean disponible;
    private LocalDate fechaNoDisponible;
}
