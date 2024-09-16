
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar las direcciones.
 */
public interface IDireccionService {
    
    /**
     * Obtiene una dirección específica por su identificador.
     *
     * Este método devuelve un objeto {@link DireccionDTO} que representa la dirección 
     * con el identificador proporcionado. Si no se encuentra ninguna dirección con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ninguna dirección 
     * existente.
     *
     * @param idDireccion el identificador único de la dirección a obtener.
     * @return el objeto {@link DireccionDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ninguna dirección con ese ID.
     */
    public DireccionDTO getDireccionById(int idDireccion);
    
    /**
     * Busca direcciones que coincidan con los criterios de búsqueda especificados.
     *
     * Este método devuelve una lista de objetos {@link DireccionDTO} que coinciden con los 
     * criterios de búsqueda proporcionados. Los parámetros de búsqueda permiten filtrar las direcciones 
     * en función del nombre de la calle, el identificador de localidad y el identificador de provincia. 
     * Las implementaciones deben considerar adecuadamente los casos en los que los valores proporcionados 
     * no son válidos o están en blanco.
     *
     * @param calle el nombre de la calle o parte del nombre para buscar direcciones.
     * @param localidad el identificador de la localidad para filtrar las direcciones, o un valor especial para ignorar este filtro.
     * @param provincia el identificador de la provincia para filtrar las direcciones, o un valor especial para ignorar este filtro.
     * @return una lista de {@link DireccionDTO} que cumplen con los criterios de búsqueda, o una lista vacía si no se encuentran coincidencias.
     */
    public List<DireccionDTO> findDirecciones(String calle, int localidad, int provincia);
    
    /**
     * Actualiza la información de una dirección en la base de datos.
     *
     * Este método actualiza los datos de una dirección existente utilizando los valores 
     * proporcionados en el objeto {@link DireccionDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param direccionDTO el objeto {@link DireccionDTO} que contiene los datos actualizados de la dirección.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateDireccion(DireccionDTO direccionDTO);
    
    /**
     * Crea una nueva dirección en la base de datos.
     *
     * Este método inserta una nueva dirección en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link DireccionDTO}. Si la inserción es exitosa, el 
     * objeto {@link DireccionDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link DireccionDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param direccionDTO el objeto {@link DireccionDTO} que contiene los datos de la 
     *                     dirección a insertar. El objeto debe tener los valores de calle, 
     *                     número, código postal y el ID de la localidad.
     * @return el objeto {@link DireccionDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public DireccionDTO createDireccion(DireccionDTO direccionDTO);
    
    /**
     * Elimina una dirección de la base de datos.
     *
     * Este método marca una dirección específica como eliminada en la base de datos,
     * cambiando su estado a inactivo o eliminándola lógicamente. Devuelve {@code true} 
     * si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param direccionId el identificador único de la dirección a eliminar.
     * @return {@code true} si la dirección fue eliminada con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteDireccion(int direccionId);
}
