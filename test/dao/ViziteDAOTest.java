package dao;

import db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import model.Vizite;
import org.junit.Test;
import static org.junit.Assert.*;

public class ViziteDAOTest extends DaoTestBase {

    private final ViziteDAO dao = new ViziteDAO();

    private Date futureDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    private int addTestVisit(Date date) {
        Vizite visit = new Vizite(
                0, patientId, doctorId, date, false
        );

        assertTrue(dao.addVizite(visit));

        List<Vizite> visits
                = dao.getVisitsByPatientId(patientId);

        assertFalse(visits.isEmpty());

        return visits.get(visits.size() - 1).getId();
    }

    @Test
    public void testAddVisit() {
        Vizite visit = new Vizite(
                0, patientId, doctorId, futureDate(5), false
        );

        assertTrue(dao.addVizite(visit));
        assertNotNull(visit.getDatumsLaiks());
        assertFalse(visit.isIrApstiprinats());
    }

    @Test
    public void testAddVisitWithNull() {
        assertFalse(dao.addVizite(null));
    }

    @Test
    public void testAddVisitWithNullDate() {
        Vizite visit = new Vizite(
                0, patientId, doctorId, null, false
        );

        assertFalse(dao.addVizite(visit));
    }

    @Test
    public void testAddVisitWithPastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);

        Vizite visit = new Vizite(
                0, patientId, doctorId,
                calendar.getTime(), false
        );

        assertFalse(dao.addVizite(visit));
    }

    @Test
    public void testAddVisitWithoutDoctorPatientConnection() {
        Vizite visit = new Vizite(
                0, patientId, 999999, futureDate(5), false
        );

        assertFalse(dao.addVizite(visit));
    }

    @Test
    public void testDoctorCannotHaveTwoVisitsOnSameDay() {
        Date date = futureDate(6);

        Vizite first = new Vizite(
                0, patientId, doctorId, date, false
        );

        assertTrue(dao.addVizite(first));

        Vizite second = new Vizite(
                0, patientId, doctorId, date, false
        );

        assertFalse(dao.addVizite(second));
    }

    @Test
    public void testGetVisitsByPatientId() {
        addTestVisit(futureDate(7));

        List<Vizite> visits
                = dao.getVisitsByPatientId(patientId);

        assertEquals(1, visits.size());
        assertEquals(patientId, visits.get(0).getPacientsId());
    }

    @Test
    public void testGetVisitsByDoctorId() {
        addTestVisit(futureDate(8));

        List<Vizite> visits
                = dao.getVisitsByDoctorId(doctorId);

        assertEquals(1, visits.size());
        assertEquals(doctorId, visits.get(0).getArstsId());
    }

    @Test
    public void testIsDateAvailable() {
        Date date = futureDate(9);

        assertTrue(dao.isDateAvailable(doctorId, date));

        assertTrue(dao.addVizite(
                new Vizite(0, patientId, doctorId, date, false)
        ));

        assertFalse(dao.isDateAvailable(doctorId, date));
    }

    @Test
    public void testIsDateAvailableWithNull() {
        assertFalse(dao.isDateAvailable(doctorId, null));
    }

    @Test
    public void testGetOccupiedDates() {
        Date date = futureDate(10);

        assertTrue(dao.addVizite(
                new Vizite(0, patientId, doctorId, date, false)
        ));

        List<Date> dates = dao.getOccupiedDates(doctorId);

        assertFalse(dates.isEmpty());
    }

    @Test
    public void testConfirmUpcomingVisit() {
        int visitId = addTestVisit(futureDate(11));

        assertTrue(dao.confirmVisit(visitId, doctorId));

        List<Vizite> visits
                = dao.getVisitsByPatientId(patientId);

        assertTrue(visits.get(0).isIrApstiprinats());
    }

    @Test
    public void testCannotConfirmVisitTwice() {
        int visitId = addTestVisit(futureDate(12));

        assertTrue(dao.confirmVisit(visitId, doctorId));

        assertFalse(dao.confirmVisit(visitId, doctorId));
    }

    @Test
    public void testCancelUpcomingVisit() {
        int visitId = addTestVisit(futureDate(13));

        assertTrue(dao.cancelUpcomingVisit(visitId, patientId));

        assertTrue(dao.getVisitsByPatientId(patientId).isEmpty());
    }

    @Test
    public void testCannotCancelVisitOfAnotherPatient() {
        int visitId = addTestVisit(futureDate(14));

        assertFalse(dao.cancelUpcomingVisit(visitId, 999999));
    }

    @Test
    public void testGetPastVisitsByPatientId() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);

        String sql = "INSERT INTO vizites "
                + "(arsta_id, pacienta_id, datums_laiks, ir_apstiprinats) "
                + "VALUES (?, ?, ?, TRUE)";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setInt(2, patientId);
            ps.setTimestamp(3,
                    new Timestamp(calendar.getTimeInMillis()));

            ps.executeUpdate();
        }

        List<Object[]> visits
                = dao.getPastVisitsByPatientId(patientId);

        assertEquals(1, visits.size());
        assertEquals("Doctor Test", visits.get(0)[2]);
    }
}
