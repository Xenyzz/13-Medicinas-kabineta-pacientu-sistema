
package dao;

import java.util.Date;
import java.util.List;
import model.Recepte;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecepteDAOTest extends DaoTestBase {

    private final RecepteDAO dao = new RecepteDAO();

    @Test
    public void testAddPrescription() {
        Recepte prescription = new Recepte(
                0,
                patientId,
                "Test medicine",
                "Test description",
                new Date()
        );

        dao.add(prescription);

        List<Recepte> result =
                dao.getReceptesByPacientsId(patientId);

        assertEquals(1, result.size());
        assertEquals("Test medicine",
                result.get(0).getMedikamentaNosaukums());
        assertEquals("Test description",
                result.get(0).getApraksts());
    }

    @Test
    public void testGetPrescriptionsByPatientId() {
        dao.add(new Recepte(
                0, patientId, "Medicine A", "Description A", new Date()
        ));

        dao.add(new Recepte(
                0, patientId, "Medicine B", "Description B", new Date()
        ));

        List<Recepte> result =
                dao.getReceptesByPacientsId(patientId);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetPrescriptionsForPatientWithoutPrescriptions() {
        List<Recepte> result =
                dao.getReceptesByPacientsId(patientId);

        assertTrue(result.isEmpty());
    }
}
