
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IEmplazamientoService;
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
public class EmplazamientoServiceImplement extends BaseMySql implements IEmplazamientoService {
    
    private static final String SQL_SELECT_COMUN = 
           """                            
           SELECT
            e.*,
           
            t.id,
            t.nombre,
           
            a.id, a.nombre,
            a.descripcion, 
            a.activo,
            
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
           
           FROM EMPLAZAMIENTO e
           INNER JOIN TIPO_EMPLAZAMIENTO t ON e.tipo_id = t.id
           INNER JOIN ALMACEN a ON e.almacen_id = a.id
           LEFT JOIN DIRECCION d ON a.direccion_id = d.id
           LEFT JOIN LOCALIDAD l ON d.localidad_id = l.id
           LEFT JOIN PROVINCIA pr ON l.provincia_id = pr.id
           """;
    
    private static final String SQL_SELECT_EXISTENCIAS_BY_EMPLAZAMIENTO =
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
          art.activo AS art_activo,

          m.id AS m_id,
          m.nombre AS m_nombre,
          m.descripcion AS m_descripcion,
          m.activo AS m_activo,

          p.id AS p_id,
          p.nombre AS proveedor_nombre,
          p.descripcion AS proveedor_descripcion,
          p.pagina_web AS proveedor_pagina_web,
          p.activo AS proveedor_activo,
          p.direccion_id AS p_direccion_id

        FROM EXISTENCIA exi
        INNER JOIN ARTICULO art ON exi.articulo_id = art.id
        LEFT JOIN MARCA m ON art.marca_id = m.id
        LEFT JOIN PROVEEDOR p ON exi.proveedor_id = p.id
        INNER JOIN EMPLAZAMIENTO empla ON exi.emplazamiento_id = empla.id
        INNER JOIN ALMACEN a ON empla.almacen_id = a.id
        WHERE exi.emplazamiento_id = ?
        """;

    private static final String SQL_UPDATE_EMPLAZAMIENTO = 
        "UPDATE EMPLAZAMIENTO SET nombre = ?, descripcion = ?, tipo_id = ?, almacen_id = ? " +
        "WHERE id = ?;";

    private static final String SQL_CREATE_EMPLAZAMIENTO = 
        "INSERT INTO EMPLAZAMIENTO (nombre, descripcion, tipo_id, almacen_id) " +
        "VALUES (?, ?, ?, ?);";

    private static final String SQL_DELETE_EMPLAZAMIENTO = 
        "UPDATE EMPLAZAMIENTO SET activo = FALSE WHERE id = ?;";
    
    
    private static EmplazamientoDTO getEmplazamientoFromResultSet(ResultSet rs) throws SQLException {

        TipoEmplazamientoDTO tipoEmplazamiento = TipoEmplazamientoDTO.builder()
                .id(rs.getInt("t.id"))
                .nombre(rs.getString("t.nombre"))
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
                .id(rs.getInt("a.id"))
                .nombre(rs.getString("a.nombre"))
                .descripcion(rs.getString("a.descripcion"))
                .direccion(direccionAlmacen)
                .activo(rs.getBoolean("a.activo"))
                .build();

        EmplazamientoDTO emplazamientoDTO = EmplazamientoDTO.builder()
                .id(rs.getInt("e.id"))
                .nombre(rs.getString("e.nombre"))
                .descripcion(rs.getString("e.descripcion"))
                .tipoEmplazamiento(tipoEmplazamiento)
                .almacen(almacenDTO)
                .build();

        return emplazamientoDTO;
    }
    
    private ExistenciaDTO getExistenciaFromResultSet(ResultSet rs) throws SQLException {
    
        MarcaDTO marcaDTO = MarcaDTO.builder()
                .id(rs.getInt("m_id"))
                .nombre(rs.getString("m_nombre"))
                .descripcion(rs.getString("m_descripcion"))
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
                //.imagen(rs.getString("art_imagen"))
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
                .activo(rs.getBoolean("proveedor_activo"))
                .direccion(direccionProveedor)
                .build();

        ExistenciaDTO existenciaDTO = ExistenciaDTO.builder()
                .id(rs.getInt("exi_id"))
                .articulo(articuloDTO)
                .proveedor(proveedorDTO)
                .sku(rs.getString("exi_sku"))
                .precio(rs.getDouble("exi_precio"))
                .fechaCompra(rs.getDate("exi_fecha_compra") != null ? rs.getDate("exi_fecha_compra").toLocalDate() : null)
                .comprador(rs.getString("exi_comprador"))
                .disponible(Disponibilidad.fromValue(rs.getBoolean("exi_disponible")))
                .fechaNoDisponible(rs.getDate("exi_fecha_no_disponible") != null ? rs.getDate("exi_fecha_no_disponible").toLocalDate() : null)
                .anotacion(rs.getString("exi_anotacion"))
                .build();

        return existenciaDTO;
    }
    
    private List<ExistenciaDTO> getExistenciasByEmplazamiento(Connection conexion, int idEmplazamiento) throws SQLException {
        List<ExistenciaDTO> listaExistencias = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_EXISTENCIAS_BY_EMPLAZAMIENTO)) {
            ps.setInt(1, idEmplazamiento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaExistencias.add(getExistenciaFromResultSet(rs));
                }
            }
        }

        return listaExistencias;
    }
    

    @Override
    public EmplazamientoDTO getEmplazamientoById(int idEmplazamiento) {
        EmplazamientoDTO emplazamientoDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE e.activo = TRUE");
        sql.append(" AND e.id = ?");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idEmplazamiento);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    emplazamientoDTO = getEmplazamientoFromResultSet(resultSet);
                    emplazamientoDTO.setListaExistencias(getExistenciasByEmplazamiento(conexion, emplazamientoDTO.getId()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
        return emplazamientoDTO;
    }

    @Override
    public List<EmplazamientoDTO> getAllEmplazamientos() {
        List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE e.activo = TRUE");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    EmplazamientoDTO emplazamientoDTO = getEmplazamientoFromResultSet(resultSet);
                    listaEmplazamientos.add(emplazamientoDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmplazamientos;
    }

    @Override
    public List<EmplazamientoDTO> findEmplazamientos(String nombre, String descripcion, int tipoEmplazamientoId, int almacenId) {
        List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE e.activo = TRUE");
        
        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND e.nombre LIKE ?");
        }
        
        // Condición por descripcion
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND e.descripcion LIKE ?");
        }
        
        
        // Condición por descripcion
        if (tipoEmplazamientoId != -1) {
            sql.append(" AND e.tipo_id = ?");
        }
        
        // Condición por descripcion
        if (almacenId != -1) {
            sql.append(" AND e.almacen_id = ?");
        }
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            int index = 1;
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                ps.setString(index++, "%" + nombre + "%");
            }

            if (descripcion != null && !descripcion.trim().isEmpty()) {
                ps.setString(index++, "%" + descripcion + "%");
            }
            
            if (tipoEmplazamientoId != -1) {
                ps.setInt(index++, tipoEmplazamientoId);
            }
            
            if (almacenId != -1) {
                ps.setInt(index++, almacenId);
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    EmplazamientoDTO emplazamientoDTO = getEmplazamientoFromResultSet(resultSet);
                    listaEmplazamientos.add(emplazamientoDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmplazamientos;
    }

    @Override
    public EmplazamientoDTO createEmplazamiento(EmplazamientoDTO emplazamientoDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_EMPLAZAMIENTO, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, emplazamientoDTO.getNombre());
            ps.setString(2, emplazamientoDTO.getDescripcion());
            ps.setInt(3, emplazamientoDTO.getTipoEmplazamiento().getId());
            ps.setInt(4, emplazamientoDTO.getAlmacen().getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        emplazamientoDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return emplazamientoDTO;
    }

    @Override
    public boolean updateEmplazamiento(EmplazamientoDTO emplazamientoDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_EMPLAZAMIENTO)) {

            ps.setString(1, emplazamientoDTO.getNombre());
            ps.setString(2, emplazamientoDTO.getDescripcion());
            ps.setInt(3, emplazamientoDTO.getTipoEmplazamiento().getId());
            ps.setInt(4, emplazamientoDTO.getAlmacen().getId());
            ps.setInt(5, emplazamientoDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteEmplazamiento(int emplazamientoId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_EMPLAZAMIENTO)) {

            ps.setInt(1, emplazamientoId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
    
}
