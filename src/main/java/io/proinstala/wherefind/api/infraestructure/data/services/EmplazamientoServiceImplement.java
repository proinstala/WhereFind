
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IEmplazamientoService;
import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import java.util.List;

/**
 *
 * @author David
 */
public class EmplazamientoServiceImplement extends BaseMySql implements IEmplazamientoService {
    
    private static final String SQL_SELECT_COMUN = 
           """                            
           SELECT
            e.*,
            d.id AS d_id, d.calle AS d_calle, d.numero AS d_numero, d.codigo_postal AS d_codigo_postal, d.localidad_id AS d_localidad_id, d.activo AS d_activo,
            l.id AS l_id, l.nombre AS l_nombre, l.provincia_id AS l_provincia_id,
            pr.id AS pr_id, pr.nombre AS pr_nombre
           FROM EMPLAZAMIENTO e
           INNER JOIN ALMACEN a ON e.almacen_id = a.id
           INNER JOIN TIPO_EMPLAZAMIENTO t ON e.tipo_id = t.id
           """;

    private static final String SQL_UPDATE_EMPLAZAMIENTO = 
        "UPDATE EMPLAZAMIENTO SET nombre = ?, descripcion = ?, tipo_id = ?, " +
        "almacen_id = ?;";

    private static final String SQL_CREATE_EMPLAZAMIENTO = 
        "INSERT INTO EMPLAZAMIENTO (nombre, descripcion, tipo_id, almacen_id) " +
        "VALUES (?, ?, ?, ?);";

    private static final String SQL_DELETE_EMPLAZAMIENTO = 
        "UPDATE EMPLAZAMIENTO SET activo = FALSE WHERE id = ?;";
    

    @Override
    public EmplazamientoDTO getEmplazamientoById(int idEmplazamiento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<EmplazamientoDTO> getAllEmplazamientos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<EmplazamientoDTO> findEmplazamientos(String nombre, String descripcion, int tipoEmplazamientoId, int almacenId) {
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
