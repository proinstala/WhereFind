
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.AlmacenDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar los almacenes.
 */
public interface IAlmacenService {
    
    /**
     * Obtiene un almacén específico por su identificador.
     *
     * Este método devuelve un objeto {@link AlmacenDTO} que representa el almacén
     * con el identificador proporcionado. Si no se encuentra ningún almacén con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ningún almacén 
     * existente.
     *
     * @param idAlmacen el identificador único del almacén a obtener.
     * @return el objeto {@link AlmacenDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ningún almacén con ese ID.
     */
    public AlmacenDTO getAlmacenById(int idAlmacen);
    
    /**
     * Obtiene una lista de todos los almacenes disponibles.
     *
     * Este método devuelve una lista de objetos {@link AlmacenDTO} que representan todos 
     * los almacenes registrados en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * almacenes disponibles.
     *
     * @return una lista de {@link AlmacenDTO} con todos los almacenes, o una lista vacía si no hay almacenes registrados.
     */
    public List<AlmacenDTO> getAllAlmacenes();
    
    /**
     * Busca almacenes cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link AlmacenDTO} que contienen el nombre 
     * especificado o que coinciden parcialmente con él.
     *
     * @param nombre el nombre o parte del nombre del almacén a buscar.
     * @param descripcion la descripcion o parte de la descripcion del almacén a buscar.
     * @return una lista de {@link AlmacenDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<AlmacenDTO> findAlmacenes(String nombre, String descripcion);
    
    /**
     * Crea un nuevo almacén en la base de datos.
     *
     * Este método inserta un nuevo almacén en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link AlmacenDTO}. Si la inserción es exitosa, el 
     * objeto {@link AlmacenDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link AlmacenDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param almacenDTO el objeto {@link AlmacenDTO} que contiene los datos del almacén a insertar.
     * @return el objeto {@link AlmacenDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public AlmacenDTO createAlmacen(AlmacenDTO almacenDTO);
    
    /**
     * Actualiza la información de un almacén en la base de datos.
     *
     * Este método actualiza los datos de un almacén existente utilizando los valores 
     * proporcionados en el objeto {@link AlmacenDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param almacenDTO el objeto {@link AlmacenDTO} que contiene los datos actualizados del almacén.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateAlmacen(AlmacenDTO almacenDTO);
    
    /**
     * Elimina un almacén de la base de datos.
     *
     * Este método elimina un almacén específico de la base de datos,
     * devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param almacenId el identificador único del almacén a eliminar.
     * @return {@code true} si el almacén fue eliminado con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteAlmacen(int almacenId);
}
