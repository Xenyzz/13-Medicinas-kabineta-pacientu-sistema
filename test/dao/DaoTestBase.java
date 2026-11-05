
package dao;

import db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.After;
import org.junit.Before;
import util.PasswordUtil;

public class DaoTestBase {

    protected int doctorId;
    protected int patientId;

    protected String doctorCode;
    protected String patientCode;

    @Before
    public void setUp() throws Exception {
        DatabaseManager.init();

        String unique = String.valueOf(System.nanoTime());

        doctorCode = "DOC" + unique;
        patientCode = "PAT" + unique;

        doctorId = createUser(
                "Doctor", "Test", doctorCode, "Arsts"
        );

        patientId = createUser(
                "Patient", "Test", patientCode, "Pacients"
        );

        String sql = "INSERT INTO arsta_pacienta_saites "
                + "(arsta_id, pacienta_id) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setInt(2, patientId);
            ps.executeUpdate();
        }
    }

    private int createUser(
            String name, String surname, String code, String role)
            throws Exception {

        String sql = "INSERT INTO lietotajs "
                + "(vards, uzvards, personas_kods_enc, "
                + "parole_hash, loma, numurs) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, code);
            ps.setString(4, PasswordUtil.hash("12345"));
            ps.setString(5, role);
            ps.setString(6, "20000000");

            ps.executeUpdate();
        }

        String selectSql =
                "SELECT lietotaja_id FROM lietotajs "
                + "WHERE personas_kods_enc = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new Exception("Testa lietotāju neizdevās izveidot.");
    }

    @After
    public void tearDown() throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {

            deleteById(conn, "vizites", "pacienta_id", patientId);
            deleteById(conn, "receptes", "pacienta_id", patientId);
            deleteById(conn, "diagnozes", "pacienta_id", patientId);

            String linkSql =
                    "DELETE FROM arsta_pacienta_saites "
                    + "WHERE pacienta_id = ? AND arsta_id = ?";

            try (PreparedStatement ps = conn.prepareStatement(linkSql)) {
                ps.setInt(1, patientId);
                ps.setInt(2, doctorId);
                ps.executeUpdate();
            }

            deleteById(conn, "lietotajs", "lietotaja_id", patientId);
            deleteById(conn, "lietotajs", "lietotaja_id", doctorId);
        }
    }

    private void deleteById(
            Connection conn, String table, String column, int id)
            throws Exception {

        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
