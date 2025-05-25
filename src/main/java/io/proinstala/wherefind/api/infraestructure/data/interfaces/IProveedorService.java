
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import java.util.List;

/**
 *
 * Interfaz que define los métodos para gestionar los proveedores.
 */
public interface IProveedorService {
    
    /**
     * Obtiene un proveedor por su identificador único.
     *
     * @param idProveedor el ID del proveedor a buscar
     * @return el proveedor correspondiente al ID, o null si no se encuentra
     */
    public ProveedorDTO getProveedorById(int idProveedor);
    
    /**
     * Obtiene una lista de todos los proveedores.
     *
     * @return una lista de todos los proveedores existentes
     */
    public List<ProveedorDTO> getProveedores();
    
    /**
     * Busca una lista de proveedores que coincidan con el nombre especificado.
     *
     * @param nombre el nombre (o parte del nombre) del proveedor a buscar
     * @return una lista de proveedores que coinciden con el criterio de búsqueda
     */
    public List<ProveedorDTO> findProveedores(String nombre, String direccion);
    
    /**
     * Actualiza la información de un proveedor existente.
     *
     * @param proveedorDTO el proveedor con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean updateProveedor(ProveedorDTO proveedorDTO);
    
    /**
     * Crea un nuevo proveedor en el sistema.
     *
     * @param proveedorDTO el proveedor a crear
     * @return el proveedor creado con su ID asignado
     */
    public ProveedorDTO createProveedor(ProveedorDTO proveedorDTO);
    
    /**
     * Elimina un proveedor por su identificador único.
     *
     * @param proveedorId el ID del proveedor a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean deleteProveedor(int proveedorId);
    
}
