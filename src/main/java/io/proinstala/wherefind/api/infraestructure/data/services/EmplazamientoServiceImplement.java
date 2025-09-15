
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IEmplazamientoService;
import io.proinstala.wherefind.shared.dtos.AlmacenDTO;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import io.proinstala.wherefind.shared.dtos.TipoEmplazamientoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class EmplazamientoServiceImplement extends BaseMySql implements IEmplazamientoService {
    //t.id AS tipo_id, t.nombre AS tipo_nombre,
    private static final String SQL_SELECT_COMUN2 = 
           """                            
           SELECT
            e.*,
            t.id, t.nombre,
            a.id AS almacen_id, a.nombre AS almacen_nombre, a.descripcion AS almacen_descripcion, 
            a.direccion_id AS almacen_direccion_id, a.activo AS almacen_activo
           FROM EMPLAZAMIENTO e
           INNER JOIN TIPO_EMPLAZAMIENTO t ON e.tipo_id = t.id
           INNER JOIN ALMACEN a ON e.almacen_id = a.id
           """;
    
    private static final String SQL_SELECT_COMUN = 
           """                            
           SELECT
            e.*,
            t.id, t.nombre,
            a.id, a.nombre, a.descripcion, 
            a.direccion_id, a.activo
           FROM EMPLAZAMIENTO e
           INNER JOIN TIPO_EMPLAZAMIENTO t ON e.tipo_id = t.id
           INNER JOIN ALMACEN a ON e.almacen_id = a.id
           """;

    private static final String SQL_UPDATE_EMPLAZAMIENTO = 
        "UPDATE EMPLAZAMIENTO SET nombre = ?, descripcion = ?, tipo_id = ?, almacen_id = ? " +
        "WHERE id = ?;";

    private static final String SQL_CREATE_EMPLAZAMIENTO = 
        "INSERT INTO EMPLAZAMIENTO (nombre, descripcion, tipo_id, almacen_id) " +
        "VALUES (?, ?, ?, ?);";

    private static final String SQL_DELETE_EMPLAZAMIENTO = 
        "UPDATE EMPLAZAMIENTO SET activo = FALSE WHERE id = ?;";
    
    
    private static EmplazamientoDTO getEmplazamientoFromResultSet(ResultSet rs) throws SQLException {

        TipoEmplazamientoDTO tipoEmplazamiento = TipoEmplazamientoDTO.builder()
                .id(rs.getInt("t.id"))
                .nombre(rs.getString("t.nombre"))
                .build();

        DireccionDTO direccionDTO = DireccionDTO.builder()
                .id(rs.getInt("a.direccion_id"))
                .build();

        AlmacenDTO almacenDTO = AlmacenDTO.builder()
                .id(rs.getInt("a.id"))
                .nombre(rs.getString("a.nombre"))
                .descripcion(rs.getString("a.descripcion"))
                .direccion(direccionDTO)
                .activo(rs.getBoolean("a.activo"))
                .build();

        EmplazamientoDTO emplazamientoDTO = EmplazamientoDTO.builder()
                .id(rs.getInt("e.id"))
                .nombre(rs.getString("e.nombre"))
                .descripcion(rs.getString("e.descripcion"))
                .tipoEmplazamiento(tipoEmplazamiento)
                .almacen(almacenDTO)
                .build();

        return emplazamientoDTO;
    }
    

    @Override
    public EmplazamientoDTO getEmplazamientoById(int idEmplazamiento) {
        EmplazamientoDTO emplazamientoDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE e.activo = TRUE");
        sql.append(" AND e.id = ?");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idEmplazamiento);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    emplazamientoDTO = getEmplazamientoFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emplazamientoDTO;
    }

    @Override
    public List<EmplazamientoDTO> getAllEmplazamientos() {
        List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE e.activo = TRUE");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    EmplazamientoDTO emplazamientoDTO = getEmplazamientoFromResultSet(resultSet);
                    listaEmplazamientos.add(emplazamientoDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmplazamientos;
    }

    @Override
    public List<EmplazamientoDTO> findEmplazamientos(String nombre, String descripcion, int tipoEmplazamientoId, int almacenId) {
        List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE e.activo = TRUE");
        
        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND e.nombre LIKE ?");
        }
        
        // Condición por descripcion
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND e.descripcion LIKE ?");
        }
        
        
        // Condición por descripcion
        if (tipoEmplazamientoId != -1) {
            sql.append(" AND e.tipo_id = ?");
        }
        
        // Condición por descripcion
        if (almacenId != -1) {
            sql.append(" AND e.almacen_id = ?");
        }
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            int index = 1;
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                ps.setString(index++, "%" + nombre + "%");
            }

            if (descripcion != null && !descripcion.trim().isEmpty()) {
                ps.setString(index++, "%" + descripcion + "%");
            }
            
            if (tipoEmplazamientoId != -1) {
                ps.setInt(index++, tipoEmplazamientoId);
            }
            
            if (almacenId != -1) {
                ps.setInt(index++, almacenId);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    EmplazamientoDTO emplazamientoDTO = getEmplazamientoFromResultSet(resultSet);
                    listaEmplazamientos.add(emplazamientoDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmplazamientos;
    }

    @Override
    public EmplazamientoDTO createEmplazamiento(EmplazamientoDTO emplazamientoDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_EMPLAZAMIENTO, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, emplazamientoDTO.getNombre());
            ps.setString(2, emplazamientoDTO.getDescripcion());
            ps.setInt(3, emplazamientoDTO.getTipoEmplazamiento().getId());
            ps.setInt(4, emplazamientoDTO.getAlmacen().getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        emplazamientoDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return emplazamientoDTO;
    }

    @Override
    public boolean updateEmplazamiento(EmplazamientoDTO emplazamientoDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_EMPLAZAMIENTO)) {

            ps.setString(1, emplazamientoDTO.getNombre());
            ps.setString(2, emplazamientoDTO.getDescripcion());
            ps.setInt(3, emplazamientoDTO.getTipoEmplazamiento().getId());
            ps.setInt(4, emplazamientoDTO.getAlmacen().getId());
            ps.setInt(5, emplazamientoDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteEmplazamiento(int emplazamientoId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_EMPLAZAMIENTO)) {

            ps.setInt(1, emplazamientoId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
    
}
