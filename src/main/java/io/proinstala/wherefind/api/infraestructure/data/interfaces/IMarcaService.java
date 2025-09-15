
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.MarcaDTO;
import java.util.List;


/**
 * Interfaz que define los métodos para gestionar las marcas.
 */
public interface IMarcaService {
    
    /**
     * Obtiene una marca específica por su identificador.
     *
     * Este método devuelve un objeto {@link MarcaDTO} que representa la marca
     * con el identificador proporcionado. Si no se encuentra ningúna marca con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ningúna marca
     * existente.
     *
     * @param idMarca el identificador único de la marca a obtener.
     * @return el objeto {@link MarcaDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ningúna marca con ese ID.
     */
    public MarcaDTO getMarcaById(int idMarca);
    
    /**
     * Obtiene una lista de todas las marcas disponibles.
     *
     * Este método devuelve una lista de objetos {@link MarcaDTO} que representan todas 
     * las marcas registrados en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * marcas disponibles.
     *
     * @return una lista de {@link MarcaDTO} con todas las marcas, o una lista vacía si no hay marcas registrados.
     */
    public List<MarcaDTO> getAllMarcas();
    
    /**
     * Busca marcas cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link MarcaDTO} que contienen el nombre o la 
     * descripción especificados, o que coinciden parcialmente con ellos.
     *
     * @param nombre el nombre o parte del nombre de la marca a buscar.
     * @param descripcion la descripcion o parte de la descripcion de la marca a buscar.
     * @return una lista de {@link MarcaDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<MarcaDTO> findMarcas(String nombre, String descripcion);
    
    /**
     * Crea una nueva marca en la base de datos.
     *
     * Este método inserta una nueva marca en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link MarcaDTO}. Si la inserción es exitosa, el 
     * objeto {@link MarcaDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link MarcaDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param marcaDTO el objeto {@link MarcaDTO} que contiene los datos de la marca a insertar.
     * @return el objeto {@link MarcaDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public MarcaDTO createMarca(MarcaDTO marcaDTO);
    
    /**
     * Actualiza la información de una marca en la base de datos.
     *
     * Este método actualiza los datos de una marca existente utilizando los valores 
     * proporcionados en el objeto {@link MarcaDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param marcaDTO el objeto {@link MarcaDTO} que contiene los datos actualizados de la marca.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateMarca(MarcaDTO marcaDTO);
    
    /**
     * Elimina una marca de la base de datos.
     *
     * Este método elimina una marca específica de la base de datos,
     * devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param marcaId el identificador único de la marca a eliminar.
     * @return {@code true} si la marca fue eliminada con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteMarca(int marcaId);
    
}
