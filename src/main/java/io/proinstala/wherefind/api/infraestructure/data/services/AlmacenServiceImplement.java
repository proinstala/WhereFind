
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IAlmacenService;
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
public class AlmacenServiceImplement extends BaseMySql implements IAlmacenService{
    
    private static final String SQL_SELECT_COMUN = 
            """
            SELECT
             a.*,
             d.calle AS d_calle, d.numero AS d_numero, d.codigo_postal AS d_codigo_postal, d.activo AS d_activo,
             l.id AS l_id, l.nombre AS l_nombre,
             pr.id AS pr_id, pr.nombre AS pr_nombre
            FROM ALMACEN a
            LEFT JOIN DIRECCION d ON a.direccion_id = d.id
            LEFT JOIN LOCALIDAD l ON d.localidad_id = l.id
            LEFT JOIN PROVINCIA pr ON l.provincia_id = pr.id
            """;
    
    private static final String SQL_SELECT_EMPLAZAMIENTOS_BY_ALMACEN = 
            """
            SELECT
             e.*,
             t_e.nombre, t_e.descripcion
            FROM EMPLAZAMIENTO e
            INNER JOIN TIPO_EMPLAZAMIENTO t_e ON e.tipo_id = t_e.id 
            INNER JOIN ALMACEN a ON e.almacen_id = a.id
            WHERE a.id = ? AND e.activo = 1;
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
    
    private static final String SQL_UPDATE_ALMACEN = 
        "UPDATE ALMACEN SET nombre = ?, descripcion = ?, direccion_id = ? " +
        "WHERE id = ?;";

    private static final String SQL_CREATE_ALMACEN = 
        "INSERT INTO ALMACEN (nombre, descripcion, direccion_id) " +
        "VALUES (?, ?, ?);";
    
    private static final String SQL_DELETE_ALMACEN = 
        "UPDATE ALMACEN SET activo = FALSE WHERE id = ?;";
    
    private AlmacenDTO getAlmacenFromResultSet(ResultSet rs) throws SQLException {
        
        AlmacenDTO almacenDTO = AlmacenDTO.builder()
            .id(rs.getInt("a.id"))
            .nombre(rs.getString("a.nombre"))
            .descripcion(rs.getString("a.descripcion"))
            .activo(rs.getBoolean("a.activo"))
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
            .id(rs.getInt("a.direccion_id"))
            .calle(rs.getString("d_calle"))
            .numero(rs.getString("d_numero"))
            .codigoPostal(rs.getInt("d_codigo_postal"))
            .localidad(localidadDTO)
            .activo(rs.getBoolean("d_activo"))
            .build();
        
        almacenDTO.setDireccion(direccionDTO);
        
        return almacenDTO;
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
    
    /*
    private List<EmplazamientoDTO> getEmplazamientosByAlmacen(AlmacenDTO almacenDTO) {
        List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_EMPLAZAMIENTOS_BY_ALMACEN)) {

            ps.setInt(1, almacenDTO.getId());

            try (ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    TipoEmplazamientoDTO tipo = TipoEmplazamientoDTO.builder()
                            .id(rs.getInt("e.tipo_id"))
                            .nombre(rs.getString("t_e.nombre"))
                            .descripcion(rs.getString("t_e.descripcion"))
                            .build();
                    
                    EmplazamientoDTO emplazamientoDTO = EmplazamientoDTO.builder()
                            .id(rs.getInt("e.id"))
                            .nombre(rs.getString("e.nombre"))
                            .descripcion(rs.getString("e.descripcion"))
                            .tipoEmplazamiento(tipo)
                            .build();

                    listaEmplazamientos.add(emplazamientoDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmplazamientos;
    }
    */
    
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
    
    private List<EmplazamientoDTO> getEmplazamientosByAlmacen(Connection conexion, AlmacenDTO almacenDTO) throws SQLException {
        List<EmplazamientoDTO> listaEmplazamientos = new ArrayList<>();
        
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_EMPLAZAMIENTOS_BY_ALMACEN)) {
            ps.setInt(1, almacenDTO.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoEmplazamientoDTO tipo = TipoEmplazamientoDTO.builder()
                            .id(rs.getInt("e.tipo_id"))
                            .nombre(rs.getString("t_e.nombre"))
                            .descripcion(rs.getString("t_e.descripcion"))
                            .build();

                    EmplazamientoDTO emplazamientoDTO = EmplazamientoDTO.builder()
                            .id(rs.getInt("e.id"))
                            .nombre(rs.getString("e.nombre"))
                            .descripcion(rs.getString("e.descripcion"))
                            .tipoEmplazamiento(tipo)
                            .build();

                    emplazamientoDTO.setListaExistencias(getExistenciasByEmplazamiento(conexion, emplazamientoDTO.getId()));
                    listaEmplazamientos.add(emplazamientoDTO);
                }
            }
        }

        return listaEmplazamientos;
    }
    
    @Override
    public AlmacenDTO getAlmacenById(int idAlmacen) {
        AlmacenDTO almacenDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE a.activo = TRUE AND a.id = ?");

        try (Connection conexion = getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idAlmacen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    almacenDTO = getAlmacenFromResultSet(rs);
                }
            }

            if (almacenDTO != null) {
                almacenDTO.setListaEmplazamientos(getEmplazamientosByAlmacen(conexion, almacenDTO));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return almacenDTO;
    }

    /*
    @Override
    public AlmacenDTO getAlmacenById(int idAlmacen) {
        AlmacenDTO almacenDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE a.activo = TRUE");
        sql.append(" AND a.id = ?");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idAlmacen);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    almacenDTO = getAlmacenFromResultSet(resultSet);
                }
            }

            if (almacenDTO != null) {
                almacenDTO.setListaEmplazamientos(getEmplazamientosByAlmacen(almacenDTO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return almacenDTO;
    }
    */

    @Override
    public List<AlmacenDTO> getAllAlmacenes() {
        List<AlmacenDTO> listaAlmacenes = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE a.activo = TRUE");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    AlmacenDTO almacenDTO = getAlmacenFromResultSet(resultSet);
                    listaAlmacenes.add(almacenDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAlmacenes;
    }

    @Override
    public List<AlmacenDTO> findAlmacenes(String nombre, String descripcion) {
        List<AlmacenDTO> listaAlmacenes = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE a.activo = TRUE");
        
        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND a.nombre LIKE ?");
        }
        
        // Condición por descripcion
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND a.descripcion LIKE ?");
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

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    AlmacenDTO almacenDTO = getAlmacenFromResultSet(resultSet);
                    listaAlmacenes.add(almacenDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAlmacenes;
    }

    @Override
    public AlmacenDTO createAlmacen(AlmacenDTO almacenDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_ALMACEN, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, almacenDTO.getNombre());
            ps.setString(2, almacenDTO.getDescripcion());
            
            // Validación para permitir direccion null
            if (almacenDTO.getDireccion() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, almacenDTO.getDireccion().getId());
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        almacenDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return almacenDTO;
    }

    @Override
    public boolean updateAlmacen(AlmacenDTO almacenDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_ALMACEN)) {

            ps.setString(1, almacenDTO.getNombre());
            ps.setString(2, almacenDTO.getDescripcion());
            ps.setObject(3, almacenDTO.getDireccion() != null ? almacenDTO.getDireccion().getId() : null);
            ps.setInt(4, almacenDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteAlmacen(int almacenId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_ALMACEN)) {

            ps.setInt(1, almacenId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }
    
}
