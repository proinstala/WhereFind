
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ArticuloDTO;
import io.proinstala.wherefind.shared.dtos.ArticuloProveedorDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar los artículos.
 */
public interface IArticuloService {
    
    /**
     * Obtiene un artículo específico por su identificador.
     *
     * Este método devuelve un objeto {@link ArticuloDTO} que representa el artículo
     * con el identificador proporcionado. Si no se encuentra ningún artículo con el ID 
     * especificado, el método puede devolver {@code null}. Las implementaciones deben 
     * manejar adecuadamente los casos en los que el ID no corresponde a ningún artículo 
     * existente.
     *
     * @param idArticulo el identificador único del artículo a obtener.
     * @return el objeto {@link ArticuloDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ningún artículo con ese ID.
     */
    public ArticuloDTO getArticuloById(int idArticulo);
    
    /**
     * Obtiene una lista de todos los artículos disponibles.
     *
     * Este método devuelve una lista de objetos {@link ArticuloDTO} que representan todos 
     * los artículos registrados en el sistema. Las implementaciones de este método deberían 
     * asegurarse de manejar correctamente las excepciones y los casos en los que no haya 
     * artículos disponibles.
     *
     * @return una lista de {@link ArticuloDTO} con todos los artículoes, o una lista vacía si no hay artículos registrados.
     */
    public List<ArticuloDTO> getAllArticulos();
    
    /**
     * Busca artículos cuyo nombre coincida total o parcialmente con el parámetro dado.
     *
     * Este método devuelve una lista de objetos {@link ArticuloDTO} que contienen el nombre o la
     * descripción especificado o que coinciden parcialmente con ellos.
     *
     * @param nombre el nombre o parte del nombre del artículo a buscar.
     * @param descripcion la descripcion o parte de la descripcion del artículo a buscar.
     * @param referencia la referencia o parte de la referencia del artículo a buscar.
     * @param marcaId el id de la marca del atículo a buscar.
     * @return una lista de {@link ArticuloDTO} que coinciden con el criterio de búsqueda, o una lista vacía si no hay coincidencias.
     */
    public List<ArticuloDTO> findArticulos(String nombre, String descripcion, String referencia, int marcaId);
    
    /**
     * Crea un nuevo artículo en la base de datos.
     *
     * Este método inserta un nuevo artículo en la base de datos utilizando los valores 
     * proporcionados en el objeto {@link ArticuloDTO}. Si la inserción es exitosa, el 
     * objeto {@link ArticuloDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve 
     * {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link ArticuloDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param articuloDTO el objeto {@link ArticuloDTO} que contiene los datos del artículo a insertar.
     * @return el objeto {@link ArticuloDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public ArticuloDTO createArticulo(ArticuloDTO articuloDTO);
    
    /**
     * Actualiza la información de un artículo en la base de datos.
     *
     * Este método actualiza los datos de un artículo existente utilizando los valores 
     * proporcionados en el objeto {@link ArticuloDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param articuloDTO el objeto {@link ArticuloDTO} que contiene los datos actualizados del artículo.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateArticulo(ArticuloDTO articuloDTO);
    
    
    /**
     * Elimina un artículo de la base de datos.
     *
     * Este método elimina un artículo específico de la base de datos,
     * devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param articuloId el identificador único del artículo a eliminar.
     * @return {@code true} si el artículo fue eliminado con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteArticulo(int articuloId);
    
    
    
    
    // ─────────────────────────────────────────────────────────────
    // Métodos para la gestión de asociaciones Artículo-Proveedor
    // ─────────────────────────────────────────────────────────────
  
    
    /**
     * Obtiene una asociación específica entre un artículo y un proveedor por su identificador.
     *
     * <p>Este método devuelve un objeto {@link ArticuloProveedorDTO} que representa la relación
     * entre un artículo y un proveedor. Si no se encuentra ninguna asociación con el ID especificado,
     * el método devuelve {@code null}. Las implementaciones deben manejar adecuadamente los casos en los
     * que el ID no corresponde a ningún registro existente.</p>
     *
     * @param articuloProveedorId el identificador único de la asociación artículo-proveedor a obtener.
     * @return el objeto {@link ArticuloProveedorDTO} correspondiente al ID proporcionado, o {@code null} si no se encuentra ninguna asociación con ese ID.
     */
    public ArticuloProveedorDTO getArticuloProveedorById(int articuloProveedorId);
    
    
    /**
     * Crea una nueva asociación entre un artículo y un proveedor.
     *
     * Este método inserta una nueva relación entre un artículo y un proveedor utilizando los valores 
     * proporcionados en el objeto {@link ArticuloProveedorDTO}. Si la inserción es exitosa, el 
     * objeto {@link ArticuloProveedorDTO} se devuelve con el ID generado automáticamente por la 
     * base de datos asignado. En caso de error durante la inserción, el método devuelve {@code null}.
     *
     * <p>La implementación debe manejar el proceso de inserción y asignar el ID generado 
     * al objeto {@link ArticuloProveedorDTO}. El manejo adecuado de los recursos y la captura de 
     * excepciones son esenciales para garantizar la integridad de los datos.</p>
     *
     * @param articuloProveedorDTO el objeto {@link ArticuloProveedorDTO} que contiene los datos de la asociación a insertar.
     * @return el objeto {@link ArticuloProveedorDTO} con el ID asignado si la inserción fue exitosa, 
     *         o {@code null} si ocurrió un error durante la inserción.
     */
    public ArticuloProveedorDTO createArticuloProveedor(ArticuloProveedorDTO articuloProveedorDTO);
    
    
    /**
     * Actualiza la información de una asociación entre un artículo y un proveedor en la base de datos.
     *
     * Este método actualiza los datos de una asociación existente utilizando los valores 
     * proporcionados en el objeto {@link ArticuloProveedorDTO}. El método devuelve {@code true} si 
     * la actualización fue exitosa, es decir, si se afectó al menos una fila. Si ocurre un error 
     * o no se actualiza ninguna fila, devuelve {@code false}.
     *
     * @param articuloProveedorDTO el objeto {@link ArticuloProveedorDTO} que contiene los datos actualizados de la asociación.
     * @return {@code true} si la actualización se realizó con éxito, o {@code false} si ocurrió un error o no se actualizó ninguna fila.
     */
    public boolean updateArticuloProveedor(ArticuloProveedorDTO articuloProveedorDTO);
    
    
    /**
     * Elimina una asociación entre un artículo y un proveedor de la base de datos.
     *
     * Este método elimina una relación específica entre un artículo y un proveedor,
     * devuelve {@code true} si la operación se realizó con éxito, es decir, si al menos una fila fue afectada.
     * En caso de error o si no se afectó ninguna fila, devuelve {@code false}.
     *
     * @param articuloProveedorId el identificador único de la asociación artículo-proveedor a eliminar.
     * @return {@code true} si la asociación fue eliminada con éxito, o {@code false} si ocurrió un error o no se eliminó ninguna fila.
     */
    public boolean deleteArticuloProveedor(int articuloProveedorId);
}
