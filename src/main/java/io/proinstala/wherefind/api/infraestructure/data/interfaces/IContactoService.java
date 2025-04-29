
package io.proinstala.wherefind.api.infraestructure.data.interfaces;

import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import java.util.List;

/**
 * Interfaz que define los métodos para gestionar los contactos de los proveedores.
 * Los métodos permiten obtener, crear, actualizar, eliminar y buscar contactos.
 */
public interface IContactoService {
    
    /**
     * Obtiene un contacto por su identificador único.
     *
     * @param idContacto el ID del contacto a buscar
     * @return el contacto correspondiente al ID, o null si no se encuentra
     */
    public ContactoDTO getContactoById(int idContacto);
    
    /**
     * Obtiene todos los contactos asociados a un proveedor.
     *
     * @param idProveedor el ID del proveedor
     * @return una lista de contactos asociados al proveedor
     */
    public List<ContactoDTO> getContactosByProveedorId(int idProveedor);
    
    /**
     * Busca contactos basándose en un nombre y un ID de proveedor específicos.
     * Si alguno de los parámetros no se proporciona o es inválido, se omite en la búsqueda.
     *
     * @param nombre el nombre del contacto a buscar. Si es null o vacío, se ignora en la búsqueda.
     * @param proveedorId el ID del proveedor a filtrar los contactos. Si es -1, se ignora en la búsqueda.
     * @return una lista de contactos que coinciden con los criterios proporcionados
     */
    public List<ContactoDTO> findContactos(String nombre, int proveedorId);
    
    /**
     * Crea un nuevo contacto en la base de datos.
     *
     * @param contactoDTO el objeto ContactoDTO con la información del nuevo contacto
     * @return el contacto creado con su ID asignado, o null si ocurrió un error durante la creación
     */
    public ContactoDTO createContacto(ContactoDTO contactoDTO);
    
    /**
     * Actualiza un contacto existente.
     *
     * @param contactoDTO el contacto con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean updateContacto(ContactoDTO contactoDTO);
    
    /**
     * Elimina un contacto de manera lógica, estableciendo su estado como inactivo.
     *
     * @param idContacto el ID del contacto a eliminar
     * @return true si el contacto fue marcado como inactivo correctamente, false en caso contrario
     */
    public boolean deleteContacto(int idContacto);
    
}
