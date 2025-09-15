
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IMarcaService;
import io.proinstala.wherefind.shared.dtos.MarcaDTO;
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
public class MarcaServiceImplement extends BaseMySql implements IMarcaService {
    
    private static final String SQL_SELECT_COMUN = 
            """
            SELECT
             m.*
            FROM MARCA m
            """;
    
    private static final String SQL_UPDATE_MARCA = 
        "UPDATE MARCA SET nombre = ?, descripcion = ? " +
        "WHERE id = ?;";

    private static final String SQL_CREATE_MARCA = 
        "INSERT INTO MARCA (nombre, descripcion) " +
        "VALUES (?, ?, ?);";
    
    private static final String SQL_DELETE_MARCA = 
        "UPDATE MARCA SET activo = FALSE WHERE id = ?;";
    
    private MarcaDTO getMarcaFromResultSet(ResultSet rs) throws SQLException {
        
        MarcaDTO marcaDTO = MarcaDTO.builder()
            .id(rs.getInt("m.id"))
            .nombre(rs.getString("m.nombre"))
            .descripcion(rs.getString("m.descripcion"))
            .activo(rs.getBoolean("m.activo"))
            .build();

        return marcaDTO;
    }

    @Override
    public MarcaDTO getMarcaById(int idMarca) {
        MarcaDTO marcaDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE m.activo = TRUE");
        sql.append(" AND m.id = ?");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idMarca);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    marcaDTO = getMarcaFromResultSet(resultSet);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marcaDTO;
    }

    @Override
    public List<MarcaDTO> getAllMarcas() {
        List<MarcaDTO> listaMarcas = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE m.activo = TRUE");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    MarcaDTO marcaDTO = getMarcaFromResultSet(resultSet);
                    listaMarcas.add(marcaDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaMarcas;
    }

    @Override
    public List<MarcaDTO> findMarcas(String nombre, String descripcion) {
        List<MarcaDTO> listaMarcas = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE m.activo = TRUE");
        
        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND m.nombre LIKE ?");
        }
        
        // Condición por descripcion
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND m.descripcion LIKE ?");
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

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    MarcaDTO marcaDTO = getMarcaFromResultSet(resultSet);
                    listaMarcas.add(marcaDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaMarcas;
    }

    @Override
    public MarcaDTO createMarca(MarcaDTO marcaDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_MARCA, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, marcaDTO.getNombre());
            ps.setString(2, marcaDTO.getDescripcion());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        marcaDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return marcaDTO;
    }

    @Override
    public boolean updateMarca(MarcaDTO marcaDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_MARCA)) {

            ps.setString(1, marcaDTO.getNombre());
            ps.setString(2, marcaDTO.getDescripcion());
            ps.setInt(3, marcaDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteMarca(int marcaId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_MARCA)) {

            ps.setInt(1, marcaId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
    
}
