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
import model.Recepte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO klase recepšu datu apstrādei.
 * Nodrošina jaunu recepšu pievienošanu un
 * recepšu iegūšanu konkrētam pacientam.
 *
 * @author Nikita.Sersts
 */
public class RecepteDAO {

    /**
     * Pievieno jaunu recepti datubāzē.
     *
     * @param recepte pievienojamās receptes objekts
     * @return true, ja recepte tika veiksmīgi pievienota,
     *         pretējā gadījumā false
     */
    public boolean add(Recepte recepte) {

        String sql = "INSERT INTO receptes "
                + "(pacienta_id, medikamenta_nosaukums, apraksts, izsniegsanas_datums) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recepte.getPacientsId());
            ps.setString(2, recepte.getMedikamentaNosaukums());
            ps.setString(3, recepte.getApraksts());

            if (recepte.getIzsniegsanasDatums() != null) {
                ps.setDate(4, new java.sql.Date(
                        recepte.getIzsniegsanasDatums().getTime()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Iegūst visas konkrētam pacientam izrakstītās receptes.
     * Receptes tiek sakārtotas pēc izsniegšanas datuma
     * dilstošā secībā.
     *
     * @param pacientsId pacienta unikālais ID
     * @return pacienta recepšu saraksts
     */
    public List<Recepte> getReceptesByPacientsId(int pacientsId) {

        List<Recepte> receptes = new ArrayList<>();

        String sql = "SELECT receptes_id, pacienta_id, "
                + "medikamenta_nosaukums, apraksts, izsniegsanas_datums "
                + "FROM receptes "
                + "WHERE pacienta_id = ? "
                + "ORDER BY izsniegsanas_datums DESC";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacientsId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Recepte recepte = new Recepte();

                    recepte.setId(rs.getInt("receptes_id"));
                    recepte.setPacientsId(rs.getInt("pacienta_id"));

                    recepte.setMedikamentaNosaukums(
                            rs.getString("medikamenta_nosaukums"));

                    recepte.setApraksts(
                            rs.getString("apraksts"));

                    java.sql.Date datums
                            = rs.getDate("izsniegsanas_datums");

                    if (datums != null) {
                        recepte.setIzsniegsanasDatums(
                                new java.util.Date(datums.getTime()));
                    }

                    receptes.add(recepte);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receptes;
    }
}