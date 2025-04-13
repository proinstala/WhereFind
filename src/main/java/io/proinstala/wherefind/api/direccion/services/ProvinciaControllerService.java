
package io.proinstala.wherefind.api.direccion.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProvinciaService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja operaciones relacionadas con provincias.
 *
 * Esta clase se encarga de gestionar las solicitudes sobre provincias y construir 
 * las respuestas adecuadas para dichas solicitudes. Extiende {@link BaseService} y utiliza servicios 
 * específicos para interactuar con la capa de persistencia.
 */
public class ProvinciaControllerService extends BaseService {
    
    /**
     * Obtiene una provincia por su identificador.
     * 
     * <p>Este método extrae el identificador de la provincia del controlador de acción, utiliza el
     * servicio de provincia para recuperar los datos de la provincia correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getProvinciaById(ActionController actionController) {
        ResponseDTO responseDTO;
        ProvinciaDTO provinciaDTO;
        
        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();
        
        int idProvincia = -1;
        try {
            String id = actionController.server().getRequestParameter("idProvincia", "-1");
            idProvincia = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        provinciaDTO = provinciaServiceImp.getProvinciaById(idProvincia);
        
        if (provinciaDTO != null) {
            responseDTO = getResponseOk("OK", provinciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Maneja la solicitud para obtener todas las provincias.
     *
     * Este método interactúa con el servicio de provincias para obtener una lista completa de todas las 
     * provincias disponibles. Luego, construye una respuesta {@link ResponseDTO} que indica el estado de 
     * la solicitud. Si la obtención de provincias es exitosa, se envía una respuesta con la lista de provincias. 
     * En caso de error, se devuelve una respuesta con un mensaje de error.
     *
     * @param actionController el {@link ActionController} que maneja la solicitud y la respuesta.
     */
    public void getProvincias(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();

        //Obtiene la lista de Provincias
        List<ProvinciaDTO> listaProvinciaDTO = provinciaServiceImp.getAllProvincias();

        if (listaProvinciaDTO != null) {
            responseDTO = getResponseOk("OK", listaProvinciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Busca provincias según el nombre proporcionado y devuelve la respuesta en formato JSON.
     *
     * <p>Este método obtiene el parámetro de búsqueda desde la solicitud HTTP y lo utiliza para 
     * recuperar una lista de provincias desde el servicio de persistencia. Si la búsqueda tiene 
     * éxito, devuelve una respuesta con la lista de provincias encontradas. En caso contrario, 
     * se envía una respuesta de error.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    public void findProvincias(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_PROVINCIA_NOMBRE, "");
        
        List<ProvinciaDTO> listaProvinciasDTO = provinciaServiceImp.findProvincias(nombre);
        
        if(listaProvinciasDTO != null) {
            responseDTO = getResponseOk("OK", listaProvinciasDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea una nueva provincia en la base de datos.
     * 
     * <p>Este método toma los datos de la nueva provincia en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de provincia para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createProvincia(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();
        
        String jsonProvincia = actionController.server().getRequestParameter("provinciaJSON", "");
        
        ProvinciaDTO provinciaDTO = null;
        if(jsonProvincia != null && !jsonProvincia.isBlank()) {
            Gson gson = new Gson();
            provinciaDTO = gson.fromJson(jsonProvincia, ProvinciaDTO.class);
            
            provinciaDTO = provinciaServiceImp.createProvincia(provinciaDTO);
        }
        
        if(provinciaDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, provinciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Elimina una provincia por su identificador.
     * 
     * <p>Este método extrae el identificador de la provincia del controlador de acción, utiliza el
     * servicio de provincia para eliminar la provincia correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteProvincia(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id de la provincia desde el parámetro 1 de la lista de parámetros
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
        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();
        
        if(provinciaServiceImp.deleteProvincia(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
    
    /**
     * Actualiza la información de una provincia existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar una provincia en la base de
     * datos. Utiliza el servicio de provincia para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateProvincia(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id de la dirección desde el parámetro 1 de la lista de parámetros
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

        IProvinciaService provinciaServiceImp = GestorPersistencia.getProvinciaService();

        ProvinciaDTO provinciaDTO = provinciaServiceImp.getProvinciaById(id);

        if(provinciaDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_PROVINCIA_NOMBRE, "");

            try {
                provinciaDTO.setNombre(nombre);

                if (provinciaServiceImp.updateProvincia(provinciaDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, provinciaDTO, 0);
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
}
