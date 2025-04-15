
package io.proinstala.wherefind.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactoDTO {
    
    private int id;
    private String nombre;
    private String apellido;
    private String puesto;
    private String telefono;
    private String email;
    private boolean activo;
    
    private ProveedorDTO proveedor;
}
