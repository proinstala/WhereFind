
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DireccionServiceImplement extends BaseMySql implements IDireccionService {

    private static final String SQL_SELECT_DIRECCION_BY_ID = "SELECT d.*, l.*, p.* FROM DIRECCION d INNER JOIN LOCALIDAD l ON(d.localidad_id = l.id) INNER JOIN PROVINCIA p ON(l.provincia_id = p.id) WHERE d.id = ?;";
    private static final String SQL_SELECT_DIRECCIONES = "SELECT d.*, l.*, p.* FROM DIRECCION d INNER JOIN LOCALIDAD l ON(d.localidad_id = l.id) INNER JOIN PROVINCIA p ON(l.provincia_id = p.id);"; 
    
    private static DireccionDTO getDireccionFromResultSet(ResultSet rs) throws SQLException {
       
        return new DireccionDTO(
                rs.getInt("d.id"),
                rs.getString("d.calle"),
                (rs.getString("d.numero") != null) ? rs.getString("d.numero") : "",
                rs.getInt("d.codigo_postal"),
                new LocalidadDTO(
                        rs.getInt("l.id"), 
                        rs.getString("l.nombre"),
                        new ProvinciaDTO(
                                rs.getInt("p.id"), 
                                rs.getString("p.nombre")
                        )
                ),
                rs.getBoolean("activo")
        );
    }
    
    
    @Override
    public DireccionDTO getDireccionById(int idDireccion) {
        DireccionDTO direccionDTO = null;
        
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_DIRECCION_BY_ID)) {
            
            ps.setInt(1, idDireccion);
            
            //Ejecutar la consulta y obtener el ResultSet dentro de otro bloque try-with-resources
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    direccionDTO = getDireccionFromResultSet(resultSet); //Crear un objeto DireccionDTO a partir del ResultSet
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return direccionDTO;
    }

    @Override
    public List<DireccionDTO> findDirecciones(String calle, int IdLocalidad, int IdProvincia) {
        List<DireccionDTO> listaDireccionesDTO = new ArrayList<>();
        
        StringBuilder sentenciaSQL = new StringBuilder(SQL_SELECT_DIRECCIONES);
        
        //Eliminar el punto y coma al final de SQL_SELECT_DIRECCIONES
        if (sentenciaSQL.charAt(sentenciaSQL.length() - 1) == ';') {
            sentenciaSQL.deleteCharAt(sentenciaSQL.length() - 1);
        }

        //Agregar la cláusula WHERE y la condicion de busqueda por nombre de calle
        sentenciaSQL.append(" WHERE d.calle LIKE ?");

        //Agregar condicion de busqueda de localidad si se pasa un valor valido de id de localidad.
        if (IdLocalidad != -1) {
            sentenciaSQL.append(" AND l.id = ?");
        }
        
        //Agregar condicion de busqueda de provincia si se pasa un valor valido de id de provincia.
        if (IdProvincia != -1) {
            sentenciaSQL.append(" AND p.id = ?");
        }

        // Añadir el punto y coma final
        sentenciaSQL.append(";");
        
        

        // Uso de try-with-resources para garantizar el cierre de recursos
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(sentenciaSQL.toString())) {

                int index = 1;
                ps.setString(index++, '%' + calle + '%');
                
                if (IdLocalidad != -1) {
                    ps.setInt(index++, IdLocalidad);
                }

                if (IdProvincia != -1) {
                    ps.setInt(index++, IdProvincia);
                }
                
                //Ejecutar la consulta y obtener el ResultSet dentro de otro bloque try-with-resources
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        DireccionDTO direccionDTO = getDireccionFromResultSet(resultSet); //Crear un objeto DireccionDTO a partir del ResultSet
                        
                        // Si la direccion es válida, agregarla a la lista
                        if (direccionDTO != null) {
                            listaDireccionesDTO.add(direccionDTO);
                        }
                    }
                }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaDireccionesDTO;
    }
    
}
