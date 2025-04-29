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

    private static final String SQL_SELECT_PROVEEDOR_BY_ID = 
        "SELECT p.*, d.*, l.*, pr.* FROM PROVEEDOR p " +
        "INNER JOIN DIRECCION d ON p.direccion_id = d.id " +
        "INNER JOIN LOCALIDAD l ON d.localidad_id = l.id " +
        "INNER JOIN PROVINCIA pr ON l.provincia_id = pr.id " +
        "WHERE p.id = ?;";

    private static final String SQL_SELECT_CONTACTOS_BY_PROVEEDOR = 
        "SELECT c.*, pt.id AS pt_id, pt.nombre AS pt_nombre " +
        "FROM CONTACTO c " +
        "INNER JOIN PUESTO_TRABAJO pt ON c.puesto_id = pt.id " +
        "WHERE c.proveedor_id = ?;";

    private static final String SQL_SELECT_PROVEEDORES = 
        "SELECT p.*, d.*, l.*, pr.* FROM PROVEEDOR p " +
        "INNER JOIN DIRECCION d ON p.direccion_id = d.id " +
        "INNER JOIN LOCALIDAD l ON d.localidad_id = l.id " +
        "INNER JOIN PROVINCIA pr ON l.provincia_id = pr.id " +
        "WHERE p.activo = TRUE AND p.nombre LIKE ?;";

    private static final String SQL_UPDATE_PROVEEDOR = 
        "UPDATE PROVEEDOR SET nombre = ?, descripcion = ?, pagina_web = ?, " +
        "url_imagen = ?, direccion_id = ? WHERE id = ?;";

    private static final String SQL_CREATE_PROVEEDOR = 
        "INSERT INTO PROVEEDOR (nombre, descripcion, pagina_web, url_imagen, direccion_id) " +
        "VALUES (?, ?, ?, ?, ?);";

    private static final String SQL_DELETE_PROVEEDOR = 
        "UPDATE PROVEEDOR SET activo = FALSE WHERE id = ?;";

    private static ProveedorDTO getProveedorFromResultSet(ResultSet rs) throws SQLException {
        DireccionDTO direccionDTO = new DireccionDTO(
            rs.getInt("d.id"),
            rs.getString("d.calle"),
            rs.getString("d.numero"),
            rs.getInt("d.codigo_postal"),
            new LocalidadDTO(
                rs.getInt("l.id"),
                rs.getString("l.nombre"),
                new ProvinciaDTO(
                    rs.getInt("pr.id"),
                    rs.getString("pr.nombre")
                )
            ),
            rs.getBoolean("d.activo")
        );

        return new ProveedorDTO(
            rs.getInt("p.id"),
            rs.getString("p.nombre"),
            rs.getString("p.descripcion"),
            rs.getString("p.pagina_web"),
            rs.getString("p.url_imagen"),
            rs.getBoolean("p.activo"),
            direccionDTO,
            new ArrayList<>()
        );
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
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_PROVEEDOR_BY_ID)) {

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

        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_PROVEEDOR_BY_ID)) {

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
    public List<ProveedorDTO> findProveedores(String nombre) {
        List<ProveedorDTO> listaProveedores = new ArrayList<>();
        
        try (Connection conexion = getConnection(); 
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_PROVEEDORES)) {

            ps.setString(1, '%' + nombre + '%');

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    ProveedorDTO proveedorDTO = getProveedorFromResultSet(resultSet);
                    proveedorDTO.setListaContactos(getContactosByProveedor(proveedorDTO.getId()));
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
            ps.setString(4, proveedorDTO.getUrlImagen());
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
            ps.setString(4, proveedorDTO.getUrlImagen());
            ps.setInt(5, proveedorDTO.getDireccion().getId());

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
