
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class ProvinciaServiceImplement extends BaseMySql implements IProvinciaService {

    //Obtiene toda la lista de provincias.
    private static final String SQL_SELECT_ALL_PROVINCIAS = "SELECT * FROM PROVINCIA";
    
    private static ProvinciaDTO getProvinciaFromResultSet(ResultSet rs) {
        try {
            return new ProvinciaDTO(rs.getInt("id"), rs.getString("nombre"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //Devuelve nulo en caso de error;
        return null;
    }
    
    /**
     * Obtiene una lista de todas las provincias desde la base de datos.
     *
     * @return una lista de {@link ProvinciaDTO} que contiene todas las provincias
     *         obtenidas de la base de datos. Si ocurre un error durante la ejecución,
     *         se devuelve {@code null}.
     */
    @Override
    public List<ProvinciaDTO> getAllProvincias() {
        List<ProvinciaDTO> listaProvinciaDTO = new ArrayList<>();

        // Uso de try-with-resources para garantizar el cierre de recursos
        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ALL_PROVINCIAS);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                ProvinciaDTO provinciaDTO = getProvinciaFromResultSet(resultSet);
                if (provinciaDTO != null) {
                    listaProvinciaDTO.add(provinciaDTO);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaProvinciaDTO;
    }
    
}
