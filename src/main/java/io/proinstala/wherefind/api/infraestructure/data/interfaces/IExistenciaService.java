
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ExistenciaDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar las existencias.
 */
public interface IExistenciaService {
    
    /**
     * Obtiene una existencia específica por su identificador.
     *
     * Este método devuelve un objeto {@link ExistenciaDTO} que representa la existencia
     * con el identificador proporcionado. Si no se encuentra ninguna existencia con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ninguna existencia 
     * registrada.
     *
     * @param idExistencia el identificador único de la existencia a obtener.
     * @return el objeto {@link ExistenciaDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ninguna existencia con ese ID.
     */
    public ExistenciaDTO getExistenciaById(int idExistencia);
    
    /**
     * Obtiene una lista de todas las existencias disponibles.
     *
     * Este método devuelve una lista de objetos {@link ExistenciaDTO} que representan todas 
     * las existencias registradas en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * existencias disponibles.
     *
     * @return una lista de {@link ExistenciaDTO} con todas las existencias, o una lista vacía si no hay registros.
     */
    public List<ExistenciaDTO> getAllExistencias();
    
    /**
     * Busca existencias que coincidan con los parámetros dados.
     *
     * Este método devuelve una lista de objetos {@link ExistenciaDTO} que cumplen con los criterios
     * de búsqueda especificados.  
     *
     * <p>Reglas de filtrado:</p>
     * <ul>
     *   <li><b>marcaId</b>, <b>almacenId</b> y <b>tipoEmlazamientoId</b>: si reciben <code>-1</code>, no se aplicará filtro por ese campo.</li>
     *   <li><b>disponibilidad</b>: 
     *       <ul>
     *         <li><code>-1</code>: no filtra por disponibilidad.</li>
     *         <li><code>0</code>: solo existencias no disponibles.</li>
     *         <li><code>1</code>: solo existencias disponibles.</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * @param nombreArticulo el nombre o parte del nombre del artículo asociado a la existencia.
     * @param referenciaArticulo la referencia o parte de la referencia del artículo.
     * @param marcaId el id de la marca del artículo asociado; <code>-1</code> para no filtrar.
     * @param almacenId el id del almacén; <code>-1</code> para no filtrar.
     * @param nombreEmplazamiento el nombre o parte del nombre del emplazamiento; <code>-1</code> para no filtrar.
     * @param disponibilidad criterio de disponibilidad: <code>-1</code>, <code>0</code> o <code>1</code>.
     * @return una lista de {@link ExistenciaDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<ExistenciaDTO> findExistencias(String nombreArticulo, String referenciaArticulo, int marcaId, int almacenId, String nombreEmplazamiento, int disponibilidad);
    
    /**
     * Crea una nueva existencia en la base de datos.
     *
     * Este método inserta una nueva existencia en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link ExistenciaDTO}. Si la inserción es exitosa, el 
     * objeto {@link ExistenciaDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * @param existenciaDTO el objeto {@link ExistenciaDTO} que contiene los datos de la existencia a insertar.
     * @return el objeto {@link ExistenciaDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public ExistenciaDTO createExistencia(ExistenciaDTO existenciaDTO);
    
    /**
     * Actualiza la información de una existencia en la base de datos.
     *
     * Este método actualiza los datos de una existencia existente utilizando los valores 
     * proporcionados en el objeto {@link ExistenciaDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param existenciaDTO el objeto {@link ExistenciaDTO} que contiene los datos actualizados de la existencia.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateExistencia(ExistenciaDTO existenciaDTO);
    
    /**
     * Elimina una existencia de la base de datos.
     *
     * Este método elimina una existencia específica de la base de datos,
     * devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param existenciaId el identificador único de la existencia a eliminar.
     * @return {@code true} si la existencia fue eliminada con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteExistencia(int existenciaId);
    
}
