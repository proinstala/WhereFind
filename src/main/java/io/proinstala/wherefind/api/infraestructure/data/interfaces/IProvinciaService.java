
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar las provincias.
 */
public interface IProvinciaService {
    
    /**
     * Obtiene una provincia específica por su identificador.
     *
     * Este método devuelve un objeto {@link ProvinciaDTO} que representa la provincia
     * con el identificador proporcionado. Si no se encuentra ninguna provincia con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ninguna dirección 
     * existente.
     *
     * @param idProvincia el identificador único de la provincia a obtener.
     * @return el objeto {@link ProvinciaDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ninguna provincia con ese ID.
     */
    public ProvinciaDTO getProvinciaById(int idProvincia);
    
    /**
     * Obtiene una lista de todas las provincias disponibles.
     *
     * Este método devuelve una lista de objetos {@link ProvinciaDTO} que representan todas 
     * las provincias registradas en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * provincias disponibles.
     *
     * @return una lista de {@link ProvinciaDTO} con todas las provincias, o una lista vacía si no hay provincias registradas.
     */
    public List<ProvinciaDTO> getAllProvincias();
    
    /**
     * Busca provincias cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link ProvinciaDTO} que contienen el nombre 
     * especificado o que coinciden parcialmente con él.
     *
     * @param nombre el nombre o parte del nombre de la provincia a buscar.
     * @return una lista de {@link ProvinciaDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<ProvinciaDTO> findProvincias(String nombre);
    
    /**
     * Crea una nueva provincia en la base de datos.
     *
     * Este método inserta una nueva provincia en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link ProvinciaDTO}. Si la inserción es exitosa, el 
     * objeto {@link ProvinciaDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link ProvinciaDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param provinciaDTO el objeto {@link ProvinciaDTO} que contiene los datos de la 
     *                     provincia a insertar. El objeto debe tener el valor del nombre.
     * @return el objeto {@link ProvinciaDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public ProvinciaDTO createProvincia(ProvinciaDTO provinciaDTO);
    
    /**
     * Actualiza la información de una provincia en la base de datos.
     *
     * Este método actualiza los datos de una provincia existente utilizando los valores 
     * proporcionados en el objeto {@link ProvinciaDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param provinciaDTO el objeto {@link ProvinciaDTO} que contiene los datos actualizados de la provincia.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateProvincia(ProvinciaDTO provinciaDTO);
    
    /**
     * Elimina una provincia de la base de datos.
     *
     * Este método elimina una provincia especifica de la base de datos,
     * devuelve {@code true} 
     * si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param provinciaId el identificador único de la dirección a eliminar.
     * @return {@code true} si la provincia fue eliminada con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteProvincia(int provinciaId);
}
