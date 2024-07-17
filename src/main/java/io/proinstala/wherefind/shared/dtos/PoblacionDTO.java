package io.proinstala.wherefind.shared.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class PoblacionDTO {
    private int id;
    private String name;
}
