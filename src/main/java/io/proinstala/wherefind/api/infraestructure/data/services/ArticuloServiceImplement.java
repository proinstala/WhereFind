
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IArticuloService;
import io.proinstala.wherefind.shared.consts.Disponibilidad;
import io.proinstala.wherefind.shared.dtos.ArticuloDTO;
import io.proinstala.wherefind.shared.dtos.ArticuloProveedorDTO;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.MarcaDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class ArticuloServiceImplement extends BaseMySql implements IArticuloService {
    
    private static final String SQL_SELECT_COMUN = 
            """
            SELECT
             art.*,
             m.id AS m_id, m.nombre AS m_nombre, m.descripcion AS m_descripcion, m.imagen AS m_imagen, m.activo AS m_activo,
             COUNT(e.id) AS art_stock_actual
            FROM ARTICULO art
            INNER JOIN MARCA m ON art.marca_id = m.id
            LEFT JOIN EXISTENCIA e ON art.id = e.articulo_id AND e.disponible = true
            """;
    
    private static final String SQL_SELECT_ARTICULO_PROVEEDOR_BY_ARTICULO = 
            """
            SELECT
            art_pro.*,
            p.nombre AS proveedor_nombre, p.descripcion AS proveedor_descripcion,
            p.pagina_web AS proveedor_pagina_web, p.imagen AS proveedor_imagen, 
            p.activo AS proveedor_activo,
            d.id AS d_id, d.calle AS d_calle, d.numero AS d_numero, d.codigo_postal AS d_codigo_postal, d.localidad_id AS d_localidad_id, d.activo AS d_activo,
            l.id AS l_id, l.nombre AS l_nombre, l.provincia_id AS l_provincia_id,
            pr.id AS pr_id, pr.nombre AS pr_nombre
            FROM  ARTICULO_PROVEEDOR art_pro
            INNER JOIN PROVEEDOR p ON art_pro.proveedor_id = p.id
            LEFT JOIN DIRECCION d ON p.direccion_id = d.id
            LEFT JOIN LOCALIDAD l ON d.localidad_id = l.id
            LEFT JOIN PROVINCIA pr ON l.provincia_id = pr.id
            WHERE art_pro.articulo_id = ?
            """;
    
   
    private static final String SQL_SELECT_ARTICULO_PROVEEDOR_BY_ID =
        """
        SELECT
            art_pro.*,
            p.nombre AS proveedor_nombre, p.descripcion AS proveedor_descripcion,
            p.pagina_web AS proveedor_pagina_web, p.imagen AS proveedor_imagen, 
            p.activo AS proveedor_activo,
            d.id AS d_id, d.calle AS d_calle, d.numero AS d_numero, d.codigo_postal AS d_codigo_postal, d.localidad_id AS d_localidad_id, d.activo AS d_activo,
            l.id AS l_id, l.nombre AS l_nombre, l.provincia_id AS l_provincia_id,
            pr.id AS pr_id, pr.nombre AS pr_nombre,

            art.*,
            m.id AS m_id, m.nombre AS m_nombre, m.descripcion AS m_descripcion, 
            m.imagen AS m_imagen, m.activo AS m_activo,
            COUNT(e.id) AS art_stock_actual

        FROM ARTICULO_PROVEEDOR art_pro
        INNER JOIN PROVEEDOR p ON art_pro.proveedor_id = p.id
        LEFT JOIN DIRECCION d ON p.direccion_id = d.id
        LEFT JOIN LOCALIDAD l ON d.localidad_id = l.id
        LEFT JOIN PROVINCIA pr ON l.provincia_id = pr.id
        INNER JOIN ARTICULO art ON art_pro.articulo_id = art.id
        INNER JOIN MARCA m ON art.marca_id = m.id
        LEFT JOIN EXISTENCIA e ON art.id = e.articulo_id AND e.disponible = true
        WHERE art_pro.id = ?
        GROUP BY art_pro.id;
        """;
   
    
    private static final String SQL_UPDATE_ARTICULO = 
        "UPDATE ARTICULO SET " +
        "nombre = ?, descripcion = ?, referencia = ?, marca_id = ?, modelo = ?, stock_minimo = ?, imagen = ? " +
        "WHERE id = ?;";

    private static final String SQL_CREATE_ARTICULO = 
        "INSERT INTO ARTICULO (nombre, descripcion, referencia, marca_id, modelo, stock_minimo, imagen) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?);";
    
    private static final String SQL_DELETE_ARTICULO = 
        "UPDATE ARTICULO SET activo = FALSE WHERE id = ?;";
    
    private static final String SQL_CREATE_ARTICULO_PROVEEDOR = 
        "INSERT INTO ARTICULO_PROVEEDOR (articulo_id, proveedor_id, precio, fecha_precio, disponible, fecha_no_disponible) " +
        "VALUES (?, ?, ?, ?, ?, ?);";
    
    private static final String SQL_UPDATE_ARTICULO_PROVEEDOR = 
        "UPDATE ARTICULO_PROVEEDOR SET articulo_id = ?, proveedor_id = ?, precio = ?, fecha_precio = ?, disponible = ?, fecha_no_disponible = ? " +
        "WHERE id = ?;";
    
    private static final String SQL_DELETE_ARTICULO_PROVEEDOR = 
        "DELETE FROM ARTICULO_PROVEEDOR WHERE id = ?;";
    
    
    private static ArticuloProveedorDTO getArticuloProveedorFromResultSet(ResultSet rs) throws SQLException {

        ProveedorDTO proveedorDTO = ProveedorDTO.builder()
                .id(rs.getInt("art_pro.proveedor_id"))
                .nombre(rs.getString("proveedor_nombre"))
                .descripcion(rs.getString("proveedor_descripcion"))
                .paginaWeb(rs.getString("proveedor_pagina_web"))
                .imagen(rs.getString("proveedor_imagen"))
                .activo(rs.getBoolean("proveedor_activo"))
                .build();

        ProvinciaDTO provinciaDTO = ProvinciaDTO.builder()
                .id(rs.getInt("pr_id"))
                .nombre(rs.getString("pr_nombre"))
                .build();

        LocalidadDTO localidadDTO = LocalidadDTO.builder()
                .id(rs.getInt("l_id"))
                .nombre(rs.getString("l_nombre"))
                .provincia(provinciaDTO)
                .build();

        DireccionDTO direccionDTO = DireccionDTO.builder()
                .id(rs.getInt("d_id"))
                .calle(rs.getString("d_calle"))
                .numero(rs.getString("d_numero"))
                .codigoPostal(rs.getInt("d_codigo_postal"))
                .localidad(localidadDTO)
                .activo(rs.getBoolean("d_activo"))
                .build();

        proveedorDTO.setDireccion(direccionDTO);
        
        ArticuloProveedorDTO articuloProveedorDTO = ArticuloProveedorDTO.builder()
                .id(rs.getInt("art_pro.id"))
                .proveedor(proveedorDTO)
                .precio(rs.getDouble("art_pro.precio"))
                .fechaPrecio(rs.getObject("art_pro.fecha_precio", LocalDate.class))
                .disponible(Disponibilidad.fromValue(rs.getBoolean("art_pro.disponible")))
                .fechaNoDisponible(rs.getObject("art_pro.fecha_no_disponible", LocalDate.class))
                .build();
        
        return articuloProveedorDTO;
    }
    
    
    private static ArticuloDTO getArticuloFromResultSet(ResultSet rs) throws SQLException {
        MarcaDTO marcaDTO = MarcaDTO.builder()
                .id(rs.getInt("art.marca_id"))
                .nombre(rs.getString("m_nombre"))
                .descripcion(rs.getString("m_descripcion"))
                .imagen(rs.getString("m_imagen"))
                .activo(rs.getBoolean("m_activo"))
                .build();
                

        ArticuloDTO articuloDTO = ArticuloDTO.builder()
                .id(rs.getInt("art.id"))
                .nombre(rs.getString("art.nombre"))
                .descripcion(rs.getString("art.descripcion"))
                .referencia(rs.getString("art.referencia"))
                .marca(marcaDTO)
                .modelo(rs.getString("art.modelo"))
                .stockMinimo(rs.getInt("art.stock_minimo"))
                .imagen(rs.getString("art.imagen"))

                .activo(rs.getBoolean("art.activo"))
                
                .stockActual(rs.getInt("art_stock_actual"))
                .build();

        return articuloDTO;
    }
    
    private List<ArticuloProveedorDTO> getArticuloProveedorByArticulo(Connection conexion , int articulo_id) {
        List<ArticuloProveedorDTO> listaArticuloProveedor = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ARTICULO_PROVEEDOR_BY_ARTICULO)) {

            ps.setInt(1, articulo_id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaArticuloProveedor.add(getArticuloProveedorFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaArticuloProveedor;
    }
    

    @Override
    public ArticuloDTO getArticuloById(int idArticulo) {
        ArticuloDTO articuloDTO = null;
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE art.id = ?");
        sql.append(" GROUP BY art.id");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idArticulo);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    articuloDTO = getArticuloFromResultSet(resultSet);
                    
                }
            }

            if (articuloDTO != null) {
                articuloDTO.setListaProveedores(getArticuloProveedorByArticulo(conexion, articuloDTO.getId()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return articuloDTO;
    }

    @Override
    public List<ArticuloDTO> getAllArticulos() {
        List<ArticuloDTO> listaArticulos = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE art.activo = TRUE");
        sql.append(" GROUP BY art.id");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    ArticuloDTO articuloDTO = getArticuloFromResultSet(resultSet);
                    listaArticulos.add(articuloDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaArticulos;
    }

    @Override
    public List<ArticuloDTO> findArticulos(String nombre, String descripcion, String referencia, int marcaId) {
        List<ArticuloDTO> listaArticulos = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE art.activo = TRUE");
        
        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND art.nombre LIKE ?");
        }
        
        // Condición por descripcion
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND art.descripcion LIKE ?");
        }
        
        // Condición por referencia
        if (referencia != null && !referencia.trim().isEmpty()) {
            sql.append(" AND art.referencia LIKE ?");
        }
        
        // Condición por marcaId
        if (marcaId != -1) {
            sql.append(" AND art.marca_id = ?");
        }
        
        sql.append(" GROUP BY art.id");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            int index = 1;
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                ps.setString(index++, "%" + nombre + "%");
            }

            if (descripcion != null && !descripcion.trim().isEmpty()) {
                ps.setString(index++, "%" + descripcion + "%");
            }
            
            if (referencia != null && !referencia.trim().isEmpty()) {
                ps.setString(index++, "%" + referencia + "%");
            }
            
            if (marcaId != -1) {
                ps.setInt(index++, marcaId);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    ArticuloDTO articuloDTO = getArticuloFromResultSet(resultSet);
                    listaArticulos.add(articuloDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaArticulos;
    }

    @Override
    public ArticuloDTO createArticulo(ArticuloDTO articuloDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_ARTICULO, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, articuloDTO.getNombre());
            ps.setString(2, articuloDTO.getDescripcion());
            ps.setString(3, articuloDTO.getReferencia());
            ps.setInt(4, articuloDTO.getMarca().getId());
            ps.setString(5, articuloDTO.getModelo());
            ps.setInt(6, articuloDTO.getStockMinimo());
            ps.setString(7, articuloDTO.getImagen());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        articuloDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return articuloDTO;
    }

    @Override
    public boolean updateArticulo(ArticuloDTO articuloDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_ARTICULO)) {

            ps.setString(1, articuloDTO.getNombre());
            ps.setString(2, articuloDTO.getDescripcion());
            ps.setString(3, articuloDTO.getReferencia());
            ps.setInt(4, articuloDTO.getMarca().getId());
            ps.setString(5, articuloDTO.getModelo());
            ps.setInt(6, articuloDTO.getStockMinimo());
            ps.setString(7, articuloDTO.getImagen());
            ps.setInt(8, articuloDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteArticulo(int articuloId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_ARTICULO)) {

            ps.setInt(1, articuloId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
    
    @Override
    public ArticuloProveedorDTO getArticuloProveedorById(int articuloProveedorId) {
        ArticuloProveedorDTO articuloProveedorDTO = null;

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ARTICULO_PROVEEDOR_BY_ID)) {

            ps.setInt(1, articuloProveedorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    articuloProveedorDTO = getArticuloProveedorFromResultSet(rs);
                    ArticuloDTO articuloDTO = getArticuloFromResultSet(rs);
                    articuloProveedorDTO.setArticulo(articuloDTO);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return articuloProveedorDTO;
    }
    
    
    @Override
    public ArticuloProveedorDTO createArticuloProveedor(ArticuloProveedorDTO articuloProveedorDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_ARTICULO_PROVEEDOR, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, articuloProveedorDTO.getArticulo().getId());
            ps.setInt(2, articuloProveedorDTO.getProveedor().getId());
            ps.setDouble(3, articuloProveedorDTO.getPrecio());
            ps.setObject(4, articuloProveedorDTO.getFechaPrecio());
            ps.setBoolean(5, articuloProveedorDTO.getDisponible().getValor());
            if (articuloProveedorDTO.getFechaNoDisponible() != null) {
                ps.setDate(6, java.sql.Date.valueOf(articuloProveedorDTO.getFechaNoDisponible()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        articuloProveedorDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return articuloProveedorDTO;
    }

    @Override
    public boolean updateArticuloProveedor(ArticuloProveedorDTO articuloProveedorDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_ARTICULO_PROVEEDOR)) {

            ps.setInt(1, articuloProveedorDTO.getArticulo().getId());
            ps.setInt(2, articuloProveedorDTO.getProveedor().getId());
            ps.setDouble(3, articuloProveedorDTO.getPrecio());
            ps.setObject(4, articuloProveedorDTO.getFechaPrecio());
            ps.setBoolean(5, articuloProveedorDTO.getDisponible().getValor());
            if (articuloProveedorDTO.getFechaNoDisponible() != null) {
                ps.setDate(6, java.sql.Date.valueOf(articuloProveedorDTO.getFechaNoDisponible()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.setInt(7, articuloProveedorDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteArticuloProveedor(int articuloProveedorId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_ARTICULO_PROVEEDOR)) {

            ps.setInt(1, articuloProveedorId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
    
}
