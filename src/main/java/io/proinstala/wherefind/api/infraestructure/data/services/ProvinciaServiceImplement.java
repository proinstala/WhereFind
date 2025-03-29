
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementación del servicio de provincias que maneja la interacción con la base de datos para obtener información
 * sobre las provincias. 
 * 
 * Esta clase extiende {@link BaseMySql} 
 *
 * <p>Implementa la interfaz {@link IProvinciaService}, que define los métodos necesarios para interactuar con las
 * provincias en la base de datos. Los métodos de esta clase están enfocados en la obtención de datos y la conversión
 * de los resultados de la base de datos en objetos {@link ProvinciaDTO}.</p>
 *
 * <p>La clase utiliza SQL nativo y se basa en conexiones JDBC para interactuar con la base de datos MySQL.
 * Maneja las excepciones de SQL y asegura el cierre de recursos mediante el uso de bloques try-with-resources.</p>
 *
 * @see BaseMySql
 * @see IProvinciaService
 * @see ProvinciaDTO
 */
public class ProvinciaServiceImplement extends BaseMySql implements IProvinciaService {

    //Obtiene toda la lista de provincias.
    private static final String SQL_SELECT_ALL_PROVINCIAS = "SELECT * FROM PROVINCIA;";
    
    /**
     * Sentencia SQL para obtener una provincia por su identificador.
     */
    private static final String SQL_SELECT_PROVINCIA_BY_ID = "SELECT * FROM PROVINCIA WHERE id = ?;";
    
    /**
     * Sentencia SQL para crear una nueva provincia.
     */
    private static final String SQL_CREATE_PROVINCIA = "INSERT INTO PROVINCIA (nombre) VALUES (?);";
    
    /**
     * Sentencia SQL para actualizar una provincia específica por su identificador.
     */
    private static final String SQL_UPDATE_PROVINCIA = "UPDATE PROVINCIA SET nombre = ? WHERE id = ?;";
    
    /**
     * Sentencia SQL para eliminar una provincia
     */
    private static final String SQL_DELETE_PROVINCIA = "DELETE FROM PROVINCIA WHERE id=?;";
    
    
    
    /**
     * Transforma un objeto {@link ResultSet} en una instancia de {@link ProvinciaDTO}.
     *
     * Este método extrae los valores de las columnas "id" y "nombre" del {@link ResultSet}
     * y los utiliza para crear un nuevo objeto {@link ProvinciaDTO}. Se espera que las columnas
     * estén presentes en el {@link ResultSet} y que tengan tipos de datos compatibles con los
     * métodos utilizados.
     *
     * @param rs el {@link ResultSet} que contiene los datos de la consulta SQL.
     * @return una instancia de {@link ProvinciaDTO} con los datos extraídos del {@link ResultSet}.
     * @throws SQLException si ocurre un error al acceder a los datos del {@link ResultSet}.
     */
    private static ProvinciaDTO getProvinciaFromResultSet(ResultSet rs) throws SQLException {
        
        return new ProvinciaDTO(rs.getInt("id"), rs.getString("nombre"));
    }
    
    /**
     * Obtiene una instancia de {@link ProvinciaDTO} a partir de su identificador único.
     *
     * Este método realiza una consulta a la base de datos para obtener una provincia 
     * específica utilizando el identificador proporcionado. Utiliza una conexión a la base de 
     * datos y un {@link PreparedStatement} para ejecutar la consulta SQL definida por 
     * {@code SQL_SELECT_PROVINCIA_BY_ID}. El resultado de la consulta se procesa con un 
     * {@link ResultSet} para construir el objeto {@link ProvinciaDTO} correspondiente.
     *
     * Si no se encuentra ninguna provincia con el identificador especificado, el método 
     * devuelve {@code null}. El método maneja automáticamente los recursos de conexión y 
     * declaración mediante bloques try-with-resources, lo que asegura el cierre adecuado 
     * de los recursos incluso en caso de error.
     *
     * @param idProvincia el identificador único de la provincia a buscar.
     * @return una instancia de {@link ProvinciaDTO} si se encuentra la provincia, o {@code null} si no se encuentra.
     */
    @Override
    public ProvinciaDTO getProvinciaById(int idProvincia) {
        ProvinciaDTO provinciaDTO = null;
        
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_PROVINCIA_BY_ID)) {
            
            ps.setInt(1, idProvincia);
            
            //Ejecutar la consulta y obtener el ResultSet dentro de otro bloque try-with-resources
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    provinciaDTO = getProvinciaFromResultSet(resultSet); //Crear un objeto DireccionDTO a partir del ResultSet
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return provinciaDTO;
    }
    
    /**
     * Obtiene una lista de todas las provincias desde la base de datos.
     *
     * <p>Realiza una consulta SQL a la tabla de provincias y transforma los resultados en
     * una lista de objetos {@link ProvinciaDTO}. Utiliza un bloque try-with-resources para
     * asegurar el correcto cierre de la conexión, la sentencia preparada y el conjunto de
     * resultados.</p>
     *
     * <p>En caso de que ocurra una excepción durante la ejecución de la consulta, el método
     * captura la excepción y devuelve {@code null} para indicar un error.</p>
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
    
    /**
     * Busca provincias en la base de datos cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * <p>Este método construye dinámicamente una consulta SQL para buscar provincias cuyo nombre contenga
     * el texto proporcionado. Utiliza una consulta con la cláusula LIKE para permitir coincidencias parciales.</p>
     *
     * <p>Utiliza un bloque try-with-resources para gestionar la conexión con la base de datos y asegurar
     * el cierre adecuado de los recursos.</p>
     *
     * <p>Si ocurre una excepción SQL durante la ejecución, el método captura la excepción, la imprime en la
     * consola y devuelve {@code null} para indicar un error.</p>
     *
     * @param nombre el nombre o parte del nombre de la provincia a buscar.
     * @return una lista de {@link ProvinciaDTO} con las provincias que coinciden con el criterio de búsqueda.
     *         Si ocurre un error durante la ejecución, se devuelve {@code null}.
     */
    @Override
    public List<ProvinciaDTO> findProvincias(String nombre) {
        List<ProvinciaDTO> listaProvinciasDTO = new ArrayList<>();
        
        StringBuilder sentenciaSQL = new StringBuilder(SQL_SELECT_ALL_PROVINCIAS);
        
        //Eliminar el punto y coma al final de SQL_SELECT_DIRECCIONES
        if (sentenciaSQL.charAt(sentenciaSQL.length() - 1) == ';') {
            sentenciaSQL.deleteCharAt(sentenciaSQL.length() - 1);
        }
        
        //Agrega la cláusula WHERE.
        sentenciaSQL.append(" WHERE");
        
        //Agrega la condicion de busqueda por nombre de provincia
        sentenciaSQL.append(" nombre LIKE ?");
        
        //Añade el punto y coma final
        sentenciaSQL.append(";");
        
        try (Connection conexion = getConnection(); 
                PreparedStatement ps = conexion.prepareStatement(sentenciaSQL.toString())) {

            int index = 1;
            ps.setString(index++, '%' + nombre + '%');
            
            try (ResultSet resulSet = ps.executeQuery()) {
                while (resulSet.next()) {
                    ProvinciaDTO provinciaDTO = getProvinciaFromResultSet(resulSet);
                    
                    // Si la provincia es valida, se agrega a la lista.
                    if (provinciaDTO != null) {
                        listaProvinciasDTO.add(provinciaDTO);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaProvinciasDTO;
    }
    
    
    /**
     * Crea una nueva provincia en la base de datos.
     *
     * <p>Este método inserta una nueva provincia en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link ProvinciaDTO}. La inserción se realiza mediante una 
     * declaración preparada, y el ID generado automáticamente por la base de datos se recupera 
     * y se asigna al objeto {@link ProvinciaDTO}. Si la inserción es exitosa, el objeto 
     * {@link ProvinciaDTO} se devuelve con el ID asignado. En caso de error, se captura la 
     * excepción y se devuelve {@code null}.</p>
     *
     * <p>El método utiliza un bloque try-with-resources para asegurar el cierre automático de 
     * los recursos de conexión y declaración, incluso en caso de error.</p>
     *
     * @param provinciaDTO el objeto {@link ProvinciaDTO} que contiene los datos de la provincia 
     *                     a insertar. El objeto debe tener el valor de nombre de la provincia.
     * @return el objeto {@link ProvinciaDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    @Override
    public ProvinciaDTO createProvincia(ProvinciaDTO provinciaDTO) {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(
                SQL_CREATE_PROVINCIA,
                Statement.RETURN_GENERATED_KEYS)) {

            // Configura los parámetros del PreparedStatement
            ps.setString(1, provinciaDTO.getNombre());

            // Ejecuta la inserción
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Obtiene el ID generado
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        provinciaDTO.setId(generatedId); // Asigna el ID generado al objeto
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Manejo de errores
        }

        return provinciaDTO; // Devuelve el objeto con el ID asignado
    }
    
    /**
     * Actualiza la información de una provincia en la base de datos.
     *
     * Este método actualiza los datos de una provincia existente en la base de datos 
     * utilizando los valores proporcionados en el objeto {@link ProvinciaDTO}. Se establece 
     * la conexión con la base de datos y se ejecuta una declaración preparada para actualizar 
     * la provincia. Utiliza un bloque try-with-resources para el cierre automático de los 
     * recursos, garantizando una gestión adecuada de la conexión y del PreparedStatement.
     *
     * Devuelve {@code true} si la actualización fue exitosa (al menos una fila afectada), 
     * o {@code false} si no se realizó ninguna actualización o si ocurrió un error.
     *
     * @param provinciaDTO el objeto {@link ProvinciaDTO} que contiene los datos actualizados de la provincia.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    @Override
    public boolean updateProvincia(ProvinciaDTO provinciaDTO) {
        
        // Verificar que el objeto de entrada no sea nulo
        if (provinciaDTO == null) {
            return false;
        }
        
        // Contador de filas afectadas
        int rowsAffected = 0;
        
        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_PROVINCIA)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setString(1, provinciaDTO.getNombre());
            ps.setInt(2, provinciaDTO.getId());
            
            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Retornar true si se ha hecho en update
        return rowsAffected > 0;
    }
    
    /**
     * Elimina una provincia de la base de datos.
     * 
     * <p>Este método elimina una provincia específica de la base de datos.
     * La operación se realiza utilizando una declaración preparada para ejecutar
     * la sentencia SQL definida por {@code SQL_DELETE_DIRECCION}.</p>
     *
     * <p>El método utiliza un bloque try-with-resources para gestionar la conexión y el 
     * {@link PreparedStatement}, asegurando el cierre adecuado de los recursos, incluso 
     * en caso de error.</p>
     *
     * @param provinciaId el identificador de la provincia que se desea eliminar.
     * @return {@code true} si la provincia fue eliminada exitosamente,
     *         {@code false} si no se realizó ninguna modificación o si ocurrió un error.
     */
    @Override
    public boolean deleteProvincia(int provinciaId) {
        
        // Contador de filas afectadas
        int rowsAffected = 0;
        
        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE_PROVINCIA)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setInt(1, provinciaId);
            
            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Retornar true si se ha hecho el delete
        return rowsAffected > 0;
    }
    
}
