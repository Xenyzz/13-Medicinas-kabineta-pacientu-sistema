/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Nikita.Sersts
 */
import db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Diagnoze;

public class DiagnozeDAO {

    // Pievieno diagnozi pacientam
    public boolean add(Diagnoze diagnoze) {

        String sql = "INSERT INTO diagnozes "
                + "(pacienta_id, apraksts, datums) "
                + "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, diagnoze.getPacientsId());
            ps.setString(2, diagnoze.getApraksts());

            if (diagnoze.getDatums() != null) {
                ps.setDate(3, new java.sql.Date(
                        diagnoze.getDatums().getTime()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Atgriež visas konkrētā pacienta diagnozes
    public List<Diagnoze> getDiagnozesByPacientsId(int pacientsId) {

        List<Diagnoze> diagnozes = new ArrayList<>();

        String sql = "SELECT diagnozes_id, pacienta_id, apraksts, datums "
                + "FROM diagnozes "
                + "WHERE pacienta_id = ? "
                + "ORDER BY datums DESC";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacientsId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Diagnoze diagnoze = new Diagnoze();

                    diagnoze.setId(rs.getInt("diagnozes_id"));
                    diagnoze.setPacientsId(rs.getInt("pacienta_id"));
                    diagnoze.setApraksts(rs.getString("apraksts"));
                    diagnoze.setDatums(rs.getDate("datums"));

                    diagnozes.add(diagnoze);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return diagnozes;
    }

    // Atjaunina diagnozi
    public boolean updateDiagnoze(Diagnoze diagnoze) {

        String sql = "UPDATE diagnozes "
                + "SET apraksts = ?, datums = ? "
                + "WHERE diagnozes_id = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, diagnoze.getApraksts());

            if (diagnoze.getDatums() != null) {
                ps.setDate(2, new java.sql.Date(
                        diagnoze.getDatums().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setInt(3, diagnoze.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
