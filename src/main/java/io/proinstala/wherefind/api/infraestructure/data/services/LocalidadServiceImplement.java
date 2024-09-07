package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de acceso a datos para localidades.
 * 
 * <p>Esta clase implementa la interfaz {@link ILocalidadService} y proporciona métodos para 
 * realizar operaciones de acceso a datos relacionadas con las localidades en la base de datos.</p>
 * 
 * <p>Utiliza consultas SQL para recuperar datos de la base de datos y transforma los resultados 
 * en objetos {@link LocalidadDTO} y {@link ProvinciaDTO}. La clase maneja las conexiones 
 * a la base de datos, las declaraciones preparadas y el procesamiento de resultados.</p>
 * 
 */
public class LocalidadServiceImplement extends BaseMySql implements ILocalidadService {

    //Obtiene todas la lista de provincias.
    private static final String SQL_SELECT_ALL_LOCALIDADES = "SELECT l.*, p.* FROM LOCALIDAD l inner join PROVINCIA p ON(l.provincia_id = p.id)";
    
    //Obtiene todas las localidades de una provincia
    private static final String SQL_SELECT_LOCALIDADES_PROVINCIA = "SELECT l.*, p.* FROM LOCALIDAD l inner join PROVINCIA p ON(l.provincia_id = p.id) WHERE l.provincia_id = ?";

    /**
    * Crea y devuelve un objeto LocalidadDTO a partir de un ResultSet.
    * 
    * Este método extrae los datos necesarios de un ResultSet que contiene
    * información de una localidad y su provincia asociada, y construye un objeto
    * LocalidadDTO. Se espera que el ResultSet esté posicionado en una fila válida
    * antes de llamar a este método.
    * 
    * @param rs El ResultSet desde el cual se extraen los datos. Debe contener las
    *           columnas "l.id" y "l.nombre" para la localidad, y "p.id" y "p.nombre"
    *           para la provincia.
    * @return Un objeto LocalidadDTO que representa la localidad y su provincia asociada.
    * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
    */
    private static LocalidadDTO getLocalidadFromResultSet(ResultSet rs) throws SQLException {
       
        return new LocalidadDTO(
                rs.getInt("l.id"),
                rs.getString("l.nombre"),
                new ProvinciaDTO(rs.getInt("p.id"), rs.getString("p.nombre")));
    }
    
    /**
     * Obtiene una lista de todas las localidades desde la base de datos.
     *
     * <p>Este método ejecuta una consulta SQL para recuperar todas las localidades 
     * y sus provincias asociadas. Los resultados se transforman en una lista de objetos 
     * {@link LocalidadDTO}.</p>
     *
     * @return Una lista de {@link LocalidadDTO} que contiene todas las localidades obtenidas
     *         de la base de datos. Si ocurre un error durante la ejecución, se devuelve {@code null}.
     */
    @Override
    public List<LocalidadDTO> getAllLocalidades() {
        List<LocalidadDTO> listaLocalidadDTO = new ArrayList<>();

        // Uso de try-with-resources para garantizar el cierre de recursos
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ALL_LOCALIDADES); 
                ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                LocalidadDTO localidadDTO = getLocalidadFromResultSet(resultSet);
                if (localidadDTO != null) {
                    listaLocalidadDTO.add(localidadDTO);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaLocalidadDTO;
    }
    
    /**
     * Obtiene una lista de localidades que pertenecen a una provincia específica.
     *
     * <p>Este método ejecuta una consulta SQL utilizando el ID de la provincia para recuperar
     * todas las localidades asociadas a dicha provincia. Los resultados se transforman en 
     * una lista de objetos {@link LocalidadDTO}.</p>
     *
     * @param provincia El objeto {@link ProvinciaDTO} que contiene la información de la provincia,
     *                  específicamente el ID de la provincia que se utilizará para buscar las localidades.
     * @return Una lista de objetos {@link LocalidadDTO} que representan las localidades de la provincia.
     *         Devuelve {@code null} si ocurre un error durante la operación.
     */
    @Override
    public List<LocalidadDTO> getLocalidadesOfProvincia(ProvinciaDTO provincia) {
        List<LocalidadDTO> listaLocalidadDTO = new ArrayList<>();

        // Uso de try-with-resources para garantizar el cierre de recursos
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_LOCALIDADES_PROVINCIA)) {

                ps.setInt(1, provincia.getId());
                
                //Ejecutar la consulta y obtener el ResultSet dentro de otro bloque try-with-resources
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        LocalidadDTO localidadDTO = getLocalidadFromResultSet(resultSet); //Crear un objeto LocalidadDTO a partir del ResultSet
                        
                        // Si la localidad es válida, agregarla a la lista
                        if (localidadDTO != null) {
                            listaLocalidadDTO.add(localidadDTO);
                        }
                    }
                }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaLocalidadDTO;
    }

}
