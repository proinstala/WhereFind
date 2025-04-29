package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IContactoService;
import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import io.proinstala.wherefind.shared.dtos.ProveedorDTO;
import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio para la gestión de contactos en la base de datos.
 *
 * Esta clase implementa la interfaz {@link IContactoService} y proporciona métodos para
 * obtener, crear, actualizar y eliminar contactos relacionados con proveedores.
 * 
 * Extiende {@link BaseMySql} para la conexión a la base de datos MySQL.
 */
public class ContactoServiceImplement extends BaseMySql implements IContactoService {
    
    
    private static final String SQL_SELECT_BY_ID = 
        "SELECT c.*, p.id AS puesto_id, p.nombre AS puesto_nombre, " +
        "pr.id AS proveedor_id, pr.nombre AS proveedor_nombre " +
        "FROM CONTACTO c " +
        "JOIN PUESTO_TRABAJO p ON c.puesto_id = p.id " +
        "JOIN PROVEEDOR pr ON c.proveedor_id = pr.id " +
        "WHERE c.id = ?";

    private static final String SQL_SELECT_BY_PROVEEDOR = 
        "SELECT c.*, p.id AS puesto_id, p.nombre AS puesto_nombre, " +
        "pr.id AS proveedor_id, pr.nombre AS proveedor_nombre " +
        "FROM CONTACTO c " +
        "JOIN PUESTO_TRABAJO p ON c.puesto_id = p.id " +
        "JOIN PROVEEDOR pr ON c.proveedor_id = pr.id " +
        "WHERE c.proveedor_id = ?";

    private static final String SQL_SELECT_CONTACTOS =
        "SELECT c.*, p.id AS puesto_id, p.nombre AS puesto_nombre, " +
        "pr.id AS proveedor_id, pr.nombre AS proveedor_nombre " +
        "FROM CONTACTO c " +
        "JOIN PUESTO_TRABAJO p ON c.puesto_id = p.id " +
        "JOIN PROVEEDOR pr ON c.proveedor_id = pr.id " +
        "WHERE c.activo = TRUE";

    private static final String SQL_INSERT_CONTACTO = 
        "INSERT INTO CONTACTO (nombre, apellido, puesto_id, telefono, email, activo, proveedor_id) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_CONTACTO = 
        "UPDATE CONTACTO SET nombre = ?, apellido = ?, puesto_id = ?, telefono = ?, email = ?, activo = ? " +
        "WHERE id = ?";

    private static final String SQL_DELETE_CONTACTO = 
        "UPDATE CONTACTO SET activo = FALSE WHERE id = ?";

    
    private ContactoDTO getContactoFromResultSet(ResultSet rs) throws SQLException {
        PuestoTrabajoDTO puesto = PuestoTrabajoDTO.builder()
            .id(rs.getInt("puesto_id"))
            .nombre(rs.getString("puesto_nombre"))
            .build();

        ProveedorDTO proveedor = ProveedorDTO.builder()
            .id(rs.getInt("proveedor_id"))
            .nombre(rs.getString("proveedor_nombre"))
            .build();

        return ContactoDTO.builder()
            .id(rs.getInt("id"))
            .nombre(rs.getString("nombre"))
            .apellido(rs.getString("apellido"))
            .puestoTrabajo(puesto)
            .telefono(rs.getString("telefono"))
            .email(rs.getString("email"))
            .activo(rs.getBoolean("activo"))
            .proveedor(proveedor)
            .build();
    }

    
    /**
     * Obtiene un contacto por su identificador, incluyendo el proveedor asociado.
     *
     * @param idContacto el identificador del contacto.
     * @return el objeto ContactoDTO correspondiente, o null si no se encuentra.
     */
    @Override
    public ContactoDTO getContactoById(int idContacto) {
        ContactoDTO contacto = null;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idContacto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    contacto = getContactoFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacto;
    }
    
   
    /**
     * Obtiene una lista de contactos asociados a un proveedor específico.
     *
     * @param idProveedor el ID del proveedor
     * @return una lista de {@link ContactoDTO} asociados al proveedor
     */
    @Override
    public List<ContactoDTO> getContactosByProveedorId(int idProveedor) {
        List<ContactoDTO> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_PROVEEDOR)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContactoDTO contacto = getContactoFromResultSet(rs);
                    lista.add(contacto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    
    @Override
    public List<ContactoDTO> findContactos(String nombre, int proveedorId) {
        List<ContactoDTO> listaContactos = new ArrayList<>();

        StringBuilder sql = new StringBuilder(SQL_SELECT_CONTACTOS);

        // Condición por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND c.nombre LIKE ?");
        }

        // Condición por proveedorId
        if (proveedorId != -1) {
            sql.append(" AND c.proveedor_id = ?");
        }

        sql.append(";");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (nombre != null && !nombre.trim().isEmpty()) {
                ps.setString(index++, "%" + nombre + "%");
            }

            if (proveedorId != -1) {
                ps.setInt(index++, proveedorId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContactoDTO contacto = getContactoFromResultSet(rs);
                    listaContactos.add(contacto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return listaContactos;
    }
    

    /**
     * Crea un nuevo contacto en la base de datos.
     *
     * @param contactoDTO el contacto a crear
     * @return el {@link ContactoDTO} creado con su ID asignado, o {@code null} si falla
     */
    @Override
    public ContactoDTO createContacto(ContactoDTO contactoDTO) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_CONTACTO, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, contactoDTO.getNombre());
            ps.setString(2, contactoDTO.getApellido());
            ps.setInt(3, contactoDTO.getPuestoTrabajo().getId()); 
            ps.setString(4, contactoDTO.getTelefono());
            ps.setString(5, contactoDTO.getEmail());
            ps.setBoolean(6, contactoDTO.isActivo());
            ps.setInt(7, contactoDTO.getProveedor().getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contactoDTO.setId(generatedKeys.getInt(1));
                        return contactoDTO;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza un contacto existente en la base de datos.
     *
     * @param contactoDTO el contacto con los datos actualizados
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario
     */
    @Override
    public boolean updateContacto(ContactoDTO contactoDTO) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_CONTACTO)) {

            ps.setString(1, contactoDTO.getNombre());
            ps.setString(2, contactoDTO.getApellido());
            ps.setInt(3, contactoDTO.getPuestoTrabajo().getId()); // actualizado
            ps.setString(4, contactoDTO.getTelefono());
            ps.setString(5, contactoDTO.getEmail());
            ps.setBoolean(6, contactoDTO.isActivo());
            ps.setInt(7, contactoDTO.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Realiza una eliminación lógica de un contacto, estableciendo su estado como inactivo.
     *
     * @param idContacto el ID del contacto a eliminar lógicamente
     * @return {@code true} si la operación fue exitosa, {@code false} en caso contrario
     */
    @Override
    public boolean deleteContacto(int idContacto) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_CONTACTO)) {

            ps.setInt(1, idContacto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
