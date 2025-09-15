
package io.proinstala.wherefind.api.almacen.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IEmplazamientoService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import io.proinstala.wherefind.shared.tools.LocalDateAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio controlador para la gestión de emplazamientos.
 * 
 * <p>Proporciona métodos para buscar, obtener, crear, actualizar y eliminar 
 * emplazamientos en el sistema. Cada método recibe un {@code ActionController} 
 * que contiene los parámetros de la solicitud y devuelve una respuesta en formato JSON.</p>
 * 
 */
public class EmplazamientoControllerService extends BaseService {
    
    /**
     * Busca emplazamientos según los parámetros de nombre y descripción proporcionados en la solicitud.
     * 
     * <p>Obtiene los parámetros de nombre y descripción del emplazamiento desde el controlador de acción,
     * utiliza el servicio de emplazamiento para recuperar la lista de emplazamientos que coinciden 
     * y devuelve la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void findEmplazamientos(ActionController actionController) {
        ResponseDTO responseDTO;

        IEmplazamientoService emplazamientoServiceImp = GestorPersistencia.getEmplazamientoService();

        List<EmplazamientoDTO> listaEmplazamientoDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_EMPLAZAMIENTO_NOMBRE, "");
        String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_EMPLAZAMIENTO_DESCRIPCION, "");
        String strIdTipo = actionController.server().getRequestParameter(FormParametros.PARAM_EMPLAZAMIENTO_TIPO, "");
        String strIdAlmacen = actionController.server().getRequestParameter(FormParametros.PARAM_EMPLAZAMIENTO_ALMACEN, "");
        
        int idTipo = -1;
        try {
            if(!strIdTipo.isBlank()) {
                idTipo = Integer.parseInt(strIdTipo);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        int idAlmacen = -1;
        try {
            if(!strIdAlmacen.isBlank()) {
                idAlmacen = Integer.parseInt(strIdAlmacen);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        listaEmplazamientoDTO = emplazamientoServiceImp.findEmplazamientos(nombre.trim(), descripcion.trim(), idTipo,  idAlmacen);

        if(listaEmplazamientoDTO != null) {
            responseDTO = getResponseOk("OK", listaEmplazamientoDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Obtiene un emplazamiento por su identificador.
     * 
     * <p>Este método extrae el identificador del emplazamiento del controlador de acción, utiliza el
     * servicio de emplazamiento para recuperar los datos del emplazamiento correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getEmplazamientoById(ActionController actionController) {
        ResponseDTO responseDTO;
        
        IEmplazamientoService emplazamientoServiceImp = GestorPersistencia.getEmplazamientoService();
        
        EmplazamientoDTO emplazamientoDTO = null;

        int idEmplazamiento = -1;
        try {
            String id = actionController.server().getRequestParameter("idEmplazamiento", "-1");
            idEmplazamiento = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        emplazamientoDTO = emplazamientoServiceImp.getEmplazamientoById(idEmplazamiento);
        
        if(emplazamientoDTO != null) {
            responseDTO = getResponseOk("OK", emplazamientoDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Obtiene la lista de todos los emplazamientos.
     * 
     * <p>Utiliza el servicio de emplazamiento para recuperar la lista completa de emplazamientos 
     * y devuelve la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getEmplazamientos(ActionController actionController) {
        ResponseDTO responseDTO;

        IEmplazamientoService emplazamientoServiceImp = GestorPersistencia.getEmplazamientoService();

        List<EmplazamientoDTO> listaEmplazamientoDTO = emplazamientoServiceImp.getAllEmplazamientos();
        
        if (listaEmplazamientoDTO != null) {
            responseDTO = getResponseOk("OK", listaEmplazamientoDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea un nuevo emplazamiento en la base de datos.
     * 
     * <p>Este método toma los datos del nuevo emplazamiento en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de emplazamiento para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createEmplazamiento(ActionController actionController) {
        ResponseDTO responseDTO;
        
        IEmplazamientoService emplazamientoServiceImp = GestorPersistencia.getEmplazamientoService();
        
        String jsonEmplazamiento = actionController.server().getRequestParameter("emplazamientoJSON", "");
        
        EmplazamientoDTO emplazamientoDTO = null;
        if(jsonEmplazamiento != null && !jsonEmplazamiento.isBlank()) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            emplazamientoDTO = gson.fromJson(jsonEmplazamiento, EmplazamientoDTO.class);
            
            emplazamientoDTO = emplazamientoServiceImp.createEmplazamiento(emplazamientoDTO);
        }
        
        if(emplazamientoDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, emplazamientoDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Actualiza la información de un emplazamiento existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar un emplazamiento en la base de
     * datos. Utiliza el servicio de emplazamiento para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateEmplazamiento(ActionController actionController) {
        ResponseDTO responseDTO;
        
        if (actionController.parametros().length <= 1) {
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        int id = actionController.getIntFromParametros(1);

        if (id == -1) {
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            responseJson(actionController.server().response(), responseDTO);
            return;
        }
        
        IEmplazamientoService emplazamientoServiceImp = GestorPersistencia.getEmplazamientoService();

        EmplazamientoDTO emplazamientoDTO = emplazamientoServiceImp.getEmplazamientoById(id);

        if(emplazamientoDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_EMPLAZAMIENTO_NOMBRE, "");
            String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_EMPLAZAMIENTO_DESCRIPCION, "");

            try {
                emplazamientoDTO.setNombre(nombre);
                emplazamientoDTO.setDescripcion(descripcion);

                if (emplazamientoServiceImp.updateEmplazamiento(emplazamientoDTO)) {
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, emplazamientoDTO, 0);
                } else {
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }

            } catch (Exception e) {
                responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
            }
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
        }

        responseJson(actionController.server().response(), responseDTO);
    } 

    /**
     * Elimina un emplazamiento por su identificador.
     * 
     * <p>Este método extrae el identificador del emplazamiento del controlador de acción, utiliza el
     * servicio de emplazamiento para eliminar el emplazamiento correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteEmplazamiento(ActionController actionController) {
        ResponseDTO responseDTO;
        
        if (actionController.parametros().length <= 1) {
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        int id = actionController.getIntFromParametros(1);

        if (id == -1) {
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            responseJson(actionController.server().response(), responseDTO);
            return;
        }
        
        IEmplazamientoService emplazamientoServiceImp = GestorPersistencia.getEmplazamientoService();
        
        if(emplazamientoServiceImp.deleteEmplazamiento(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    } 
    
}
