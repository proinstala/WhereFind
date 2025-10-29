
package io.proinstala.wherefind.api.articulo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.proinstala.wherefind.api.infraestructure.data.GestorPersistencia;
import io.proinstala.wherefind.api.infraestructure.data.interfaces.IArticuloService;
import io.proinstala.wherefind.shared.consts.Disponibilidad;
import io.proinstala.wherefind.shared.consts.textos.FormParametros;
import io.proinstala.wherefind.shared.consts.textos.LocaleApp;
import io.proinstala.wherefind.shared.controllers.actions.ActionController;
import io.proinstala.wherefind.shared.dtos.ArticuloDTO;
import io.proinstala.wherefind.shared.dtos.ArticuloProveedorDTO;
import io.proinstala.wherefind.shared.dtos.MarcaDTO;
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
public class ArticuloControllerService extends BaseService {
    
    /**
     * Busca artículos según los parámetros de nombre y descripción proporcionados en la solicitud.
     * 
     * <p>Obtiene los parámetros de nombre y descripción de la artículo desde el controlador de acción,
     * utiliza el servicio de artículo para recuperar la lista de artículos que coinciden y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void findArticulos(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();

        List<ArticuloDTO> listaArticuloDTO = null;
        
        String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_NOMBRE, "");
        String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_DESCRIPCION, "");
        String referencia = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_REFERENCIA, "");
        String strIdmarca = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_MARCA, "");
        
        int idMarca = -1;
        try {
            idMarca = Integer.parseInt(strIdmarca);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        

        listaArticuloDTO = articuloServiceImp.findArticulos(nombre, descripcion, referencia, idMarca);

        if(listaArticuloDTO != null) {
            responseDTO = getResponseOk("OK", listaArticuloDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene un artículo por su identificador.
     * 
     * <p>Este método extrae el identificador del artículo del controlador de acción, utiliza el
     * servicio de artículo para recuperar los datos del artículo correspondiente, y devuelve 
     * la respuesta en formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getArticuloById(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();
        
        ArticuloDTO articuloDTO = null;

        int idArticulo = -1;
        try {
            String id = actionController.server().getRequestParameter("idArticulo", "-1");
            idArticulo = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        articuloDTO = articuloServiceImp.getArticuloById(idArticulo);
        
        if(articuloDTO != null) {
            responseDTO = getResponseOk("OK", articuloDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Obtiene la lista de todos los artículos.
     * 
     * <p>Utiliza el servicio de artículo para recuperar la lista completa de artículos y devuelve
     * la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getArticulos(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;

        // Conecta con el Gestor de Persistencia
        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();

        List<ArticuloDTO> listaArticuloDTO = null;
        
        listaArticuloDTO = articuloServiceImp.getAllArticulos();
        
        if (listaArticuloDTO != null) {
            responseDTO = getResponseOk("OK", listaArticuloDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }

        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea una nuevo artículo en la base de datos.
     * 
     * <p>Este método toma los datos del nuevo artículo en formato JSON desde el controlador de acción,
     * los deserializa y los envía al servicio de artículo para su creación. Devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void createArticulo(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        // Conecta con el Gestor de Persistencia
        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();
        
        String jsonArticulo = actionController.server().getRequestParameter("articuloJSON", "");
        
        ArticuloDTO articuloDTO = null;
        if(jsonArticulo != null && !jsonArticulo.isBlank()) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            articuloDTO = gson.fromJson(jsonArticulo, ArticuloDTO.class);
            
            articuloDTO = articuloServiceImp.createArticulo(articuloDTO);
        }
        
        if(articuloDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, articuloDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Actualiza la información de un artículo existente.
     * 
     * <p>Este método verifica los parámetros proporcionados para actualizar un artículo en la base de
     * datos. Utiliza el servicio de artículo para realizar la actualización y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void updateArticulo(ActionController actionController) {
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
        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();

        ArticuloDTO articuloDTO = articuloServiceImp.getArticuloById(id);

        if(articuloDTO != null) {
            String nombre = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_NOMBRE, "");
            String descripcion = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_DESCRIPCION, "");
            String referencia = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_REFERENCIA, "");
            String strIdmarca = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_MARCA, "");
            String modelo = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_MODELO, "");
            String stockMinimo = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_STOCK_MINIMO, "0");
            String imagen = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_IMAGEN, "");

            try {
                int idMarca = Integer.parseInt(strIdmarca);

                articuloDTO.setNombre(nombre);
                articuloDTO.setDescripcion(descripcion);
                articuloDTO.setReferencia(referencia);
                articuloDTO.setMarca(MarcaDTO.builder().id(idMarca).build());
                articuloDTO.setModelo(modelo);
                articuloDTO.setStockMinimo(Integer.parseInt(stockMinimo));
                articuloDTO.setImagen(imagen);

                if (articuloServiceImp.updateArticulo(articuloDTO)) {
                     //Como la acción se ha ejecutado correctamente se crea la respuesta acorde a la misma
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, articuloDTO, 0);
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
     * Elimina un artículo por su identificador.
     * 
     * <p>Este método extrae el identificador del artículo del controlador de acción, utiliza el
     * servicio de artículo para eliminar el artículo correspondiente, y devuelve la respuesta en
     * formato JSON.</p>
     * 
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void deleteArticulo(ActionController actionController) {
        //Respuesta de la acción actual
        ResponseDTO responseDTO;
        
        if (actionController.parametros().length == 1) {
            // Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_FALTAN_PARAMETROS);
            responseJson(actionController.server().response(), responseDTO);
            return;
        } 
            
        // Obtiene el id del artículo desde el parámetro 1 de la lista de parámetros
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
        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();
        
        if(articuloServiceImp.deleteArticulo(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }  
    
    /**
     * Obtiene una asociación artículo-proveedor por su identificador.
     * 
     * <p>Este método extrae el identificador del registro artículo-proveedor desde el controlador de acción,
     * utiliza el servicio de persistencia para obtener sus datos completos (incluyendo la información
     * del artículo y del proveedor) y devuelve la respuesta en formato JSON.</p>
     *
     * @param actionController El controlador de acción que contiene los parámetros de la solicitud.
     */
    public void getArticuloProveedorById(ActionController actionController) {
        ResponseDTO responseDTO;
        
        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();
        
        ArticuloProveedorDTO articuloProveedorDTO = null;

        int idArticuloProveedor = -1;
        try {
            String id = actionController.server().getRequestParameter("idArticuloProveedor", "-1");
            idArticuloProveedor = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        articuloProveedorDTO = articuloServiceImp.getArticuloProveedorById(idArticuloProveedor);
        
        if(articuloProveedorDTO != null) {
            responseDTO = getResponseOk("OK", articuloProveedorDTO, 0);
        } else {
            //Crea la respuesta con un error
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR, new ArrayList<>());
        }
        
        //Devuelve la respuesta al navegador del usuario en formato json
        responseJson(actionController.server().response(), responseDTO);
    }
    
    /**
     * Crea una nueva asociacion proveedor artículo en la base de datos.
     *
     * <p>Este método recibe los datos del proveedor-artículo en formato JSON, los deserializa a un objeto
     * {@link ArticuloProveedorDTO}, y delega la operación al servicio de persistencia. Devuelve la respuesta
     * al cliente en formato JSON.</p>
     *
     * @param actionController el controlador de la acción que contiene los parámetros de la solicitud.
     */
    public void createArticuloProveedor(ActionController actionController) {
        ResponseDTO responseDTO;

        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();

        String jsonProveedor = actionController.server().getRequestParameter("articuloProveedorJSON", "");
        ArticuloProveedorDTO articuloProveedorDTO = null;

        if (jsonProveedor != null && !jsonProveedor.isBlank()) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).registerTypeAdapter(Disponibilidad.class, new DisponibilidadAdapter()).create();
            articuloProveedorDTO = gson.fromJson(jsonProveedor, ArticuloProveedorDTO.class);

            articuloProveedorDTO = articuloServiceImp.createArticuloProveedor(articuloProveedorDTO);
        }

        if (articuloProveedorDTO != null) {
            responseDTO = getResponseOk(LocaleApp.INFO_CREATE_OK, articuloProveedorDTO, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Actualiza la información de una asociacion de proveedor artículo.
     *
     * <p>Obtiene el identificador del proveedor-artículo y los nuevos datos desde la solicitud.
     * Si los parámetros son válidos, actualiza la información mediante el servicio de persistencia.</p>
     *
     * @param actionController el controlador de la acción que contiene los parámetros de la solicitud.
     */
    public void updateArticuloProveedor(ActionController actionController) {
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

        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();
        ArticuloProveedorDTO articuloProveedorDTO = articuloServiceImp.getArticuloProveedorById(id);

        if (articuloProveedorDTO != null) {
            String strPrecio = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_PROVEEDOR_PRECIO, "0");
            String strFechaPrecio = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_PROVEEDOR_FECHA_PRECIO, "");
            String strDisponible = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_PROVEEDOR_DISPONIBILIDAD, "");
            String strFechaNoDisponible = actionController.server().getRequestParameter(FormParametros.PARAM_ARTICULO_PROVEEDOR_FECHA_NO_DISPONIBLE, "");

            try {
                double precio = Double.parseDouble(strPrecio);
                
                LocalDate fechaPrecio = null;
                if(!strFechaPrecio.isBlank()) {
                    fechaPrecio = LocalDate.parse(strFechaPrecio);
                }
                
                
                int disponible = Integer.parseInt(strDisponible);
                Disponibilidad disponibilidad = Disponibilidad.fromValue(disponible > 0);
                
                
                LocalDate fechaNoDisponible = null;
                if(disponibilidad.equals(Disponibilidad.NO_DISPONIBLE) && !strFechaNoDisponible.isBlank()) {
                    fechaNoDisponible = LocalDate.parse(strFechaNoDisponible);
                }
                
                articuloProveedorDTO.setPrecio(precio);
                articuloProveedorDTO.setFechaPrecio(fechaPrecio);
                articuloProveedorDTO.setDisponible(disponibilidad);
                articuloProveedorDTO.setFechaNoDisponible(fechaNoDisponible);

                if (articuloServiceImp.updateArticuloProveedor(articuloProveedorDTO)) {
                    responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, articuloProveedorDTO, 0);
                } else {
                    responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
                }

            } catch (NumberFormatException e) {
                responseDTO = getResponseError(LocaleApp.ERROR_PARAMETRO_NO_CORRECTO);
            }
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_NO_EXISTE_ELEMENTO);
        }

        responseJson(actionController.server().response(), responseDTO);
    }

    /**
     * Elimina una asociacion de proveedor artículo.
     *
     * <p>Este método obtiene el identificador del registro proveedor-artículo y solicita su eliminación
     * a través del servicio de persistencia. Devuelve una respuesta indicando el resultado de la operación.</p>
     *
     * @param actionController el controlador de la acción que maneja la solicitud.
     */
    public void deleteArticuloProveedor(ActionController actionController) {
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

        IArticuloService articuloServiceImp = GestorPersistencia.getArticuloService();

        if (articuloServiceImp.deleteArticuloProveedor(id)) {
            responseDTO = getResponseOk(LocaleApp.INFO_UPDATE_OK, id, 0);
        } else {
            responseDTO = getResponseError(LocaleApp.ERROR_SE_HA_PRODUCIDO_UN_ERROR);
        }

        responseJson(actionController.server().response(), responseDTO);
    }

    
}
