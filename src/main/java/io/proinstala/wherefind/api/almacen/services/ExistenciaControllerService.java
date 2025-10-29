
package io.proinstala.wherefind.api.almacen.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IExistenciaService;
import io.proinstala.wherefind.shared.consts.Disponibilidad;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ArticuloDTO;
import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import io.proinstala.wherefind.shared.dtos.ExistenciaDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.ResponseDTO;
import io.proinstala.wherefind.shared.services.BaseService;
import io.proinstala.wherefind.shared.tools.DisponibilidadAdapter;
import io.proinstala.wherefind.shared.tools.LocalDateAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class ExistenciaControllerService extends BaseService {
    
    /**
     * Busca existencias según los parámetros de nombre articulo, referencia, marca, proveedor, almacen y disponible proporcionados en la solicitud.
     * 
     * <p>Obtiene los parámetros de nombre articulo, referencia, marca, proveedor, almacen y disponible de la existencia desde el controlador de acción,
     * utiliza el servicio de existencia para recuperar la lista de existencias que coinciden y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void findExistencias(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        IExistenciaService existenciaServiceImp = GestorPersistencia.getExistenciaService();

        List<ExistenciaDTO> listaExistenciaDTO = null;
        
        String articulo = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_ARTICULO, "");
        String referencia = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_REFERENCIA, "");
        String sku = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_SKU, "");
        String strIdMarca = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_MARCA, "");
        String strIdAlmacen = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_ALMACEN, "");
        String nombreEmplazamiento = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_NOMBRE_EPLAZAMIENTO, "");
        String strDisponible = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_DISPONIBILIDAD, "");
        
        int idMarca = -1;
        int idAlmacen = -1;
        int disponible = -1;
        try {
            idMarca = Integer.parseInt(strIdMarca);
            idAlmacen = Integer.parseInt(strIdAlmacen);
            disponible = Integer.parseInt(strDisponible);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        listaExistenciaDTO = existenciaServiceImp.findExistencias(articulo, referencia, sku, idMarca, idAlmacen, nombreEmplazamiento,  disponible);

        if(listaExistenciaDTO != null) {
            responseDTO = getResponseOk("OK", listaExistenciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Busca todas las existencias asociadas a un artículo específico.
     * 
     * <p>Este método obtiene el identificador del artículo desde los parámetros
     * de la solicitud proporcionados por el {@link ActionController}, y utiliza
     * el servicio de existencias para recuperar todas las existencias vinculadas
     * a dicho artículo.  
     * 
     * <p>Los resultados se devuelven al cliente en formato JSON, conteniendo una
     * lista de objetos {@link ExistenciaDTO}.  
     * En caso de que no existan registros o se produzca un error durante la búsqueda,
     * se devuelve una respuesta de error estandarizada.</p>
     * 
     * @param actionController el controlador de acción que contiene los parámetros de la solicitud.
     * 
     * @see IExistenciaService#findExistenciasByArticulo(int)
     * @see ExistenciaDTO
     */
    public void findExistenciasByArticulo(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        IExistenciaService existenciaServiceImp = GestorPersistencia.getExistenciaService();

        List<ExistenciaDTO> listaExistenciaDTO = null;
        
        String strIdArticulo = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_ARTICULO_ID, "");
        
        int idArticulo = -1;
        try {
            idArticulo = Integer.parseInt(strIdArticulo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        listaExistenciaDTO = existenciaServiceImp.findExistenciasByArticulo(idArticulo);

        if(listaExistenciaDTO != null) {
            responseDTO = getResponseOk("OK", listaExistenciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene una existencia por su identificador.
     * 
     * <p>Este método extrae el identificador de la existencia del controlador de acción, utiliza el
     * servicio de existencia para recuperar los datos de la existencia correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getExistenciaById(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IExistenciaService existenciaServiceImp = GestorPersistencia.getExistenciaService();
        
        ExistenciaDTO existenciaDTO = null;

        int idExistencia = -1;
        try {
            String id = actionController.server().getRequestParameter("idExistencia", "-1");
            idExistencia = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        existenciaDTO = existenciaServiceImp.getExistenciaById(idExistencia);
        
        if(existenciaDTO != null) {
            responseDTO = getResponseOk("OK", existenciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Crea una nuevo existencia en la base de datos.
     * 
     * <p>Este método toma los datos de la nueva existencia en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de existencia para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createExistencia(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IExistenciaService existenciaServiceImp = GestorPersistencia.getExistenciaService();
        
        String jsonExistencia = actionController.server().getRequestParameter("existenciaJSON", "");
        
        ExistenciaDTO existenciaDTO = null;
        if(jsonExistencia != null && !jsonExistencia.isBlank()) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).registerTypeAdapter(Disponibilidad.class, new DisponibilidadAdapter()).create();
            existenciaDTO = gson.fromJson(jsonExistencia, ExistenciaDTO.class);
            
            existenciaDTO = existenciaServiceImp.createExistencia(existenciaDTO);
        }
        
        if(existenciaDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, existenciaDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    
    /**
     * Elimina una existencia por su identificador.
     * 
     * <p>Este método extrae el identificador de la existencia del controlador de acción, utiliza el
     * servicio de existencia para eliminar la existencia correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteExistencia(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Comprueba que hay más de 1 parámetro
        if (actionController.parametros().length <= 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id de la existencia desde el parámetro 1 de la lista de parámetros
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
        IExistenciaService existenciaServiceImp = GestorPersistencia.getExistenciaService();
        
        if(existenciaServiceImp.deleteExistencia(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
    
    /**
     * Actualiza la información de una existencia existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar una existencia en la base de
     * datos. Utiliza el servicio de existencia para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateExistencia(ActionController actionController) {
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
        IExistenciaService existenciaServiceImp = GestorPersistencia.getExistenciaService();

        ExistenciaDTO existenciaDTO = existenciaServiceImp.getExistenciaById(id);

        if(existenciaDTO != null) {
            String sku = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_SKU, "");
            String comprador = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_COMPRADOR, "");
            String anotacion = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_ANOTACION, "");
            
            String strPrecio = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_PRECIO, "");
            String strDisponible = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_DISPONIBILIDAD, "");
            String strFechaCompra = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_FECHA_COMPRA, "");
            String strFechaNoDisponible = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_FECHA_NO_DISPONIBLE, "");
            
            String strIdArticulo = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_ARTICULO, "");
            String strIdEmplazmiento = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_EMPLAZAMIENTO, "");
            String strIdProveedor = actionController.server().getRequestParameter(FormParametros.PARAM_EXISTENCIA_ARTICULO_PROVEEDOR, ""); //es el id de proveedor.

            try {
                int idArticulo = Integer.parseInt(strIdArticulo);
                int idEmplazamiento = Integer.parseInt(strIdEmplazmiento);
                int idProveedor = Integer.parseInt(strIdProveedor);
                
                double precio = Double.parseDouble(strPrecio);
                
                LocalDate fechaCompra = LocalDate.parse(strFechaCompra);
                
                int disponible = Integer.parseInt(strDisponible);
                Disponibilidad disponibilidad = Disponibilidad.fromValue(disponible > 0);
                
                
                LocalDate fechaNoDisponible = null;
                if(disponibilidad.equals(Disponibilidad.NO_DISPONIBLE) && !strFechaNoDisponible.isBlank()) {
                    fechaNoDisponible = LocalDate.parse(strFechaNoDisponible);
                }
                
                
                ProveedorDTO proveedorDTO = null;
                if(idProveedor > 0) {
                    proveedorDTO = ProveedorDTO.builder().id(idProveedor).build();
                } 
                
                existenciaDTO.setArticulo(ArticuloDTO.builder().id(idArticulo).build());
                existenciaDTO.setProveedor(proveedorDTO);
                existenciaDTO.setSku(sku);
                existenciaDTO.setEmplazamiento(EmplazamientoDTO.builder().id(idEmplazamiento).build());
                existenciaDTO.setPrecio(precio);
                existenciaDTO.setFechaCompra(fechaCompra);
                existenciaDTO.setComprador(comprador);
                existenciaDTO.setDisponible(disponibilidad);
                existenciaDTO.setFechaNoDisponible(fechaNoDisponible);
                existenciaDTO.setAnotacion(anotacion);

                if (existenciaServiceImp.updateExistencia(existenciaDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, existenciaDTO, 0);
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
