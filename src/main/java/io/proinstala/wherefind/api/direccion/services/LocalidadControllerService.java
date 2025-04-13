
package io.proinstala.wherefind.api.direccion.services;

import com.google.gson.Gson;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.ILocalidadService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja operaciones relacionadas con localidades.
 *
 * Esta clase se encarga de gestionar las solicitudes sobre localidades y construir 
 * las respuestas adecuadas para dichas solicitudes. Extiende {@link BaseService} y utiliza servicios 
 * específicos para interactuar con la capa de persistencia.
 */
public class LocalidadControllerService extends BaseService {
    
    /**
     * Obtiene una localidad por su identificador.
     * 
     * <p>Este método extrae el identificador de la localidad del controlador de acción, utiliza el
     * servicio de localidad para recuperar los datos de la localidad correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getLocalidadById(ActionController actionController) {
        ResponseDTO responseDTO;
        LocalidadDTO localidadDTO;
        
        // Conecta con el Gestor de Persistencia
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();
        
        int idLocalidad = -1;
        try {
            String id = actionController.server().getRequestParameter("idLocalidad", "-1");
            idLocalidad = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        localidadDTO = localidadServiceImp.getLocalidadById(idLocalidad);
        
        if (localidadDTO != null) {
            responseDTO = getResponseOk("OK", localidadDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Maneja la solicitud para obtener una lista de localidades.
     *
     * <p>Este método procesa la solicitud del cliente para obtener localidades. Dependiendo de los 
     * parámetros recibidos, puede devolver todas las localidades o solo las localidades de una 
     * provincia específica. Si se proporciona un JSON con los datos de la provincia, se filtrarán 
     * las localidades correspondientes a esa provincia. En caso contrario, se devolverán todas 
     * las localidades disponibles.</p>
     *
     * <p>La respuesta se construye y se envía al cliente en formato JSON utilizando {@link ActionController}.</p>
     *
     * @param actionController el controlador de la acción actual que maneja la solicitud y la respuesta.
     */
    public void getLocalidades(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();

        List<LocalidadDTO> listaLocalidadesDTO = null;


        String jsonProvincia = actionController.server().getRequestParameter("jsonProvincia", "");

        if(jsonProvincia != null && !jsonProvincia.isBlank()) {
            Gson gson = new Gson();
            ProvinciaDTO provinciaDTO = gson.fromJson(jsonProvincia, ProvinciaDTO.class);

            //Obtiene la lista de Provincias
            listaLocalidadesDTO = localidadServiceImp.getLocalidadesOfProvincia(provinciaDTO);
        } else {
            //Obtiene la lista de Provincias
            listaLocalidadesDTO = localidadServiceImp.getAllLocalidades();
        }
        
        
        if (listaLocalidadesDTO != null) {
            responseDTO = getResponseOk("OK", listaLocalidadesDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Busca localidades según el nombre proporcionado y devuelve la respuesta en formato JSON.
     *
     * <p>Este método obtiene el parámetro de búsqueda desde la solicitud HTTP y lo utiliza para 
     * recuperar una lista de localidades desde el servicio de persistencia. Si la búsqueda tiene 
     * éxito, devuelve una respuesta con la lista de localidades encontradas. En caso contrario, 
     * se envía una respuesta de error.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud y respuesta.
     */
    public void findLocalidades(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_LOCALIDAD_NOMBRE, "");
        String provincia = actionController.server().getRequestParameter(FormParametros.PARAM_LOCALIDAD_PROVINCIA, "");
        
        int idProvincia = -1;
        try {
            idProvincia = Integer.parseInt(provincia);
        } catch (NumberFormatException e) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
        } catch (Exception e) {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        List<LocalidadDTO> listaLocalidadesDTO = localidadServiceImp.findLocalidades(nombre, idProvincia);
        
        if(listaLocalidadesDTO != null) {
            responseDTO = getResponseOk("OK", listaLocalidadesDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea una nueva localidad en la base de datos.
     * 
     * <p>Este método toma los datos de la nueva localidad en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de localidad para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createLocalidad(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();
        
        String jsonLocalidad = actionController.server().getRequestParameter("localidadJSON", "");
        
        LocalidadDTO localidadDTO = null;
        if(jsonLocalidad != null && !jsonLocalidad.isBlank()) {
            Gson gson = new Gson();
            localidadDTO = gson.fromJson(jsonLocalidad, LocalidadDTO.class);
            
            localidadDTO = localidadServiceImp.createLocalidad(localidadDTO);
        }
        
        if(localidadDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, localidadDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Elimina una localidad por su identificador.
     * 
     * <p>Este método extrae el identificador de la localidad del controlador de acción, utiliza el
     * servicio de localidad para eliminar la localidad correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteLocalidad(ActionController actionController) {
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
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();
        
        if(localidadServiceImp.deleteLocalidad(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
    
    /**
     * Actualiza la información de una localidad existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar una localidad en la base de
     * datos. Utiliza el servicio de localidad para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateLocalidad(ActionController actionController) {
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

        // Conecta con el Gestor de Persistencia
        ILocalidadService localidadServiceImp = GestorPersistencia.getLocalidadService();

        LocalidadDTO localidadDTO = localidadServiceImp.getLocalidadById(id);

        if(localidadDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_LOCALIDAD_NOMBRE, "");
            String strProvincia = actionController.server().getRequestParameter(FormParametros.PARAM_LOCALIDAD_PROVINCIA, "");

            try {
                int provinciaId = Integer.parseInt(strProvincia);
                localidadDTO.setNombre(nombre);

                localidadDTO.setProvincia(new ProvinciaDTO(provinciaId, ""));

                if (localidadServiceImp.updateLocalidad(localidadDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, localidadDTO, 0);
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
