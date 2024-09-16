
package io.proinstala.wherefind.api.direccion.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con direcciones.
 * 
 * <p>Esta clase proporciona métodos para obtener, buscar y actualizar direcciones en la base de datos.</p>
 * 
 * <p>Extiende {@link BaseService} y utiliza {@link IDireccionService} para interactuar con la base de datos.</p>
 */
public class DireccionControllerService extends BaseService{
    
    /**
     * Obtiene una dirección por su identificador.
     * 
     * <p>Este método extrae el identificador de la dirección del controlador de acción, utiliza el
     * servicio de dirección para recuperar los datos de la dirección correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getDireccionById(ActionController actionController) {
        ResponseDTO responseDTO;
        DireccionDTO direccionDTO;
        
        IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();
        
        int idDireccion = -1;
        try {
            String id = actionController.server().getRequestParameter("idDireccion", "-1");
            idDireccion = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        direccionDTO = direccionServiceImp.getDireccionById(idDireccion);
        
        if (direccionDTO != null) {
            responseDTO = getResponseOk("OK", direccionDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Busca direcciones basadas en los parámetros proporcionados.
     * 
     * <p>Este método extrae los parámetros de búsqueda del controlador de acción, utiliza el servicio
     * de dirección para encontrar direcciones que coincidan con los criterios, y devuelve la respuesta
     * en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void findDirecciones(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();
        
        List<DireccionDTO> listaDireccionesDTO = null;
        
        String calle = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_CALLE, "");
        String strIdLocalidad = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_LOCALIDAD, "");
        String strIdProvincia = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_PROVINCIA, "");
        
        int idLocalidad = -1;
        try {
            idLocalidad = Integer.parseInt(strIdLocalidad);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        int idProvincia = -1;
        try {
            idProvincia = Integer.parseInt(strIdProvincia);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        listaDireccionesDTO = direccionServiceImp.findDirecciones(calle, idLocalidad, idProvincia);
        
        if(listaDireccionesDTO != null) {
            responseDTO = getResponseOk("OK", listaDireccionesDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Actualiza la información de una dirección existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar una dirección en la base de
     * datos. Utiliza el servicio de dirección para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateDireccion(ActionController actionController) {
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

        IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();

        DireccionDTO direccionDTO = direccionServiceImp.getDireccionById(id);

        if(direccionDTO != null) {
            String calle = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_CALLE, "");
            String numero = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_NUMERO, "");
            String strCodigoPostal = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_CODIGO_POSTAL, "");
            String strLocalidad = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_LOCALIDAD, "");
            String strProvincia = actionController.server().getRequestParameter(FormParametros.PARAM_DIRECCION_PROVINCIA, "");

            try {
                Integer cp = (strCodigoPostal.isBlank()? null : Integer.valueOf(strCodigoPostal)); 
                int localidadId = Integer.parseInt(strLocalidad);
                int provinciaId = Integer.parseInt(strProvincia);
                direccionDTO.setCalle(calle);
                direccionDTO.setNumero(numero);
                direccionDTO.setCodigoPostal(cp);

                direccionDTO.setLocalidad(new LocalidadDTO(localidadId, "", new ProvinciaDTO(provinciaId, "")));

                if (direccionServiceImp.updateDireccion(direccionDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, direccionDTO, 0);
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
     * Elimina una dirección por su identificador.
     * 
     * <p>Este método extrae el identificador de la dirección del controlador de acción, utiliza el
     * servicio de dirección para eliminar la dirección correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteDireccion(ActionController actionController) {
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
        

        IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();

        /*
        DireccionDTO direccionDTO = direccionServiceImp.getDireccionById(id);
        
        if(direccionDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, direccionDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        */
        
        if(direccionServiceImp.deleteDireccion(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }    
    
    /**
     * Crea una nueva dirección en la base de datos.
     * 
     * <p>Este método toma los datos de la nueva dirección en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de dirección para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createDireccion(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();
        
        String jsonDireccion = actionController.server().getRequestParameter("direccionJSON", "");
        
        DireccionDTO direccionDTO = null;
        if(jsonDireccion != null && !jsonDireccion.isBlank()) {
            Gson gson = new Gson();
            direccionDTO = gson.fromJson(jsonDireccion, DireccionDTO.class); //Despues de pruebas meter directamente esto dentro de el metodo de createDireccion
            
            if (direccionDTO.getCodigoPostal() == 0) {
                direccionDTO.setCodigoPostal(null);
            }
            
            direccionDTO = direccionServiceImp.createDireccion(direccionDTO);
        }
        
        if(direccionDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, direccionDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
   
}
