package io.proinstala.wherefind.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private int id;
    private String userName;
    private String password;
    private String rol;
    private String nombre;
    private String apellidos;
    private String email;
    private String imagen;
}