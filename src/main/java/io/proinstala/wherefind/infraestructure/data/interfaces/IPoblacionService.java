package io.proinstala.wherefind.infraestructure.data.interfaces;

import java.util.List;

import io.proinstala.wherefind.shared.dtos.PoblacionDTO;

public interface IPoblacionService {
    public PoblacionDTO add(PoblacionDTO poblacion);
    public List<PoblacionDTO> getAllPoblaciones();
}
