
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IExistenciaService;
import io.proinstala.wherefind.shared.consts.Disponibilidad;
import io.proinstala.wherefind.shared.dtos.AlmacenDTO;
import io.proinstala.wherefind.shared.dtos.ArticuloDTO;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.EmplazamientoDTO;
import io.proinstala.wherefind.shared.dtos.ExistenciaDTO;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.MarcaDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.TipoEmplazamientoDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class ExistenciaServiceImplement extends BaseMySql implements IExistenciaService {
    
    private static final String SQL_SELECT_COMUN = 
    """
    SELECT 
      exi.id AS exi_id,
      exi.articulo_id AS exi_articulo_id,
      exi.proveedor_id AS exi_proveedor_id,
      exi.sku AS exi_sku,
      exi.emplazamiento_id AS exi_emplazamiento_id,
      exi.precio AS exi_precio,
      exi.fecha_compra AS exi_fecha_compra,
      exi.comprador AS exi_comprador,
      exi.disponible AS exi_disponible,
      exi.fecha_no_disponible AS exi_fecha_no_disponible,
      exi.anotacion AS exi_anotacion,
      
      art.id AS art_id,
      art.nombre AS art_nombre,
      art.descripcion AS art_descripcion,
      art.referencia AS art_referencia,
      art.marca_id AS art_marca_id,
      art.modelo AS art_modelo,
      art.stock_minimo AS art_stock_minimo,
      art.imagen AS art_imagen,
      art.activo AS art_activo,

      m.id AS m_id,
      m.nombre AS m_nombre,
      m.descripcion AS m_descripcion,
      m.imagen AS m_imagen,
      m.activo AS m_activo,

      p.id AS p_id,
      p.nombre AS proveedor_nombre,
      p.descripcion AS proveedor_descripcion,
      p.pagina_web AS proveedor_pagina_web,
      p.imagen AS proveedor_imagen, 
      p.activo AS proveedor_activo,
      p.direccion_id AS p_direccion_id,

      empla.id AS empla_id,
      empla.nombre AS empla_nombre,
      empla.descripcion AS empla_descripcion,
      empla.tipo_id AS empla_tipo_id,
      empla.almacen_id AS empla_almacen_id,

      t.id AS t_id,
      t.nombre AS t_nombre,

      a.id AS a_id,
      a.nombre AS a_nombre,
      a.descripcion AS a_descripcion,
      a.direccion_id AS a_direccion_id,
      a.activo AS a_activo,

      d.id AS d_id,
      d.calle AS d_calle,
      d.numero AS d_numero,
      d.codigo_postal AS d_codigo_postal,
      d.localidad_id AS d_localidad_id,
      d.activo AS d_activo,

      l.id AS l_id,
      l.nombre AS l_nombre,
      l.provincia_id AS l_provincia_id,

      pr.id AS pr_id,
      pr.nombre AS pr_nombre

    FROM EXISTENCIA exi
    INNER JOIN ARTICULO art ON exi.articulo_id = art.id
    LEFT JOIN MARCA m ON art.marca_id = m.id
    LEFT JOIN PROVEEDOR p ON exi.proveedor_id = p.id
    INNER JOIN EMPLAZAMIENTO empla ON exi.emplazamiento_id = empla.id
    INNER JOIN TIPO_EMPLAZAMIENTO t ON empla.tipo_id = t.id
    INNER JOIN ALMACEN a ON empla.almacen_id = a.id
    LEFT JOIN DIRECCION d ON a.direccion_id = d.id
    LEFT JOIN LOCALIDAD l ON d.localidad_id = l.id
    LEFT JOIN PROVINCIA pr ON l.provincia_id = pr.id
    """;
    
    private static final String SQL_UPDATE_EXISTENCIA = 
        "UPDATE EXISTENCIA SET " +
        "articulo_id = ?, proveedor_id = ?, sku = ?, emplazamiento_id = ?, precio = ?, fecha_compra = ?, comprador = ?, disponible = ?, fecha_no_disponible = ?, anotacion = ? " +
        "WHERE id = ?;";

    private static final String SQL_CREATE_EXISTENCIA = 
        "INSERT INTO EXISTENCIA (articulo_id, proveedor_id, sku, emplazamiento_id, precio, fecha_compra, comprador, disponible, fecha_no_disponible, anotacion) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    private static final String SQL_DELETE_EXISTENCIA = 
        "DELETE FROM EXISTENCIA WHERE id = ?;";
    
    private static final String SQL_ENABLE_EXISTENCIA = 
        "UPDATE EXISTENCIA SET disponible = TRUE, fecha_no_disponible = NULL WHERE id = ?;";
    
    private static final String SQL_DISABLE_EXISTENCIA = 
        "UPDATE EXISTENCIA SET disponible = FALSE, fecha_no_disponible = ? WHERE id = ?;";
    
    
    private static ExistenciaDTO getExistenciaFromResultSet(ResultSet rs) throws SQLException {
    
        MarcaDTO marcaDTO = MarcaDTO.builder()
                .id(rs.getInt("m_id"))
                .nombre(rs.getString("m_nombre"))
                .descripcion(rs.getString("m_descripcion"))
                .imagen(rs.getString("m_imagen"))
                .activo(rs.getBoolean("m_activo"))
                .build();         

        ArticuloDTO articuloDTO = ArticuloDTO.builder()
                .id(rs.getInt("art_id"))
                .nombre(rs.getString("art_nombre"))
                .descripcion(rs.getString("art_descripcion"))
                .referencia(rs.getString("art_referencia"))
                .marca(marcaDTO)
                .modelo(rs.getString("art_modelo"))
                .stockMinimo(rs.getInt("art_stock_minimo"))
                .imagen(rs.getString("art_imagen"))
                .activo(rs.getBoolean("art_activo"))
                .build();

        DireccionDTO direccionProveedor = DireccionDTO.builder()
                .id(rs.getInt("p_direccion_id"))
                .build();

        ProveedorDTO proveedorDTO = ProveedorDTO.builder()
                .id(rs.getInt("exi_proveedor_id"))
                .nombre(rs.getString("proveedor_nombre"))
                .descripcion(rs.getString("proveedor_descripcion"))
                .paginaWeb(rs.getString("proveedor_pagina_web"))
                .imagen(rs.getString("proveedor_imagen"))
                .activo(rs.getBoolean("proveedor_activo"))
                .direccion(direccionProveedor)
                .build();

        ProvinciaDTO provinciaAlmacen = ProvinciaDTO.builder()
                .id(rs.getInt("pr_id"))
                .nombre(rs.getString("pr_nombre"))
                .build();

        LocalidadDTO localidadAlmacen = LocalidadDTO.builder()
                .id(rs.getInt("l_id"))
                .nombre(rs.getString("l_nombre"))
                .provincia(provinciaAlmacen)
                .build();

        DireccionDTO direccionAlmacen = DireccionDTO.builder()
                .id(rs.getInt("d_id"))
                .calle(rs.getString("d_calle"))
                .numero(rs.getString("d_numero"))
                .codigoPostal(rs.getInt("d_codigo_postal"))
                .localidad(localidadAlmacen)
                .activo(rs.getBoolean("d_activo"))
                .build();

        AlmacenDTO almacenDTO = AlmacenDTO.builder()
                .id(rs.getInt("a_id"))
                .nombre(rs.getString("a_nombre"))
                .descripcion(rs.getString("a_descripcion"))
                .direccion(direccionAlmacen)
                .activo(rs.getBoolean("a_activo"))
                .build();

        TipoEmplazamientoDTO tipoEmplazamiento = TipoEmplazamientoDTO.builder()
                .id(rs.getInt("t_id"))
                .nombre(rs.getString("t_nombre"))
                .build();

        EmplazamientoDTO emplazamientoDTO = EmplazamientoDTO.builder()
                .id(rs.getInt("empla_id"))
                .nombre(rs.getString("empla_nombre"))
                .descripcion(rs.getString("empla_descripcion"))
                .tipoEmplazamiento(tipoEmplazamiento)
                .almacen(almacenDTO)
                .build();

        ExistenciaDTO existenciaDTO = ExistenciaDTO.builder()
                .id(rs.getInt("exi_id"))
                .articulo(articuloDTO)
                .proveedor(proveedorDTO)
                .sku(rs.getString("exi_sku"))
                .emplazamiento(emplazamientoDTO)
                .precio(rs.getDouble("exi_precio"))
                .fechaCompra(rs.getDate("exi_fecha_compra") != null ? rs.getDate("exi_fecha_compra").toLocalDate() : null)
                .comprador(rs.getString("exi_comprador"))
                .disponible(Disponibilidad.fromValue(rs.getBoolean("exi_disponible")))
                .fechaNoDisponible(rs.getDate("exi_fecha_no_disponible") != null ? rs.getDate("exi_fecha_no_disponible").toLocalDate() : null)
                .anotacion(rs.getString("exi_anotacion"))
                .build();

        return existenciaDTO;
    }

    @Override
    public ExistenciaDTO getExistenciaById(int idExistencia) {
        ExistenciaDTO existenciaDTO = null;
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE exi.id = ?");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idExistencia);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    existenciaDTO = getExistenciaFromResultSet(resultSet);
                    
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existenciaDTO;
    }

    @Override
    public List<ExistenciaDTO> getAllExistencias() {
        List<ExistenciaDTO> listaExistencias = new ArrayList<>();
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_COMUN)) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    ExistenciaDTO existenciaDTO = getExistenciaFromResultSet(resultSet);
                    listaExistencias.add(existenciaDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaExistencias;
    }

    @Override
    public List<ExistenciaDTO> findExistencias(String nombreArticulo, String referenciaArticulo, String sku, int marcaId, int almacenId, String nombreEmplazamiento, int disponibilidad) {
        List<ExistenciaDTO> listaExistencias = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE art.nombre LIKE ?");

        // Condición por referencia
        if (referenciaArticulo != null && !referenciaArticulo.trim().isEmpty()) {
            sql.append(" AND art.referencia LIKE ?");
        }
        
        // Condición por sku
        if (sku != null && !sku.trim().isEmpty()) {
            sql.append(" AND exi.sku LIKE ?");
        }
        
        // Condición por marcaId
        if (marcaId != -1) {
            sql.append(" AND art.marca_id = ?");
        }
        
        // Condición por almacenId
        if (almacenId != -1) {
            sql.append(" AND a.id = ?");
        }
        
        // Condición por nombre emplazamiento
        if (!nombreEmplazamiento.isBlank()) {
            sql.append(" AND empla.nombre like ?");
        }
        
        // Condición por disponibilidad
        if (disponibilidad != -1) {
            sql.append(" AND exi.disponible = ?");
        }
        

        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            int index = 1;
            
            ps.setString(index++, "%" + nombreArticulo + "%");
            
            if (referenciaArticulo != null && !referenciaArticulo.trim().isEmpty()) {
                ps.setString(index++, "%" + referenciaArticulo + "%");
            }
            
            if (sku != null && !sku.trim().isEmpty()) {
                ps.setString(index++, "%" + sku + "%");
            }
            
            if (marcaId != -1) {
                ps.setInt(index++, marcaId);
            }
            
            if (almacenId != -1) {
                ps.setInt(index++, almacenId);
            }
            
            if (!nombreEmplazamiento.isBlank()) {
                ps.setString(index++, "%" + nombreEmplazamiento + "%");
            }
            
            if (disponibilidad != -1) {
                ps.setInt(index++, disponibilidad);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    ExistenciaDTO existenciaDTO = getExistenciaFromResultSet(resultSet);
                    listaExistencias.add(existenciaDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaExistencias;
    }

    @Override
    public ExistenciaDTO createExistencia(ExistenciaDTO existenciaDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_EXISTENCIA, Statement.RETURN_GENERATED_KEYS)) {
            

            ps.setInt(1, existenciaDTO.getArticulo().getId());
            ps.setObject(2, existenciaDTO.getProveedor() != null ? existenciaDTO.getProveedor().getId() : null);
            ps.setObject(3, existenciaDTO.getSku() != null ? existenciaDTO.getSku() : null);
            ps.setInt(4, existenciaDTO.getEmplazamiento().getId());
            ps.setDouble(5, existenciaDTO.getPrecio());
            ps.setDate(6, Date.valueOf(existenciaDTO.getFechaCompra()));
            ps.setString(7, existenciaDTO.getComprador());
            ps.setBoolean(8, existenciaDTO.getDisponible().getValor());
            if (existenciaDTO.getFechaNoDisponible() != null) {
                ps.setDate(9, java.sql.Date.valueOf(existenciaDTO.getFechaNoDisponible()));
            } else {
                ps.setNull(9, java.sql.Types.DATE);
            }
            ps.setObject(10, existenciaDTO.getAnotacion() != null ? existenciaDTO.getAnotacion() : null);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        existenciaDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return existenciaDTO;
    }

    @Override
    public boolean updateExistencia(ExistenciaDTO existenciaDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_EXISTENCIA)) {
            
            ps.setInt(1, existenciaDTO.getArticulo().getId());
            ps.setObject(2, existenciaDTO.getProveedor() != null ? existenciaDTO.getProveedor().getId() : null);
            ps.setObject(3, existenciaDTO.getSku() != null ? existenciaDTO.getSku() : null);
            ps.setInt(4, existenciaDTO.getEmplazamiento().getId());
            ps.setDouble(5, existenciaDTO.getPrecio());
            ps.setDate(6, Date.valueOf(existenciaDTO.getFechaCompra()));
            ps.setString(7, existenciaDTO.getComprador());
            ps.setBoolean(8, existenciaDTO.getDisponible().getValor());
            if (existenciaDTO.getFechaNoDisponible() != null) {
                ps.setDate(9, java.sql.Date.valueOf(existenciaDTO.getFechaNoDisponible()));
            } else {
                ps.setNull(9, java.sql.Types.DATE);
            }
            ps.setObject(10, existenciaDTO.getAnotacion() != null ? existenciaDTO.getAnotacion() : null);
            
            ps.setInt(11, existenciaDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteExistencia(int existenciaId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_EXISTENCIA)) {

            ps.setInt(1, existenciaId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    
    
}
