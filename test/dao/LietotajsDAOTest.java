
package dao;

import db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Lietotajs;
import org.junit.Test;
import static org.junit.Assert.*;

public class LietotajsDAOTest extends DaoTestBase {

    private final LietotajsDAO dao = new LietotajsDAO();

    @Test
    public void testRegisterPatient() throws Exception {
        String code = "REG" + System.nanoTime();

        Lietotajs user = new Lietotajs(
                0, "Test", "User", code, null, "Pacients", "20000001"
        );

        try {
            assertTrue(dao.register(user, "12345"));

            Lietotajs found = findUserByCode(code);

            assertNotNull(found);
            assertEquals("Test", found.getVards());
            assertEquals("User", found.getUzvards());

        } finally {
            deleteUserByCode(code);
        }
    }

    @Test
    public void testRegisterWithInvalidRole() {
        Lietotajs user = new Lietotajs(
                0, "Test", "Admin", "ADMIN" + System.nanoTime(),
                null, "Administrators", "20000002"
        );

        assertFalse(dao.register(user, "12345"));
    }

    @Test
    public void testRegisterWithShortPassword() {
        Lietotajs user = new Lietotajs(
                0, "Test", "User", "SHORT" + System.nanoTime(),
                null, "Pacients", "20000003"
        );

        assertFalse(dao.register(user, "123"));
    }

    @Test
    public void testLoginWithCorrectPassword() {
        Lietotajs user = dao.login(patientCode, "12345");

        assertNotNull(user);
        assertEquals(patientId, user.getId());
    }

    @Test
    public void testLoginWithWrongPassword() {
        assertNull(dao.login(patientCode, "wrong"));
    }

    @Test
    public void testFindById() {
        Lietotajs user = dao.findById(patientId);

        assertNotNull(user);
        assertEquals(patientId, user.getId());
        assertEquals("Patient", user.getVards());
        assertEquals("Pacients", user.getLoma());
    }

    @Test
    public void testFindNonExistingUser() {
        assertNull(dao.findById(999999));
    }

    @Test
    public void testFindDoctorByPatientId() {
        Lietotajs doctor = dao.findDoctorByPatientId(patientId);

        assertNotNull(doctor);
        assertEquals(doctorId, doctor.getId());
        assertEquals("Doctor", doctor.getVards());
    }

    @Test
    public void testAssignDoctorToPatient() throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM arsta_pacienta_saites "
                     + "WHERE pacienta_id = ? AND arsta_id = ?")) {

            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.executeUpdate();
        }

        assertTrue(dao.assignDoctorToPatient(patientId, doctorId));
    }

    @Test
    public void testAssignDoctorToAlreadyAssignedPatient() {
        assertFalse(dao.assignDoctorToPatient(patientId, doctorId));
    }

    @Test
    public void testAssignDoctorWithInvalidPatient() {
        assertFalse(dao.assignDoctorToPatient(999999, doctorId));
    }

    @Test
    public void testFindUsersByRole() {
        assertFalse(dao.findUsersByRole("Pacients").isEmpty());
    }

    @Test
    public void testGetPatientsByDoctorId() {
        assertFalse(dao.getPatientsByDoctorId(doctorId).isEmpty());
    }

    private Lietotajs findUserByCode(String code) throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT lietotaja_id FROM lietotajs "
                     + "WHERE personas_kods_enc = ?")) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return dao.findById(rs.getInt(1));
                }
            }
        }

        return null;
    }

    private void deleteUserByCode(String code) throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM lietotajs WHERE personas_kods_enc = ?")) {

            ps.setString(1, code);
            ps.executeUpdate();
        }
    }
}
