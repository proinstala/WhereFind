
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
    
}
