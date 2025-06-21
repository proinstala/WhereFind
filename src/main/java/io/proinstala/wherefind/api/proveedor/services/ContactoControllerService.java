
package io.proinstala.wherefind.api.proveedor.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IContactoService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con contactos.
 * 
 * <p>Esta clase proporciona métodos para obtener, buscar y actualizar contactos en la base de datos.</p>
 * 
 * <p>Extiende {@link BaseService} y utiliza {@link IContactoService} para interactuar con la base de datos.</p>
 */
public class ContactoControllerService extends BaseService {
    
    /**
     * Busca contactos según el nombre proporcionado y devuelve la respuesta en formato JSON.
     *
     * <p>Este método obtiene el parámetro de búsqueda desde la solicitud HTTP y lo utiliza para 
     * recuperar una lista de contactos desde el servicio de persistencia. Si la búsqueda tiene 
     * éxito, devuelve una respuesta con la lista de contactos encontradas. En caso contrario, 
     * se envía una respuesta de error.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    public void findContactos(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IContactoService contactoServiceImp = GestorPersistencia.getContactoService();
        
        List<ContactoDTO> listaContactosDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_NOMBRE, "");
        String strIdProveedor = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_PROVEEDOR, "");
        
        int idProveedor = -1;
        try {
            idProveedor = Integer.parseInt(strIdProveedor);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        listaContactosDTO = contactoServiceImp.findContactos(nombre, idProveedor);
        
        if(listaContactosDTO != null) {
            responseDTO = getResponseOk("OK", listaContactosDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene un contacto por su identificador.
     * 
     * <p>Este método extrae el identificador del contacto del controlador de acción, utiliza el
     * servicio de contacto para recuperar los datos del contacto correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getContactoById(ActionController actionController) {
        ResponseDTO responseDTO;
        ContactoDTO contactoDTO;
        
        // Conecta con el Gestor de Persistencia
        IContactoService contactoServiceImp = GestorPersistencia.getContactoService();
        
        int idContacto = -1;
        try {
            String id = actionController.server().getRequestParameter("idContacto", "-1");
            idContacto = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        contactoDTO = contactoServiceImp.getContactoById(idContacto);
        
        if (contactoDTO != null) {
            responseDTO = getResponseOk("OK", contactoDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Crea una nuevo contacto en la base de datos.
     * 
     * <p>Este método toma los datos de la nuevo contacto en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de contacto para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createContacto(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IContactoService contactoServiceImp = GestorPersistencia.getContactoService();
        
        String jsonContacto = actionController.server().getRequestParameter("contactoJSON", "");
        
        ContactoDTO contactoDTO = null;
        if(jsonContacto != null && !jsonContacto.isBlank()) {
            Gson gson = new Gson();
            contactoDTO = gson.fromJson(jsonContacto, ContactoDTO.class);
            
            contactoDTO = contactoServiceImp.createContacto(contactoDTO);
        }
        
        if(contactoDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, contactoDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Actualiza la información de un contacto existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar un contacto en la base de
     * datos. Utiliza el servicio de contacto para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateContacto(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id del contacto desde el parámetro 1 de la lista de parámetros
        int id = actionController.getIntFromParametros(1);

        // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
        // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
        if (id == -1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            responseJson(actionController.server().response(), responseDTO);
            return;
        }
        // Crea la respuesta con un error
        responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);

        // Conecta con el Gestor de Persistencia
        IContactoService contactoServiceImp = GestorPersistencia.getContactoService();

        ContactoDTO contactoDTO = contactoServiceImp.getContactoById(id);

        if(contactoDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_NOMBRE, "");
            String apellido = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_APELLIDO, "");
            String telefono = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_TELEFONO, "");
            String email = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_EMAIL, "");
            String strPuesto = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_PUESTO, "");
            String strProveedor = actionController.server().getRequestParameter(FormParametros.PARAM_CONTACTO_PROVEEDOR, "");

            try {
                int puestoId = Integer.parseInt(strPuesto);
                int proveedorId = Integer.parseInt(strProveedor);
                contactoDTO.setNombre(nombre);
                contactoDTO.setApellido(apellido);
                contactoDTO.setTelefono(telefono);
                contactoDTO.setEmail(email);
                
                contactoDTO.setPuestoTrabajo(PuestoTrabajoDTO.builder().id(puestoId).build());
                contactoDTO.setProveedor(ProveedorDTO.builder().id(proveedorId).build());

                if (contactoServiceImp.updateContacto(contactoDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, contactoDTO, 0);
                } else {
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }

            } catch (NumberFormatException e) {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            } catch (Exception e) {
                responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
            }
        }
        responseJson(actionController.server().response(), responseDTO);
    } 
    
    /**
     * Elimina un contacto por su identificador.
     * 
     * <p>Este método extrae el identificador del contacto del controlador de acción, utiliza el
     * servicio de contacto para eliminar el contacto correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteContacto(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id de la localidad desde el parámetro 1 de la lista de parámetros
        int id = actionController.getIntFromParametros(1);

        // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
        // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
        if (id == -1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            responseJson(actionController.server().response(), responseDTO);
            return;
        }
        
        // Conecta con el Gestor de Persistencia
        IContactoService contactoServiceImp = GestorPersistencia.getContactoService();
        
        if(contactoServiceImp.deleteContacto(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
}
