
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IPuestoTrabajoService;
import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de puesto trabajo que maneja la interacción con la base de datos para obtener información
 * sobre los puestos de trabajo. 
 * 
 * Esta clase extiende {@link BaseMySql} 
 *
 * <p>Implementa la interfaz {@link IPuestoTrabajoService}, que define los métodos necesarios para interactuar con los
 * puestos de trabajo en la base de datos. Los métodos de esta clase están enfocados en la obtención de datos y la conversión
 * de los resultados de la base de datos en objetos {@link PuestoTrabajoDTO}.</p>
 *
 * <p>La clase utiliza SQL nativo y se basa en conexiones JDBC para interactuar con la base de datos MySQL.
 * Maneja las excepciones de SQL y asegura el cierre de recursos mediante el uso de bloques try-with-resources.</p>
 *
 * @see BaseMySql
 * @see IPuestoTrabajoService
 * @see PuestoTrabajoDTO
 */
public class PuestoTrabajoServiceImplement extends BaseMySql implements IPuestoTrabajoService {
    
    private static final String SQL_SELECT_ALL = "SELECT * FROM PUESTO_TRABAJO";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM PUESTO_TRABAJO WHERE id = ?";

    private PuestoTrabajoDTO getPuestoFromResultSet(ResultSet rs) throws SQLException {
        return PuestoTrabajoDTO.builder()
                .id(rs.getInt("id"))
                .nombre(rs.getString("nombre"))
                .build();
    }

    /**
     * Obtiene una instancia de {@link PuestoTrabajoDTO} a partir de su identificador único.
     *
     * Este método realiza una consulta a la base de datos para obtener un puesto de trabajo
     * específico utilizando el identificador proporcionado. Utiliza una conexión a la base de 
     * datos y un {@link PreparedStatement} para ejecutar la consulta SQL definida por 
     * {@code SQL_SELECT_BY_ID}. El resultado de la consulta se procesa con un 
     * {@link ResultSet} para construir el objeto {@link PuestoTrabajoDTO} correspondiente.
     *
     * Si no se encuentra ningun puesto con el identificador especificado, el método 
     * devuelve {@code null}. El método maneja automáticamente los recursos de conexión y 
     * declaración mediante bloques try-with-resources, lo que asegura el cierre adecuado 
     * de los recursos incluso en caso de error.
     *
     * @param idPuesto el identificador único del puesto a buscar.
     * @return una instancia de {@link PuestoTrabajoDTO} si se encuentra el puesto, o {@code null} si no se encuentra.
     */
    @Override
    public PuestoTrabajoDTO getPuestoById(int idPuesto) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idPuesto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getPuestoFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * Busca puestos de trabajo en la base de datos cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * <p>Este método construye dinámicamente una consulta SQL para buscar puestos cuyo nombre contenga
     * el texto proporcionado. Utiliza una consulta con la cláusula LIKE para permitir coincidencias parciales.</p>
     *
     * <p>Utiliza un bloque try-with-resources para gestionar la conexión con la base de datos y asegurar
     * el cierre adecuado de los recursos.</p>
     *
     * <p>Si ocurre una excepción SQL durante la ejecución, el método captura la excepción, la imprime en la
     * consola y devuelve {@code null} para indicar un error.</p>
     *
     * @param nombre el nombre o parte del nombre del puesto a buscar.
     * @return una lista de {@link PuestoTrabajoDTO} con los puestos que coinciden con el criterio de búsqueda.
     *         Si ocurre un error durante la ejecución, se devuelve {@code null}.
     */
    @Override
    public List<PuestoTrabajoDTO> findPuestos(String nombre) {
        List<PuestoTrabajoDTO> listaPuestoTrabajoDTO = new ArrayList<>();
        
        StringBuilder sentenciaSQL = new StringBuilder(SQL_SELECT_ALL);
        
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
                    PuestoTrabajoDTO puestoTrabajoDTO = getPuestoFromResultSet(resulSet);
                    
                    // Si la provincia es valida, se agrega a la lista.
                    if (puestoTrabajoDTO != null) {
                        listaPuestoTrabajoDTO.add(puestoTrabajoDTO);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //Devolver null para indicar un error
        }

        return listaPuestoTrabajoDTO;
    }
    

    /**
     * Obtiene una lista de todas los puesto de trabajo desde la base de datos.
     *
     * <p>Realiza una consulta SQL a la tabla de puesto_trabajo transforma los resultados en
     * una lista de objetos {@link PuestoTrabajoDTO}. Utiliza un bloque try-with-resources para
     * asegurar el correcto cierre de la conexión, la sentencia preparada y el conjunto de
     * resultados.</p>
     *
     * <p>En caso de que ocurra una excepción durante la ejecución de la consulta, el método
     * captura la excepción y devuelve {@code null} para indicar un error.</p>
     *
     * @return una lista de {@link PuestoTrabajoDTO} que contiene todas los puestos
     *         obtenidos de la base de datos. Si ocurre un error durante la ejecución,
     *         se devuelve {@code null}.
     */
    @Override
    public List<PuestoTrabajoDTO> getAllPuestos() {
        List<PuestoTrabajoDTO> puestos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                puestos.add(getPuestoFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puestos;
    }
}
