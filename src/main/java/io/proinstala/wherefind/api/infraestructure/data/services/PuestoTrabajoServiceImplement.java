
package io.proinstala.wherefind.api.infraestructure.data.services;

import io.proinstala.wherefind.api.infraestructure.data.interfaces.IPuestoTrabajoService;
import io.proinstala.wherefind.shared.dtos.PuestoTrabajoDTO;
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
public class PuestoTrabajoServiceImplement extends BaseMySql implements IPuestoTrabajoService {
    
    private static final String SQL_SELECT_ALL = "SELECT * FROM PUESTO_TRABAJO";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM PUESTO_TRABAJO WHERE id = ?";

    private PuestoTrabajoDTO getPuestoFromResultSet(ResultSet rs) throws SQLException {
        return PuestoTrabajoDTO.builder()
                .id(rs.getInt("id"))
                .nombre(rs.getString("nombre"))
                .build();
    }

    @Override
    public PuestoTrabajoDTO getPuestoById(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getPuestoFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PuestoTrabajoDTO> getAllPuestos() {
        List<PuestoTrabajoDTO> puestos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                puestos.add(getPuestoFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puestos;
    }
}
