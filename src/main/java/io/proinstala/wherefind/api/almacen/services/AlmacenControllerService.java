
package io.proinstala.wherefind.api.almacen.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IAlmacenService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.AlmacenDTO;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import io.proinstala.wherefind.shared.tools.LocalDateAdapter;
import java.time.LocalDate;

/**
 *
 * @author David
 */
public class AlmacenControllerService extends BaseService {

    /**
     * Busca almacenes según los parámetros de nombre y descripción proporcionados en la solicitud.
     * 
     * <p>Obtiene los parámetros de nombre y descripción del almacén desde el controlador de acción,
     * utiliza el servicio de almacén para recuperar la lista de almacenes que coinciden y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void findAlmacenes(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        IAlmacenService almacenServiceImp = GestorPersistencia.getAlmacenService();

        List<AlmacenDTO> listaAlmacenDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_ALMACEN_NOMBRE, "");
        String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_ALMACEN_DESCRIPCION, "");

        listaAlmacenDTO = almacenServiceImp.findAlmacenes(nombre, descripcion);

        if(listaAlmacenDTO != null) {
            responseDTO = getResponseOk("OK", listaAlmacenDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Obtiene un almacen por su identificador.
     * 
     * <p>Este método extrae el identificador del almacen del controlador de acción, utiliza el
     * servicio de almacen para recuperar los datos del almacen correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getAlmacenById(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IAlmacenService almacenServiceImp = GestorPersistencia.getAlmacenService();
        
        AlmacenDTO almacenDTO = null;

        int idAlmacen = -1;
        try {
            String id = actionController.server().getRequestParameter("idAlmacen", "-1");
            idAlmacen = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        almacenDTO = almacenServiceImp.getAlmacenById(idAlmacen);
        
        if(almacenDTO != null) {
            responseDTO = getResponseOk("OK", almacenDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Obtiene la lista de todos los almacenes.
     * 
     * <p>Utiliza el servicio de almacén para recuperar la lista completa de almacenes y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getAlmacenes(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IAlmacenService almacenServiceImp = GestorPersistencia.getAlmacenService();

        List<AlmacenDTO> listaAlmacenDTO = null;
        
        listaAlmacenDTO = almacenServiceImp.getAllAlmacenes();
        
        
        if (listaAlmacenDTO != null) {
            responseDTO = getResponseOk("OK", listaAlmacenDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea un nuevo almacen en la base de datos.
     * 
     * <p>Este método toma los datos del nuevo almacen en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de almacen para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createAlmacen(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IAlmacenService almacenServiceImp = GestorPersistencia.getAlmacenService();
        
        String jsonAlmacen = actionController.server().getRequestParameter("almacenJSON", "");
        
        AlmacenDTO almacenDTO = null;
        if(jsonAlmacen != null && !jsonAlmacen.isBlank()) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            almacenDTO = gson.fromJson(jsonAlmacen, AlmacenDTO.class);
            
            almacenDTO = almacenServiceImp.createAlmacen(almacenDTO);
        }
        
        if(almacenDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, almacenDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Actualiza la información de un alamacen existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar un almacen en la base de
     * datos. Utiliza el servicio de almacen para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateAlmacen(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id del alamacen desde el parámetro 1 de la lista de parámetros
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
        IAlmacenService almacenServiceImp = GestorPersistencia.getAlmacenService();

        AlmacenDTO almacenDTO = almacenServiceImp.getAlmacenById(id);

        if(almacenDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_ALMACEN_NOMBRE, "");
            String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_ALMACEN_DESCRIPCION, "");
            String strDirecion = actionController.server().getRequestParameter(FormParametros.PARAM_ALMACEN_DIRECCION, "");

            try {
                int direccionId = Integer.parseInt(strDirecion);
                almacenDTO.setNombre(nombre);
                almacenDTO.setDescripcion(descripcion);

                if(direccionId > 0) {
                    almacenDTO.setDireccion(DireccionDTO.builder().id(direccionId).build());
                } else {
                     almacenDTO.setDireccion(null);
                }

                if (almacenServiceImp.updateAlmacen(almacenDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, almacenDTO, 0);
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
     * Elimina un almacen por su identificador.
     * 
     * <p>Este método extrae el identificador del alamacen del controlador de acción, utiliza el
     * servicio de alamace para eliminar el alamacen correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteAlmacen(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id del almacen desde el parámetro 1 de la lista de parámetros
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
        IAlmacenService almacenServiceImp = GestorPersistencia.getAlmacenService();
        
        if(almacenServiceImp.deleteAlmacen(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
}
