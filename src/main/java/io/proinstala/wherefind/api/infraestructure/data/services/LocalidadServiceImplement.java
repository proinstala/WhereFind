package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    /**
     * Sentencia SQL para obtener una dirección por su identificador.
     */
    private static final String SQL_SELECT_LOCALIDAD_BY_ID = "SELECT l.*, p.* FROM LOCALIDAD l INNER JOIN PROVINCIA p ON(l.provincia_id = p.id) WHERE l.id = ?;";
    

    //Obtiene todas la lista de provincias.
    private static final String SQL_SELECT_ALL_LOCALIDADES = "SELECT l.*, p.* FROM LOCALIDAD l inner join PROVINCIA p ON(l.provincia_id = p.id)";
    
    //Obtiene todas las localidades de una provincia
    private static final String SQL_SELECT_LOCALIDADES_PROVINCIA = "SELECT l.*, p.* FROM LOCALIDAD l inner join PROVINCIA p ON(l.provincia_id = p.id) WHERE l.provincia_id = ?";

    /**
     * Sentencia SQL para eliminar una localidad
     */
    private static final String SQL_DELETE_LOCALIDAD = "DELETE FROM LOCALIDAD WHERE id=?;";
    
    /**
     * Sentencia SQL para crear una nueva dirección.
     */
    private static final String SQL_CREATE_LOCALIDAD = "INSERT INTO LOCALIDAD (nombre, provincia_id) VALUES (?,?);";
    
    /**
     * Sentencia SQL para actualizar una localidad específica por su identificador.
     */
    private static final String SQL_UPDATE_LOCALIDAD = "UPDATE LOCALIDAD SET nombre = ?, provincia_id = ? WHERE id = ?;";
    
    
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
     * Obtiene una instancia de {@link LocalidadDTO} a partir de su identificador único.
     *
     * Este método realiza una consulta a la base de datos para obtener una localidad 
     * específica utilizando el identificador proporcionado. Utiliza una conexión a la base de 
     * datos y un {@link PreparedStatement} para ejecutar la consulta SQL definida por 
     * {@code SQL_SELECT_LOCALIDAD_BY_ID}. El resultado de la consulta se procesa con un 
     * {@link ResultSet} para construir el objeto {@link LocalidadDTO} correspondiente.
     *
     * Si no se encuentra ninguna localidad con el identificador especificado, el método 
     * devuelve {@code null}. El método maneja automáticamente los recursos de conexión y 
     * declaración mediante bloques try-with-resources, lo que asegura el cierre adecuado 
     * de los recursos incluso en caso de error.
     *
     * @param idLocalidad el identificador único de la localidad a buscar.
     * @return una instancia de {@link LocalidadDTO} si se encuentra la localidad, o {@code null} si no se encuentra.
     */
    @Override
    public LocalidadDTO getLocalidadById(int idLocalidad) {
        LocalidadDTO localidadDTO = null;
        
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_LOCALIDAD_BY_ID)) {
            
            ps.setInt(1, idLocalidad);
            
            //Ejecutar la consulta y obtener el ResultSet dentro de otro bloque try-with-resources
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    localidadDTO = getLocalidadFromResultSet(resultSet); //Crear un objeto LocalidadDTO a partir del ResultSet
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localidadDTO;
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
    
    /**
     * Busca localidades en la base de datos cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * <p>Este método construye dinámicamente una consulta SQL para buscar localidades cuyo nombre contenga
     * el texto proporcionado. Utiliza una consulta con la cláusula LIKE para permitir coincidencias parciales.</p>
     *
     * <p>Utiliza un bloque try-with-resources para gestionar la conexión con la base de datos y asegurar
     * el cierre adecuado de los recursos.</p>
     *
     * <p>Si ocurre una excepción SQL durante la ejecución, el método captura la excepción, la imprime en la
     * consola y devuelve {@code null} para indicar un error.</p>
     *
     * @param nombre el nombre o parte del nombre de la localidad a buscar.
     * @return una lista de {@link LocalidadDTO} con las localidades que coinciden con el criterio de búsqueda.
     *         Si ocurre un error durante la ejecución, se devuelve {@code null}.
     */
    @Override
    public List<LocalidadDTO> findLocalidades(String nombre, int idProvincia) {
        List<LocalidadDTO> listaLocalidadesDTO = new ArrayList<>();
        
        StringBuilder sentenciaSQL = new StringBuilder(SQL_SELECT_ALL_LOCALIDADES);
        
        //Eliminar el punto y coma al final de SQL_SELECT_DIRECCIONES
        if (sentenciaSQL.charAt(sentenciaSQL.length() - 1) == ';') {
            sentenciaSQL.deleteCharAt(sentenciaSQL.length() - 1);
        }
        
        //Agrega la cláusula WHERE.
        sentenciaSQL.append(" WHERE");
        
        //Agrega la condicion de busqueda por nombre de provincia
        sentenciaSQL.append(" l.nombre LIKE ?");
        
        //Agregar condicion de busqueda de provincia si se pasa un valor valido de id de provincia.
        if (idProvincia != -1) {
            sentenciaSQL.append(" AND p.id = ?");
        }
        
        //Añade el punto y coma final
        sentenciaSQL.append(";");
        
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(sentenciaSQL.toString())) {

            int index = 1;
            ps.setString(index++, '%' + nombre + '%');
            if(idProvincia != -1) {
                ps.setInt(index++, idProvincia);
            }
            
            
            try (ResultSet resulSet = ps.executeQuery()) {
                while (resulSet.next()) {
                    LocalidadDTO localidadDTO = getLocalidadFromResultSet(resulSet);
                    
                    // Si la provincia es valida, se agrega a la lista.
                    if (localidadDTO != null) {
                        listaLocalidadesDTO.add(localidadDTO);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaLocalidadesDTO;
    }

    /**
     * Crea una nueva localidad en la base de datos.
     *
     * <p>Este método inserta un nuevo registro en la tabla LOCALIDAD utilizando los datos
     * proporcionados en el objeto {@link LocalidadDTO}. Si la inserción es exitosa, se recupera 
     * y se asigna el ID generado al objeto {@link LocalidadDTO} y se devuelve.</p>
     *
     * @param localidadDTO El objeto {@link LocalidadDTO} que contiene la información de la localidad
     *                     a crear. Debe tener el nombre y la provincia válidos.
     * @return El objeto {@link LocalidadDTO} con el ID generado si se crea correctamente,
     *         o {@code null} si ocurre un error durante la operación.
     */
    @Override
    public LocalidadDTO createLocalidad(LocalidadDTO localidadDTO) {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(
                SQL_CREATE_LOCALIDAD,
                Statement.RETURN_GENERATED_KEYS)) {

            // Configura los parámetros del PreparedStatement
            ps.setString(1, localidadDTO.getNombre());
            ps.setInt(2, localidadDTO.getProvincia().getId());

            // Ejecuta la inserción
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Obtiene el ID generado
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        localidadDTO.setId(generatedId); // Asigna el ID generado al objeto
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Manejo de errores
        }

        return localidadDTO; // Devuelve el objeto con el ID asignado
    }

    /**
     * Elimina una localidad de la base de datos utilizando su ID.
     *
     * <p>Este método ejecuta una sentencia SQL para eliminar la localidad correspondiente 
     * al ID proporcionado. Retorna {@code true} si la eliminación fue exitosa.</p>
     *
     * @param localidadId El ID de la localidad a eliminar.
     * @return {@code true} si la localidad fue eliminada correctamente, {@code false} en caso contrario.
     */
    @Override
    public boolean deleteLocalidad(int localidadId) {
        // Contador de filas afectadas
        int rowsAffected = 0;
        
        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE_LOCALIDAD)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setInt(1, localidadId);
            
            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Retornar true si se ha hecho en update
        return rowsAffected > 0;
    }

    @Override
    public boolean updateLocalidad(LocalidadDTO localidadDTO) {
        // Verificar que el objeto de entrada no sea nulo
        if (localidadDTO == null) {
            return false;
        }
        
        // Contador de filas afectadas
        int rowsAffected = 0;
        
        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_LOCALIDAD)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setString(1, localidadDTO.getNombre());
            ps.setInt(2, localidadDTO.getProvincia().getId());
            ps.setInt(3, localidadDTO.getId());
            
            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Retornar true si se ha hecho en update
        return rowsAffected > 0;
    }

}
