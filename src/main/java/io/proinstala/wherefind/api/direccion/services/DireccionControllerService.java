
package io.proinstala.wherefind.api.direccion.services;

import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IDireccionService;
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
 *
 * @author David
 */
public class DireccionControllerService extends BaseService{
    
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
    
    public void findDirecciones(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();
        
        List<DireccionDTO> listaDireccionesDTO = null;
        
        String calle = actionController.server().getRequestParameter("calle", "");
        String strIdLocalidad = actionController.server().getRequestParameter("localidad", "");
        String strIdProvincia = actionController.server().getRequestParameter("provincia", "");
        
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
    
    public void updateDireccion(ActionController actionController) {
        // Respuesta de la acción actual
        ResponseDTO responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
        } else {
            // Obtiene el id del usuario desde el parámetro 1 de la lista de parámetros
            int id = actionController.getIntFromParametros(1);

            // Si el id es mayor que -1 significa que hay en principio un id válido que se puede procesar
            // En caso de ser igual a -1 significa que el parámetro introducido no es correcto
            if (id == -1) {
                // Crea la respuesta con un error
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            } else {
                IDireccionService direccionServiceImp = GestorPersistencia.getDireccionService();
                
                DireccionDTO direccionDTO = direccionServiceImp.getDireccionById(id);
                
                if(direccionDTO != null) {
                    String calle = actionController.server().getRequestParameter("calle", "");
                    String numero = actionController.server().getRequestParameter("numero", "");
                    String strCodigoPostal = actionController.server().getRequestParameter("codigoPostal", "");
                    String strLocalidad = actionController.server().getRequestParameter("localidad", "");
                    String strProvincia = actionController.server().getRequestParameter("provincia", "");
                    
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
                            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_USER, direccionDTO, 0);
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
            }
        }
        responseJson(actionController.server().response(), responseDTO);
    }
    
}
