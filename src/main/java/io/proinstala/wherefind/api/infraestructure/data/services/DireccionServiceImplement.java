
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

/**
 * Implementación del servicio para la gestión de direcciones en la base de datos.
 * 
 * <p>Esta clase implementa la interfaz {@link IDireccionService} y proporciona métodos para
 * obtener, buscar y actualizar direcciones en la base de datos. Utiliza sentencias SQL predefinidas
 * para realizar las operaciones de lectura y escritura sobre la base de datos.</p>
 * 
 * <p>Extiende {@link BaseMySql} para proporcionar la funcionalidad de conexión con MySQL.</p>
 */
public class DireccionServiceImplement extends BaseMySql implements IDireccionService {

    /**
     * Consulta SQL para obtener una dirección por su identificador.
     */
    private static final String SQL_SELECT_DIRECCION_BY_ID = "SELECT d.*, l.*, p.* FROM DIRECCION d INNER JOIN LOCALIDAD l ON(d.localidad_id = l.id) INNER JOIN PROVINCIA p ON(l.provincia_id = p.id) WHERE d.id = ?;";
    
    /**
     * Consulta SQL para obtener todas las direcciones.
     */
    private static final String SQL_SELECT_DIRECCIONES = "SELECT d.*, l.*, p.* FROM DIRECCION d INNER JOIN LOCALIDAD l ON(d.localidad_id = l.id) INNER JOIN PROVINCIA p ON(l.provincia_id = p.id);"; 
    
    /**
     * Consulta SQL para actualizar una dirección específica por su identificador.
     */
    private static final String SQL_UPDATE_DIRECCION = "UPDATE DIRECCION SET calle = ?, numero = ?, codigo_postal = ?, localidad_id = ? WHERE id = ?;";
    
    
    /**
     * Transforma un objeto {@link ResultSet} en una instancia de {@link DireccionDTO}.
     *
     * <p>Este método extrae los valores de las columnas asociadas a una dirección, incluyendo
     * la calle, número, código postal, localidad, provincia y el estado activo, del {@link ResultSet}.
     * Además, construye objetos anidados de {@link LocalidadDTO} y {@link ProvinciaDTO} 
     * para representar la jerarquía de datos complejos.</p>
     *
     * @param rs el {@link ResultSet} que contiene los datos de la consulta SQL.
     * @return una instancia de {@link DireccionDTO} con los datos extraídos del {@link ResultSet}.
     * @throws SQLException si ocurre un error al acceder a los datos del {@link ResultSet}.
     * @throws IllegalArgumentException si el {@link ResultSet} es nulo.
     */
    private static DireccionDTO getDireccionFromResultSet(ResultSet rs) throws SQLException {
       
        return new DireccionDTO(
                rs.getInt("d.id"),
                rs.getString("d.calle"),
                (rs.getString("d.numero") != null) ? rs.getString("d.numero") : "",
                (Integer)rs.getObject("d.codigo_postal"),
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
    
    /**
     * Obtiene una instancia de {@link DireccionDTO} a partir de su identificador único.
     *
     * Este método realiza una consulta a la base de datos para obtener una dirección 
     * específica utilizando el identificador proporcionado. Utiliza una conexión a la base de 
     * datos y un {@link PreparedStatement} para ejecutar la consulta SQL definida por 
     * {@code SQL_SELECT_DIRECCION_BY_ID}. El resultado de la consulta se procesa con un 
     * {@link ResultSet} para construir el objeto {@link DireccionDTO} correspondiente.
     *
     * Si no se encuentra ninguna dirección con el identificador especificado, el método 
     * devuelve {@code null}. El método maneja automáticamente los recursos de conexión y 
     * declaración mediante bloques try-with-resources, lo que asegura el cierre adecuado 
     * de los recursos incluso en caso de error.
     *
     * @param idDireccion el identificador único de la dirección a buscar.
     * @return una instancia de {@link DireccionDTO} si se encuentra la dirección, o {@code null} si no se encuentra.
     */
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

    
    /**
     * Busca y devuelve una lista de direcciones que coincidan con los criterios
     * de búsqueda especificados.
     *
     * Este método realiza una consulta a la base de datos para obtener
     * direcciones que coincidan con el nombre de la calle proporcionado, el
     * identificador de localidad y el identificador de provincia, si son
     * válidos. Construye dinámicamente la consulta SQL utilizando un
     * {@link StringBuilder} y agrega condiciones de búsqueda según los
     * parámetros proporcionados.
     *
     * La consulta utiliza LIKE para buscar coincidencias parciales en el nombre
     * de la calle, y utiliza los identificadores de localidad y provincia si se
     * proporcionan valores válidos (distintos de -1). Los recursos de conexión,
     * declaración y resultado se manejan mediante bloques try-with-resources
     * para asegurar el cierre adecuado.
     *
     * Si ocurre una excepción SQL, esta se captura y se imprime la traza de la
     * pila. En caso de error, el método devuelve {@code null} para indicar que
     * no se pudo completar la operación.
     *
     * @param calle el nombre (o parte del nombre) de la calle a buscar. Se
     * utiliza LIKE para coincidencias parciales.
     * @param IdLocalidad el identificador de la localidad para filtrar las
     * direcciones, o -1 si no se debe filtrar por localidad.
     * @param IdProvincia el identificador de la provincia para filtrar las
     * direcciones, o -1 si no se debe filtrar por provincia.
     * @return una lista de {@link DireccionDTO} que cumplen con los criterios
     * de búsqueda, o {@code null} si ocurre un error.
     */
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

    
    /**
     * Actualiza la información de una dirección en la base de datos.
     *
     * Este método actualiza los datos de una dirección existente en la base de datos 
     * utilizando los valores proporcionados en el objeto {@link DireccionDTO}. Se establece 
     * la conexión con la base de datos y se ejecuta una declaración preparada para actualizar 
     * la dirección. Utiliza un bloque try-with-resources para el cierre automático de los 
     * recursos, garantizando una gestión adecuada de la conexión y del PreparedStatement.
     *
     * Devuelve {@code true} si la actualización fue exitosa (al menos una fila afectada), 
     * o {@code false} si no se realizó ninguna actualización o si ocurrió un error.
     *
     * @param direccionDTO el objeto {@link DireccionDTO} que contiene los datos actualizados de la dirección.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    @Override
    public boolean updateDireccion(DireccionDTO direccionDTO) {
        
        // Verificar que el objeto de entrada no sea nulo
        if (direccionDTO == null) {
            return false;
        }
        
        // Contador de filas afectadas
        int rowsAffected = 0;
        
        // Usar try-with-resources para asegurar el cierre automático de recursos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_DIRECCION)) {

            // Establecer los parámetros en el PreparedStatement
            ps.setString(1, direccionDTO.getCalle());
            ps.setString(2, direccionDTO.getNumero());
            ps.setObject(3, direccionDTO.getCodigoPostal());
            ps.setInt(4, direccionDTO.getLocalidad().getId());
            ps.setInt(5, direccionDTO.getId());
            
            // Ejecutar la actualización y obtener el número de filas afectadas
            rowsAffected = ps.executeUpdate();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Retornar true si se ha hecho en update
        return rowsAffected > 0;
    }
    
}
