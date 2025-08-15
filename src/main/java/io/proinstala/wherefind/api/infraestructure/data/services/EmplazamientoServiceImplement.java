
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IEmplazamientoService;
import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import java.util.List;

/**
 *
 * @author David
 */
public class EmplazamientoServiceImplement extends BaseMySql implements IEmplazamientoService {
    
    

    @Override
    public EmplazamientoDTO getEmplazamientoById(int idEmplazamiento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<EmplazamientoDTO> getAllEmplazamientos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<EmplazamientoDTO> findEmplazamientos(int almacenId, int tipoEmplazamientoId, String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EmplazamientoDTO createEmplazamiento(EmplazamientoDTO emplazamientoDTO) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateEmplazamiento(EmplazamientoDTO emplazamientoDTO) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteEmplazamiento(int emplazamientoId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
