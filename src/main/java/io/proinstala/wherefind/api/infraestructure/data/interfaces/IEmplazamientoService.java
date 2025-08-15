
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar los emplazamientos.
 */
public interface IEmplazamientoService {
    
    /**
     * Obtiene un emplazamiento específico por su identificador.
     *
     * Este método devuelve un objeto {@link EmplazamientoDTO} que representa el emplazamiento
     * con el identificador proporcionado. Si no se encuentra ningún emplazamiento con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ningún emplazamiento 
     * existente.
     *
     * @param idEmplazamiento el identificador único del emplazamiento a obtener.
     * @return el objeto {@link EmplazamientoDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ningún emplazamiento con ese ID.
     */
    public EmplazamientoDTO getEmplazamientoById(int idEmplazamiento);
    
    /**
     * Obtiene una lista de todos los emplazamientos disponibles.
     *
     * Este método devuelve una lista de objetos {@link EmplazamientoDTO} que representan todos 
     * los emplazamientos registrados en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * emplazamientos disponibles.
     *
     * @return una lista de {@link EmplazamientoDTO} con todos los emplazamientos, o una lista vacía si no hay emplazamientos registrados.
     */
    public List<EmplazamientoDTO> getAllEmplazamientos();
    
    /**
     * Busca emplazamientos filtrados por almacén, tipo de emplazamiento y nombre.
     *
     * Este método devuelve una lista de objetos {@link EmplazamientoDTO} que pertenecen 
     * al almacén y tipo de emplazamiento especificados y cuyo nombre coincida total o 
     * parcialmente con el parámetro dado.
     *
     * @param almacenId el identificador único del almacén.
     * @param tipoEmplazamientoId el identificador único del tipo de emplazamiento.
     * @param nombre el nombre o parte del nombre del emplazamiento a buscar.
     * @return una lista de {@link EmplazamientoDTO} que cumplen con los criterios de búsqueda,
     *         o una lista vacía si no hay coincidencias.
     */
    public List<EmplazamientoDTO> findEmplazamientos(int almacenId, int tipoEmplazamientoId, String nombre);
    
    /**
     * Crea un nuevo emplazamiento en la base de datos.
     *
     * Este método inserta un nuevo emplazamiento en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link EmplazamientoDTO}. Si la inserción es exitosa, el 
     * objeto {@link EmplazamientoDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link EmplazamientoDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param emplazamientoDTO el objeto {@link EmplazamientoDTO} que contiene los datos del 
     *                         emplazamiento a insertar.
     * @return el objeto {@link EmplazamientoDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public EmplazamientoDTO createEmplazamiento(EmplazamientoDTO emplazamientoDTO);
    
    /**
     * Actualiza la información de un emplazamiento en la base de datos.
     *
     * Este método actualiza los datos de un emplazamiento existente utilizando los valores 
     * proporcionados en el objeto {@link EmplazamientoDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param emplazamientoDTO el objeto {@link EmplazamientoDTO} que contiene los datos actualizados del emplazamiento.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateEmplazamiento(EmplazamientoDTO emplazamientoDTO);
    
    
    /**
     * Elimina un emplazamiento de la base de datos.
     *
     * Este método elimina un emplazamiento específico de la base de datos,
     * devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param emplazamientoId el identificador único del emplazamiento a eliminar.
     * @return {@code true} si el emplazamiento fue eliminado con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteEmplazamiento(int emplazamientoId);
}
