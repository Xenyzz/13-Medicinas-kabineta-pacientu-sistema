
package dao;

import java.util.Date;
import java.util.List;
import model.Diagnoze;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnozeDAOTest extends DaoTestBase {

    private final DiagnozeDAO dao = new DiagnozeDAO();

    @Test
    public void testAddDiagnosis() {
        Diagnoze diagnosis = new Diagnoze(
                0, patientId, "Test diagnosis", new Date()
        );

        assertTrue(dao.add(diagnosis));

        List<Diagnoze> result =
                dao.getDiagnozesByPacientsId(patientId);

        assertEquals(1, result.size());
        assertEquals("Test diagnosis",
                result.get(0).getApraksts());
    }

    @Test
    public void testGetDiagnosesByPatientId() {
        dao.add(new Diagnoze(
                0, patientId, "First diagnosis", new Date()
        ));

        dao.add(new Diagnoze(
                0, patientId, "Second diagnosis", new Date()
        ));

        List<Diagnoze> result =
                dao.getDiagnozesByPacientsId(patientId);

        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateDiagnosis() {
        dao.add(new Diagnoze(
                0, patientId, "Old diagnosis", new Date()
        ));

        List<Diagnoze> result =
                dao.getDiagnozesByPacientsId(patientId);

        Diagnoze diagnosis = result.get(0);
        diagnosis.setApraksts("Updated diagnosis");

        assertTrue(dao.updateDiagnoze(diagnosis));

        List<Diagnoze> updated =
                dao.getDiagnozesByPacientsId(patientId);

        assertEquals("Updated diagnosis",
                updated.get(0).getApraksts());
    }

    @Test
    public void testUpdateNonExistingDiagnosis() {
        Diagnoze diagnosis = new Diagnoze(
                999999, patientId, "Does not exist", new Date()
        );

        assertFalse(dao.updateDiagnoze(diagnosis));
    }

    @Test
    public void testGetDiagnosesForPatientWithoutDiagnoses() {
        List<Diagnoze> result =
                dao.getDiagnozesByPacientsId(patientId);

        assertTrue(result.isEmpty());
    }
}
