
package io.proinstala.wherefind.api.articulo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IMarcaService;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.MarcaDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import io.proinstala.wherefind.shared.tools.LocalDateAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class MarcaControllerService extends BaseService {
    
    /**
     * Busca almacenes según los parámetros de nombre y descripción proporcionados en la solicitud.
     * 
     * <p>Obtiene los parámetros de nombre y descripción del almacén desde el controlador de acción,
     * utiliza el servicio de almacén para recuperar la lista de almacenes que coinciden y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void findMarcas(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        IMarcaService marcaServiceImp = GestorPersistencia.getMarcaService();

        List<MarcaDTO> listaMarcaDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_MARCA_NOMBRE, "");
        String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_MARCA_DESCRIPCION, "");

        listaMarcaDTO = marcaServiceImp.findMarcas(nombre, descripcion);

        if(listaMarcaDTO != null) {
            responseDTO = getResponseOk("OK", listaMarcaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene una marca por su identificador.
     * 
     * <p>Este método extrae el identificador de la marca del controlador de acción, utiliza el
     * servicio de marca para recuperar los datos de la marca correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getMarcaById(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IMarcaService marcaServiceImp = GestorPersistencia.getMarcaService();
        
        MarcaDTO marcaDTO = null;

        int idMarca = -1;
        try {
            String id = actionController.server().getRequestParameter("idMarca", "-1");
            idMarca = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        marcaDTO = marcaServiceImp.getMarcaById(idMarca);
        
        if(marcaDTO != null) {
            responseDTO = getResponseOk("OK", marcaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene la lista de todos las marcas.
     * 
     * <p>Utiliza el servicio de marca para recuperar la lista completa de marcas y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getMarcas(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IMarcaService marcaServiceImp = GestorPersistencia.getMarcaService();

        List<MarcaDTO> listaMarcaDTO = null;
        
        listaMarcaDTO = marcaServiceImp.getAllMarcas();
        
        if (listaMarcaDTO != null) {
            responseDTO = getResponseOk("OK", listaMarcaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea una nueva marca en la base de datos.
     * 
     * <p>Este método toma los datos de la nueva marca en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de marca para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createMarca(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IMarcaService marcaServiceImp = GestorPersistencia.getMarcaService();
        
        String jsonMarca = actionController.server().getRequestParameter("marcaJSON", "");
        
        MarcaDTO marcaDTO = null;
        if(jsonMarca != null && !jsonMarca.isBlank()) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            marcaDTO = gson.fromJson(jsonMarca, MarcaDTO.class);
            
            marcaDTO = marcaServiceImp.createMarca(marcaDTO);
        }
        
        if(marcaDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, marcaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Actualiza la información de una marca existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar una marca en la base de
     * datos. Utiliza el servicio de marca para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateMarca(ActionController actionController) {
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
        IMarcaService marcaServiceImp = GestorPersistencia.getMarcaService();

        MarcaDTO marcaDTO = marcaServiceImp.getMarcaById(id);

        if(marcaDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_MARCA_NOMBRE, "");
            String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_MARCA_DESCRIPCION, "");

            try {
                marcaDTO.setNombre(nombre);
                marcaDTO.setDescripcion(descripcion);

                if (marcaServiceImp.updateMarca(marcaDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, marcaDTO, 0);
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
     * Elimina una marca por su identificador.
     * 
     * <p>Este método extrae el identificador de la marca del controlador de acción, utiliza el
     * servicio de marca para eliminar la marca correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteMarca(ActionController actionController) {
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
        IMarcaService marcaServiceImp = GestorPersistencia.getMarcaService();
        
        if(marcaServiceImp.deleteMarca(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
    
}
