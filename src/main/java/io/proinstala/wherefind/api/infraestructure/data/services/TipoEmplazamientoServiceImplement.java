
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.ITipoEmplazamientoService;
import io.proinstala.wherefind.shared.dtos.TipoEmplazamientoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de tipo de emplazamiento que maneja la interacción con la base de datos para obtener información
 * sobre los tipos de emplazamiento. 
 * 
 * Esta clase extiende {@link BaseMySql} 
 *
 * <p>Implementa la interfaz {@link ITipoEmplazamientoService}, que define los métodos necesarios para interactuar con los
 * tipos de emplazamiento en la base de datos. Los métodos de esta clase están enfocados en la obtención de datos y la conversión
 * de los resultados de la base de datos en objetos {@link TipoEmplazamientoDTO}.</p>
 *
 * <p>La clase utiliza SQL nativo y se basa en conexiones JDBC para interactuar con la base de datos MySQL.
 * Maneja las excepciones de SQL y asegura el cierre de recursos mediante el uso de bloques try-with-resources.</p>
 *
 * @see BaseMySql
 * @see ITipoEmplazamientoService
 * @see TipoEmplazamientoDTO
 */
public class TipoEmplazamientoServiceImplement extends BaseMySql implements ITipoEmplazamientoService{

    private static final String SQL_SELECT_COMUN = "SELECT * FROM TIPO_EMPLAZAMIENTO";


    private TipoEmplazamientoDTO getTipoEmplazamientoFromResultSet(ResultSet rs) throws SQLException {
        return TipoEmplazamientoDTO.builder()
                .id(rs.getInt("id"))
                .nombre(rs.getString("nombre"))
                .descripcion(rs.getString("descripcion"))
                .build();
    }

    /**
     * Obtiene una instancia de {@link TipoEmplazamientoDTO} a partir de su identificador único.
     *
     * Este método realiza una consulta a la base de datos para obtener un tipo de emplazamiento
     * específico utilizando el identificador proporcionado. Utiliza una conexión a la base de
     * datos y un {@link PreparedStatement} para ejecutar la consulta SQL definida por
     * {@code SQL_SELECT_BY_ID}. El resultado de la consulta se procesa con un
     * {@link ResultSet} para construir el objeto {@link TipoEmplazamientoDTO} correspondiente.
     *
     * Si no se encuentra ningun tipo con el identificador especificado, el método
     * devuelve {@code null}. El método maneja automáticamente los recursos de conexión y
     * declaración mediante bloques try-with-resources, lo que asegura el cierre adecuado
     * de los recursos incluso en caso de error.
     *
     * @param idTipoEmplazamiento el identificador único del tipo de emplazamiento a buscar.
     * @return una instancia de {@link TipoEmplazamientoDTO} si se encuentra el tipo, o {@code null} si no se encuentra.
     */
    @Override
    public TipoEmplazamientoDTO getTipoEmplazamientoById(int idTipoEmplazamiento) {
        
        String sql = SQL_SELECT_COMUN + " WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTipoEmplazamiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getTipoEmplazamientoFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Busca tipos de emplazamiento en la base de datos cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * <p>Este método utiliza una consulta con la cláusula LIKE para permitir coincidencias parciales.</p>
     *
     * <p>Utiliza un bloque try-with-resources para gestionar la conexión con la base de datos y asegurar
     * el cierre adecuado de los recursos.</p>
     *
     * <p>Si ocurre una excepción SQL durante la ejecución, el método captura la excepción, la imprime en la
     * consola y devuelve {@code null} para indicar un error.</p>
     *
     * @param nombre el nombre o parte del nombre del tipo de emplazamiento a buscar.
     * @return una lista de {@link TipoEmplazamientoDTO} con los tipos que coinciden con el criterio de búsqueda.
     *         Si ocurre un error durante la ejecución, se devuelve {@code null}.
     */
    @Override
    public List<TipoEmplazamientoDTO> findTiposEmplazamiento(String nombre) {
        List<TipoEmplazamientoDTO> listaTipos = new ArrayList<>();

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_COMUN)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoEmplazamientoDTO tipo = getTipoEmplazamientoFromResultSet(rs);
                    if (tipo != null) {
                        listaTipos.add(tipo);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return listaTipos;
    }

    /**
     * Obtiene una lista de todos los tipos de emplazamiento desde la base de datos.
     *
     * <p>Realiza una consulta SQL a la tabla de tipo_emplazamiento y transforma los resultados en
     * una lista de objetos {@link TipoEmplazamientoDTO}. Utiliza un bloque try-with-resources para
     * asegurar el correcto cierre de la conexión, la sentencia preparada y el conjunto de
     * resultados.</p>
     *
     * <p>En caso de que ocurra una excepción durante la ejecución de la consulta, el método
     * captura la excepción y devuelve {@code null} para indicar un error.</p>
     *
     * @return una lista de {@link TipoEmplazamientoDTO} que contiene todos los tipos
     *         obtenidos de la base de datos. Si ocurre un error durante la ejecución,
     *         se devuelve {@code null}.
     */
    @Override
    public List<TipoEmplazamientoDTO> getAllTiposEmplazamiento() {
        List<TipoEmplazamientoDTO> tipos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_COMUN);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipos.add(getTipoEmplazamientoFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return tipos;
    }

}
