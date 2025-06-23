package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IProveedorService;
import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
import io.proinstala.wherefind.shared.dtos.LocalidadDTO;
import io.proinstala.wherefind.shared.dtos.ProvinciaDTO;
import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import jakarta.enterprise.context.RequestScoped;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio para la gestión de proveedores en la base de datos.
 */
@RequestScoped
public class ProveedorServiceImplement extends BaseMySql implements IProveedorService {
    
   private static final String SQL_SELECT_COMUN = 
           """                            
           SELECT
            p.*,
            d.id AS d_id, d.calle AS d_calle, d.numero AS d_numero, d.codigo_postal AS d_codigo_postal, d.localidad_id AS d_localidad_id, d.activo AS d_activo,
            l.id AS l_id, l.nombre AS l_nombre, l.provincia_id AS l_provincia_id,
            pr.id AS pr_id, pr.nombre AS pr_nombre
           FROM PROVEEDOR p
           LEFT JOIN DIRECCION d ON p.direccion_id = d.id
           INNER JOIN LOCALIDAD l ON d.localidad_id = l.id
           INNER JOIN PROVINCIA pr ON l.provincia_id = pr.id
           """;

    private static final String SQL_SELECT_CONTACTOS_BY_PROVEEDOR = 
        "SELECT c.*, pt.id AS pt_id, pt.nombre AS pt_nombre " +
        "FROM CONTACTO c " +
        "INNER JOIN PUESTO_TRABAJO pt ON c.puesto_id = pt.id " +
        "WHERE c.proveedor_id = ?;";

    private static final String SQL_UPDATE_PROVEEDOR = 
        "UPDATE PROVEEDOR SET nombre = ?, descripcion = ?, pagina_web = ?, " +
        "imagen = ?, direccion_id = ? WHERE id = ?;";

    private static final String SQL_CREATE_PROVEEDOR = 
        "INSERT INTO PROVEEDOR (nombre, descripcion, pagina_web, imagen, direccion_id) " +
        "VALUES (?, ?, ?, ?, ?);";

    private static final String SQL_DELETE_PROVEEDOR = 
        "UPDATE PROVEEDOR SET activo = FALSE WHERE id = ?;";

    
    private static ProveedorDTO getProveedorFromResultSet(ResultSet rs) throws SQLException {
        
         ProveedorDTO proveedorDTO = new ProveedorDTO(
            rs.getInt("p.id"),
            rs.getString("p.nombre"),
            rs.getString("p.descripcion"),
            rs.getString("p.pagina_web"),
            rs.getString("p.imagen"),
            rs.getBoolean("p.activo"),
            new DireccionDTO(),
            new ArrayList<>()
        );
        
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
         
        return proveedorDTO;
    }

    private List<ContactoDTO> getContactosByProveedor(int proveedorId) {
        List<ContactoDTO> listaContactos = new ArrayList<>();
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_CONTACTOS_BY_PROVEEDOR)) {

            ps.setInt(1, proveedorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PuestoTrabajoDTO puesto = new PuestoTrabajoDTO(
                        rs.getInt("pt_id"),
                        rs.getString("pt_nombre")
                    );

                    ContactoDTO contactoDTO = new ContactoDTO(
                        rs.getInt("c.id"),
                        rs.getString("c.nombre"),
                        rs.getString("c.apellido"),
                        puesto,  // Asignamos el objeto, no un string
                        rs.getString("c.telefono"),
                        rs.getString("c.email"),
                        rs.getBoolean("c.activo"),
                        null // No asignamos proveedor aquí
                    );

                    listaContactos.add(contactoDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaContactos;
    }

    @Override
    public ProveedorDTO getProveedorById(int idProveedor) {
        ProveedorDTO proveedorDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE p.activo = TRUE");
        sql.append(" AND p.id = ?");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idProveedor);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    proveedorDTO = getProveedorFromResultSet(resultSet);
                }
            }

            if (proveedorDTO != null) {
                proveedorDTO.setListaContactos(getContactosByProveedor(idProveedor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedorDTO;
    }

    public ProveedorDTO getProveedorBasicById(int idProveedor) {
        ProveedorDTO proveedorDTO = null;
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE p.activo = TRUE");
        sql.append(" AND p.proveedor_id = ?");

        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            ps.setInt(1, idProveedor);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    proveedorDTO = getProveedorFromResultSet(resultSet); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proveedorDTO;
    }
    
    @Override
    public List<ProveedorDTO> getProveedores() {
        List<ProveedorDTO> listaProveedores = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        sql.append(" WHERE p.activo = TRUE");
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    ProveedorDTO proveedorDTO = getProveedorFromResultSet(resultSet);
                    //proveedorDTO.setListaContactos(getContactosByProveedor(proveedorDTO.getId()));
                    listaProveedores.add(proveedorDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProveedores;
    }

    @Override
    public List<ProveedorDTO> findProveedores(String nombre, String descripcion) {
        List<ProveedorDTO> listaProveedores = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);
        
        sql.append(" WHERE p.activo = TRUE");
        
        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND p.nombre LIKE ?");
        }
        
        // Condición por descripcion
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            sql.append(" AND p.descripcion LIKE ?");
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
                    ProveedorDTO proveedorDTO = getProveedorFromResultSet(resultSet);
                    //proveedorDTO.setListaContactos(getContactosByProveedor(proveedorDTO.getId()));
                    listaProveedores.add(proveedorDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProveedores;
    }

    @Override
    public boolean updateProveedor(ProveedorDTO proveedorDTO) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_PROVEEDOR)) {

            ps.setString(1, proveedorDTO.getNombre());
            ps.setString(2, proveedorDTO.getDescripcion());
            ps.setString(3, proveedorDTO.getPaginaWeb());
            ps.setString(4, proveedorDTO.getImagen());
            ps.setInt(5, proveedorDTO.getDireccion().getId());
            ps.setInt(6, proveedorDTO.getId());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteProveedor(int proveedorId) {
        int rowsAffected = 0;
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_PROVEEDOR)) {

            ps.setInt(1, proveedorId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected > 0;
    }

    @Override
    public ProveedorDTO createProveedor(ProveedorDTO proveedorDTO) {
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_CREATE_PROVEEDOR, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, proveedorDTO.getNombre());
            ps.setString(2, proveedorDTO.getDescripcion());
            ps.setString(3, proveedorDTO.getPaginaWeb());
            ps.setString(4, proveedorDTO.getImagen());
            
            // Validación para permitir direccion null
            if (proveedorDTO.getDireccion() == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, proveedorDTO.getDireccion().getId());
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        proveedorDTO.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return proveedorDTO;
    }
}
