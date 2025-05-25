package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IContactoService;
import io.proinstala.wherefind.shared.dtos.ContactoDTO;
import io.proinstala.wherefind.shared.dtos.DireccionDTO;
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
    
    
    private static final String SQL_SELECT_COMUN = 
            """
            SELECT c.*,
            p_t.nombre AS puesto_nombre,
            p.nombre AS proveedor_nombre, p.descripcion AS proveedor_descripcion,
            p.pagina_web AS proveedor_pagina_web, p.imagen AS proveedor_imagen, 
            p.activo AS proveedor_activo, p.direccion_id AS proveedor_direccion_id
            FROM CONTACTO c
            JOIN PUESTO_TRABAJO p_t ON c.puesto_id = p_t.id
            JOIN PROVEEDOR p ON c.proveedor_id = p.id
            """;
        

    private static final String SQL_INSERT_CONTACTO = 
            """
            INSERT INTO CONTACTO (nombre, apellido, puesto_id, telefono, email, activo, proveedor_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;
           

    private static final String SQL_UPDATE_CONTACTO = 
            """
            UPDATE CONTACTO SET nombre = ?, apellido = ?, puesto_id = ?, telefono = ?, email = ?, activo = ?, proveedor_id = ? 
            WHERE id = ?
            """;

    private static final String SQL_DELETE_CONTACTO = 
        "UPDATE CONTACTO SET activo = FALSE WHERE id = ?";

    /**
     * Extrae los datos de un {@link ResultSet} y los convierte en un objeto
     * {@link ContactoDTO}.
     *
     * <p>
     * Este método toma un conjunto de resultados de una consulta SQL que
     * contiene información sobre un contacto, su puesto de trabajo, su
     * proveedor y la dirección asociada al proveedor. Luego, construye y
     * devuelve una instancia de {@link ContactoDTO} con los datos extraídos del
     * {@link ResultSet}.</p>
     *
     * @param rs el {@link ResultSet} que contiene los datos de la consulta SQL.
     * No puede ser {@code null}.
     * @return un objeto {@link ContactoDTO} con los datos extraídos del
     * {@link ResultSet}.
     * @throws SQLException si ocurre un error al acceder a los datos del
     * {@link ResultSet}.
     */
    private ContactoDTO getContactoFromResultSet(ResultSet rs) throws SQLException {
        PuestoTrabajoDTO puesto = PuestoTrabajoDTO.builder()
                .id(rs.getInt("puesto_id"))
                .nombre(rs.getString("puesto_nombre"))
                .build();

        DireccionDTO direccion = DireccionDTO.builder()
                .id(rs.getInt("proveedor_direccion_id"))
                .build();

        ProveedorDTO proveedor = ProveedorDTO.builder()
                .id(rs.getInt("proveedor_id"))
                .nombre(rs.getString("proveedor_nombre"))
                .descripcion(rs.getString("proveedor_descripcion"))
                .paginaWeb(rs.getString("proveedor_pagina_web"))
                .urlImagen(rs.getString("proveedor_imagen"))
                .activo(rs.getBoolean("proveedor_activo"))
                .direccion(direccion)
                .listaContactos(new ArrayList<>())
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
        
        String sql = SQL_SELECT_COMUN + " WHERE c.activo = TRUE AND c.id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

        String sql = SQL_SELECT_COMUN + " WHERE c.activo = TRUE AND c.proveedor_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

        StringBuilder sql = new StringBuilder(SQL_SELECT_COMUN);

        sql.append(" WHERE c.activo = TRUE");
        
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
            ps.setInt(3, contactoDTO.getPuestoTrabajo().getId()); 
            ps.setString(4, contactoDTO.getTelefono());
            ps.setString(5, contactoDTO.getEmail());
            ps.setBoolean(6, contactoDTO.isActivo());
            ps.setInt(7, contactoDTO.getProveedor().getId());
            ps.setInt(8, contactoDTO.getId());

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
