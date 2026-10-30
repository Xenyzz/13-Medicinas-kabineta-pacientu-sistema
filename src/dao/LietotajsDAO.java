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
import model.Lietotajs;
import util.PasswordUtil;
import java.sql.*;

public class LietotajsDAO {

    public boolean register(Lietotajs lietotajs, String password) {

        String sql
                = "INSERT INTO lietotajs "
                + "(vards, uzvards, personas_kods_enc, parole_hash, loma, numurs) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lietotajs.getVards());
            ps.setString(2, lietotajs.getUzvards());
            ps.setString(3, lietotajs.getPersonasKodsEnc());
            ps.setString(4, PasswordUtil.hash(password));
            ps.setString(5, lietotajs.getLoma());
            ps.setString(6, lietotajs.getNumurs());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int login(String numurs, String password) {

        String sql
                = "SELECT lietotaja_id FROM lietotajs "
                + "WHERE numurs=? AND parole_hash=?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numurs);
            ps.setString(2, PasswordUtil.hash(password));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("lietotaja_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Lietotajs findById(int id) {

        return null;
    }

    public boolean deleteUser(String username) {

        return false;
    }

    public boolean assignDoctorToPatient(int pacientsId, int arstsId) {

        return false;
    }
}
