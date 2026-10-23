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

        String role = lietotajs.getLoma();

        if (!"Pacients".equals(role) && !"Arsts".equals(role)) {
            return false;
        }

        if (password == null || password.length() < 4) {
            return false;
        }

        String sql = "INSERT INTO lietotajs "
                + "(vards, uzvards, personas_kods_enc, "
                + "parole_hash, loma, numurs) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {

            String checkSql = "SELECT lietotaja_id FROM lietotajs "
                    + "WHERE personas_kods_enc = ?";

            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, lietotajs.getPersonasKodsEnc());

                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, lietotajs.getVards());
                ps.setString(2, lietotajs.getUzvards());
                ps.setString(3, lietotajs.getPersonasKodsEnc());
                ps.setString(4, PasswordUtil.hash(password));
                ps.setString(5, role);
                ps.setString(6, lietotajs.getNumurs());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Lietotajs findDoctorByPatientId(int pacientsId) {
        String sql
                = "SELECT l.lietotaja_id, l.vards, l.uzvards, l.loma "
                + "FROM lietotajs l "
                + "JOIN arsta_pacienta_saites a "
                + "ON l.lietotaja_id = a.arsta_id "
                + "WHERE a.pacienta_id = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacientsId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lietotajs doctor = new Lietotajs();
                    doctor.setId(rs.getInt("lietotaja_id"));
                    doctor.setVards(rs.getString("vards"));
                    doctor.setUzvards(rs.getString("uzvards"));
                    doctor.setLoma(rs.getString("loma"));

                    return doctor;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Lietotajs login(String personalCode, String password) {

        String sql = "SELECT * FROM lietotajs WHERE personas_kods_enc = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, personalCode);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String savedHash = rs.getString("parole_hash");
                    String enteredHash = PasswordUtil.hash(password);

                    if (savedHash.equals(enteredHash)) {
                        Lietotajs user = new Lietotajs();

                        user.setId(rs.getInt("lietotaja_id"));
                        user.setVards(rs.getString("vards"));
                        user.setUzvards(rs.getString("uzvards"));
                        user.setPersonasKodsEnc(
                                rs.getString("personas_kods_enc"));
                        user.setParoleHash(savedHash);
                        user.setLoma(rs.getString("loma"));
                        user.setNumurs(rs.getString("numurs"));

                        return user;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Lietotajs findById(int id) {

        String sql = "SELECT lietotaja_id, vards, uzvards, "
                + "personas_kods_enc, parole_hash, loma, numurs "
                + "FROM lietotajs WHERE lietotaja_id = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lietotajs user = new Lietotajs();

                    user.setId(rs.getInt("lietotaja_id"));
                    user.setVards(rs.getString("vards"));
                    user.setUzvards(rs.getString("uzvards"));
                    user.setPersonasKodsEnc(
                            rs.getString("personas_kods_enc"));
                    user.setParoleHash(rs.getString("parole_hash"));
                    user.setLoma(rs.getString("loma"));
                    user.setNumurs(rs.getString("numurs"));

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteUser(String username) {
        return false;
    }

    public boolean assignDoctorToPatient(int pacientsId, int arstsId) {

        String patientSql
                = "SELECT lietotaja_id FROM lietotajs "
                + "WHERE lietotaja_id = ? AND loma = 'Pacients'";

        String doctorSql
                = "SELECT lietotaja_id FROM lietotajs "
                + "WHERE lietotaja_id = ? AND loma IN ('Arsts', 'Ārsts')";

        String checkSql
                = "SELECT arsta_id FROM arsta_pacienta_saites "
                + "WHERE pacienta_id = ?";

        String insertSql
                = "INSERT INTO arsta_pacienta_saites "
                + "(arsta_id, pacienta_id) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {

            // 1. Pārbaude, vai pacients eksistē
            try (PreparedStatement ps = conn.prepareStatement(patientSql)) {
                ps.setInt(1, pacientsId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                }
            }

            // 2. Pārbaudiet, vai ārsts pastāv
            try (PreparedStatement ps = conn.prepareStatement(doctorSql)) {
                ps.setInt(1, arstsId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                }
            }

            // 3. Pārbaude, vai pacientam ir nozīmēts ārsts
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, pacientsId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Пациент уже связан с врачом
                        return false;
                    }
                }
            }

            // 4. Jaunā savienojuma saglabāšana
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, arstsId);
                ps.setInt(2, pacientsId);

                return ps.executeUpdate() == 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<Lietotajs> findUsersByRole(String role) {
        java.util.List<Lietotajs> users = new java.util.ArrayList<>();

        String sql = "SELECT lietotaja_id, vards, uzvards, "
                + "personas_kods_enc, loma "
                + "FROM lietotajs WHERE loma = ? "
                + "ORDER BY uzvards, vards";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lietotajs user = new Lietotajs();
                    user.setId(rs.getInt("lietotaja_id"));
                    user.setVards(rs.getString("vards"));
                    user.setUzvards(rs.getString("uzvards"));
                    user.setPersonasKodsEnc(
                            rs.getString("personas_kods_enc"));
                    user.setLoma(rs.getString("loma"));

                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

}
