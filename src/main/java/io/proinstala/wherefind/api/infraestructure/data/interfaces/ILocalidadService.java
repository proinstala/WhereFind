
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar las localidades.
 */
public interface ILocalidadService {
    
    /**
     * Obtiene una localidad específica por su identificador.
     *
     * Este método devuelve un objeto {@link LocalidadDTO} que representa la localidad
     * con el identificador proporcionado. Si no se encuentra ninguna localidad con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ninguna localidad 
     * existente.
     *
     * @param idLocalidad el identificador único de la localidad a obtener.
     * @return el objeto {@link LocalidadDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ninguna localidad con ese ID.
     */
    public LocalidadDTO getLocalidadById(int idLocalidad);
    
    /**
     * Obtiene una lista de todas las localidades disponibles.
     *
     * Este método devuelve una lista de objetos {@link LocalidadDTO} que representan todas 
     * las localidades registradas en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * localidades disponibles.
     *
     * @return una lista de {@link LocalidadDTO} con todas las localidades, o una lista vacía si no hay localidades registradas.
     */
    public List<LocalidadDTO> getAllLocalidades();
    
    /**
     * Obtiene una lista de localidades que pertenecen a una provincia específica.
     *
     * <p>Este método devuelve una lista de objetos {@link LocalidadDTO} que representan las localidades 
     * que están asociadas con la provincia especificada. La provincia debe ser proporcionada como un objeto 
     * {@link ProvinciaDTO}. Las implementaciones deben manejar correctamente los casos en los que la provincia 
     * proporcionada sea nula o no tenga localidades asociadas.</p>
     *
     * @param provincia el objeto {@link ProvinciaDTO} que representa la provincia para la cual se desean obtener las localidades.
     * @return una lista de {@link LocalidadDTO} que pertenecen a la provincia especificada, o una lista vacía si no hay localidades asociadas a la provincia.
     */
    public List<LocalidadDTO> getLocalidadesOfProvincia(ProvinciaDTO provincia);
    
    /**
     * Busca localidades cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link LocalidadDTO} que contienen el nombre 
     * especificado o que coinciden parcialmente con él.
     *
     * @param nombre el nombre o parte del nombre de la localidad a buscar.
     * @param idProvincia el identificador de provincia para filtrar.
     * @return una lista de {@link LocalidadDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<LocalidadDTO> findLocalidades(String nombre, int idProvincia);
    
    /**
     * Crea una nueva localidad en la base de datos.
     *
     * Este método inserta una nueva localidad en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link LocalidadDTO}. Si la inserción es exitosa, el 
     * objeto {@link LocalidadDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link LocalidadDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param localidadDTO el objeto {@link LocalidadDTO} que contiene los datos de la 
     *                     localidad a insertar. El objeto debe tener los valores de nombre, 
     *                     y el ID de la provincia.
     * @return el objeto {@link LocalidadDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public LocalidadDTO createLocalidad(LocalidadDTO localidadDTO);
    
    /**
     * Elimina una localidad de la base de datos.
     *
     * Este método elimina una localidad específica en la base de datos. 
     * Devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param localidadId el identificador único de la localidad a eliminar.
     * @return {@code true} si la localidad fue eliminada con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteLocalidad(int localidadId);
    
    /**
     * Actualiza la información de una localidad en la base de datos.
     *
     * Este método actualiza los datos de una localidad existente utilizando los valores 
     * proporcionados en el objeto {@link LocalidadDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param localidadDTO el objeto {@link LocalidadDTO} que contiene los datos actualizados de la localidad.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateLocalidad(LocalidadDTO localidadDTO);
}
