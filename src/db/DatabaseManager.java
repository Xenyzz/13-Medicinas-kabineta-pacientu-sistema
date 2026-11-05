/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

/**
 *
 * @author Nikita.Sersts
 */
import java.sql.*;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import util.PasswordUtil;

public class DatabaseManager {

    private static final String URL
            = "jdbc:derby:database/med_db;create=true";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // =====================================================
    // 1. INIT - create tables only
    // =====================================================
    public static void init() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            DatabaseMetaData meta = conn.getMetaData();

            if (!tableExists(meta, "LIETOTAJS")) {
                st.executeUpdate(
                        "CREATE TABLE lietotajs ("
                        + "lietotaja_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "personas_kods_enc VARCHAR(255), "
                        + "numurs VARCHAR(50), "
                        + "parole_hash VARCHAR(255) NOT NULL, "
                        + "vards VARCHAR(100) NOT NULL, "
                        + "uzvards VARCHAR(100) NOT NULL, "
                        + "loma VARCHAR(50) NOT NULL)"
                );
            }

            if (!tableExists(meta, "ARSTA_PACIENTA_SAITES")) {
                st.executeUpdate(
                        "CREATE TABLE arsta_pacienta_saites ("
                        + "saites_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "arsta_id INT NOT NULL, "
                        + "pacienta_id INT NOT NULL, "
                        + "FOREIGN KEY (arsta_id) REFERENCES lietotajs(lietotaja_id), "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            if (!tableExists(meta, "VIZITES")) {
                st.executeUpdate(
                        "CREATE TABLE vizites ("
                        + "vizites_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "arsta_id INT NOT NULL, "
                        + "pacienta_id INT NOT NULL, "
                        + "datums_laiks TIMESTAMP NOT NULL, "
                        + "ir_apstiprinats BOOLEAN DEFAULT FALSE, "
                        + "FOREIGN KEY (arsta_id) REFERENCES lietotajs(lietotaja_id), "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            if (!tableExists(meta, "RECEPTES")) {
                st.executeUpdate(
                        "CREATE TABLE receptes ("
                        + "receptes_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "pacienta_id INT NOT NULL, "
                        + "medikamenta_nosaukums VARCHAR(255) NOT NULL, "
                        + "apraksts VARCHAR(1000), "
                        + "izsniegsanas_datums DATE, "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            if (!tableExists(meta, "DIAGNOZES")) {
                st.executeUpdate(
                        "CREATE TABLE diagnozes ("
                        + "diagnozes_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "pacienta_id INT NOT NULL, "
                        + "apraksts VARCHAR(1000), "
                        + "datums DATE, "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Database initialization error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static boolean tableExists(
            DatabaseMetaData meta, String tableName) throws SQLException {

        try (ResultSet rs = meta.getTables(
                null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    // =====================================================
    // 2. FILL - fill all tables, including admin
    // =====================================================
    public static void fillAllTables() {
        try (Connection conn = getConnection()) {

            String checkSql
                    = "SELECT COUNT(*) FROM lietotajs "
                    + "WHERE personas_kods_enc = 'admin' "
                    + "OR (vards = 'Nikita' AND uzvards = 'Sersts')";

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(checkSql)) {

                rs.next();

                if (rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Test data already exists. Clear tables first.",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }
            }

            conn.setAutoCommit(false);

            try {
                String userSql
                        = "INSERT INTO lietotajs "
                        + "(personas_kods_enc, numurs, parole_hash, "
                        + "vards, uzvards, loma) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(userSql)) {

                    // Admin
                    addUser(ps, "admin", "00000000",
                            "admin123", "Admin", "System", "Administrators");

                    // Doctors
                    addUser(ps, "010101-10001", "20000001",
                            "1234", "Janis", "Berzins", "\u0100rsts");

                    addUser(ps, "020202-20002", "20000002",
                            "1234", "Anna", "Kalnina", "\u0100rsts");

                    addUser(ps, "030303-30003", "20000003",
                            "1234", "Martins", "Ozols", "\u0100rsts");

                    // Patients
                    addUser(ps, "040404-40004", "20000004",
                            "1234", "Nikita", "Sersts", "Pacients");

                    addUser(ps, "050505-50005", "20000005",
                            "1234", "Arturs", "Kalnins", "Pacients");

                    addUser(ps, "060606-60006", "20000006",
                            "1234", "Elina", "Berzina", "Pacients");

                    addUser(ps, "070707-70007", "20000007",
                            "1234", "Karlis", "Jansons", "Pacients");

                    addUser(ps, "080808-80008", "20000008",
                            "1234", "Sofija", "Ozola", "Pacients");

                    addUser(ps, "090909-90009", "20000009",
                            "1234", "Roberts", "Vilsons", "Pacients");

                    addUser(ps, "101010-10010", "20000010",
                            "1234", "Marta", "Lapina", "Pacients");
                }

                int doctor1 = findUserId(conn, "Janis", "Berzins");
                int doctor2 = findUserId(conn, "Anna", "Kalnina");
                int doctor3 = findUserId(conn, "Martins", "Ozols");

                int patient1 = findUserId(conn, "Nikita", "Sersts");
                int patient2 = findUserId(conn, "Arturs", "Kalnins");
                int patient3 = findUserId(conn, "Elina", "Berzina");
                int patient4 = findUserId(conn, "Karlis", "Jansons");
                int patient5 = findUserId(conn, "Sofija", "Ozola");
                int patient6 = findUserId(conn, "Roberts", "Vilsons");
                int patient7 = findUserId(conn, "Marta", "Lapina");

                // Doctor-patient relationships
                String linkSql
                        = "INSERT INTO arsta_pacienta_saites "
                        + "(arsta_id, pacienta_id) VALUES (?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(linkSql)) {
                    addLink(ps, doctor1, patient1);
                    addLink(ps, doctor1, patient2);
                    addLink(ps, doctor1, patient3);
                    addLink(ps, doctor2, patient2);
                    addLink(ps, doctor2, patient4);
                    addLink(ps, doctor2, patient5);
                    addLink(ps, doctor3, patient3);
                    addLink(ps, doctor3, patient6);
                    addLink(ps, doctor3, patient7);
                    addLink(ps, doctor1, patient7);
                }

                // Appointments
                String visitSql
                        = "INSERT INTO vizites "
                        + "(arsta_id, pacienta_id, datums_laiks, "
                        + "ir_apstiprinats) VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(visitSql)) {
                    addVisit(ps, doctor1, patient1, "2026-09-01 10:00:00", true);
                    addVisit(ps, doctor1, patient2, "2026-09-03 11:30:00", true);
                    addVisit(ps, doctor1, patient3, "2026-09-05 09:00:00", false);
                    addVisit(ps, doctor2, patient2, "2026-09-08 14:00:00", true);
                    addVisit(ps, doctor2, patient4, "2026-09-10 15:30:00", true);
                    addVisit(ps, doctor2, patient5, "2026-09-12 10:30:00", false);
                    addVisit(ps, doctor3, patient3, "2026-09-15 13:00:00", true);
                    addVisit(ps, doctor3, patient6, "2026-09-17 09:30:00", true);
                    addVisit(ps, doctor3, patient7, "2026-09-20 11:00:00", false);
                    addVisit(ps, doctor1, patient7, "2026-09-22 16:00:00", true);
                    addVisit(ps, doctor1, patient1, "2026-09-25 10:00:00", true);
                    addVisit(ps, doctor2, patient2, "2026-09-27 12:00:00", true);
                    addVisit(ps, doctor3, patient3, "2026-09-29 14:30:00", false);
                    addVisit(ps, doctor1, patient5, "2026-10-01 09:00:00", true);
                    addVisit(ps, doctor2, patient4, "2026-10-03 11:30:00", true);
                }

                // Prescriptions
                String prescriptionSql
                        = "INSERT INTO receptes "
                        + "(pacienta_id, medikamenta_nosaukums, "
                        + "apraksts, izsniegsanas_datums) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(prescriptionSql)) {
                    addPrescription(ps, patient1, "Paracetamol",
                            "For pain and fever", "2026-09-01");
                    addPrescription(ps, patient2, "Ibuprofen",
                            "For pain and inflammation", "2026-09-03");
                    addPrescription(ps, patient3, "Amoxicillin",
                            "For bacterial infection", "2026-09-05");
                    addPrescription(ps, patient4, "Loratadine",
                            "For allergy symptoms", "2026-09-08");
                    addPrescription(ps, patient5, "Omeprazole",
                            "For reducing stomach acid", "2026-09-10");
                    addPrescription(ps, patient6, "Vitamin D",
                            "For vitamin D supplementation", "2026-09-12");
                    addPrescription(ps, patient7, "Ibuprofen",
                            "For pain", "2026-09-15");
                    addPrescription(ps, patient1, "Cetirizine",
                            "For allergy symptoms", "2026-09-20");
                }

                // Diagnoses
                String diagnosisSql
                        = "INSERT INTO diagnozes "
                        + "(pacienta_id, apraksts, datums) "
                        + "VALUES (?, ?, ?)";

                try (PreparedStatement ps = conn.prepareStatement(diagnosisSql)) {
                    addDiagnosis(ps, patient1, "Upper respiratory infection", "2026-09-01");
                    addDiagnosis(ps, patient2, "Headache", "2026-09-03");
                    addDiagnosis(ps, patient3, "Bacterial infection", "2026-09-05");
                    addDiagnosis(ps, patient4, "Seasonal allergy", "2026-09-08");
                    addDiagnosis(ps, patient5, "Gastritis", "2026-09-10");
                    addDiagnosis(ps, patient6, "High temperature", "2026-09-12");
                    addDiagnosis(ps, patient7, "Vitamin D deficiency", "2026-09-15");
                    addDiagnosis(ps, patient1, "Muscle pain", "2026-09-17");
                }

                conn.commit();

                JOptionPane.showMessageDialog(
                        null,
                        "All test data inserted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error inserting data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static void addUser(
            PreparedStatement ps,
            String code,
            String phone,
            String password,
            String firstName,
            String lastName,
            String role) throws SQLException {

        ps.setString(1, code);
        ps.setString(2, phone);
        ps.setString(3, PasswordUtil.hash(password));
        ps.setString(4, firstName);
        ps.setString(5, lastName);
        ps.setString(6, role);
        ps.executeUpdate();
    }

    private static int findUserId(
            Connection conn,
            String firstName,
            String lastName) throws SQLException {

        String sql
                = "SELECT lietotaja_id FROM lietotajs "
                + "WHERE vards = ? AND uzvards = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("lietotaja_id");
                }
            }
        }

        throw new SQLException(
                "User not found: " + firstName + " " + lastName);
    }

    private static void addLink(
            PreparedStatement ps, int doctorId, int patientId)
            throws SQLException {

        ps.setInt(1, doctorId);
        ps.setInt(2, patientId);
        ps.executeUpdate();
    }

    private static void addVisit(
            PreparedStatement ps,
            int doctorId,
            int patientId,
            String dateTime,
            boolean approved) throws SQLException {

        ps.setInt(1, doctorId);
        ps.setInt(2, patientId);
        ps.setTimestamp(3, Timestamp.valueOf(dateTime));
        ps.setBoolean(4, approved);
        ps.executeUpdate();
    }

    private static void addPrescription(
            PreparedStatement ps,
            int patientId,
            String medicine,
            String description,
            String date) throws SQLException {

        ps.setInt(1, patientId);
        ps.setString(2, medicine);
        ps.setString(3, description);
        ps.setDate(4, Date.valueOf(date));
        ps.executeUpdate();
    }

    private static void addDiagnosis(
            PreparedStatement ps,
            int patientId,
            String description,
            String date) throws SQLException {

        ps.setInt(1, patientId);
        ps.setString(2, description);
        ps.setDate(3, Date.valueOf(date));
        ps.executeUpdate();
    }

    // =====================================================
    // 3. CLEAR - delete all table data, including admin
    // =====================================================
    public static void clearAllTables() {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete ALL table data?",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        String[] tables = {
            "VIZITES",
            "RECEPTES",
            "DIAGNOZES",
            "ARSTA_PACIENTA_SAITES",
            "LIETOTAJS"
        };

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            conn.setAutoCommit(false);

            try {
                // Delete data in foreign-key order
                for (String table : tables) {
                    st.executeUpdate("DELETE FROM " + table);
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }

            // Reset IDENTITY counters after deleting all records
            try (Connection resetConn = getConnection(); Statement resetSt = resetConn.createStatement()) {

                resetSt.executeUpdate(
                        "ALTER TABLE VIZITES ALTER COLUMN VIZITES_ID RESTART WITH 1");

                resetSt.executeUpdate(
                        "ALTER TABLE RECEPTES ALTER COLUMN RECEPTES_ID RESTART WITH 1");

                resetSt.executeUpdate(
                        "ALTER TABLE DIAGNOZES ALTER COLUMN DIAGNOZES_ID RESTART WITH 1");

                resetSt.executeUpdate(
                        "ALTER TABLE ARSTA_PACIENTA_SAITES ALTER COLUMN SAITES_ID RESTART WITH 1");

                resetSt.executeUpdate(
                        "ALTER TABLE LIETOTAJS ALTER COLUMN LIETOTAJA_ID RESTART WITH 1");
            }

            JOptionPane.showMessageDialog(
                    null,
                    "All tables cleared and ID counters reset!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error clearing tables: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // 4. SHOW - view all tables
    // =====================================================
    public static void showDatabaseWindow() {
        
         JOptionPane.showMessageDialog(
                null,
                "Test accounts:\n"
                + "Admin: admin / admin123\n"
                + "Doctors and patients password: 1234",
                "Test accounts",
                JOptionPane.INFORMATION_MESSAGE
        );
         
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Database Viewer");
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTabbedPane tabs = new JTabbedPane();

            String[] tables = {
                "lietotajs",
                "arsta_pacienta_saites",
                "vizites",
                "receptes",
                "diagnozes"
            };

            try (Connection conn = getConnection()) {
                for (String table : tables) {
                    try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM " + table)) {

                        ResultSetMetaData meta = rs.getMetaData();
                        int columnCount = meta.getColumnCount();

                        DefaultTableModel model = new DefaultTableModel();

                        for (int i = 1; i <= columnCount; i++) {
                            model.addColumn(meta.getColumnName(i));
                        }

                        while (rs.next()) {
                            Object[] row = new Object[columnCount];

                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }

                            model.addRow(row);
                        }

                        JTable tableView = new JTable(model);
                        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                        tabs.addTab(table, new JScrollPane(tableView));
                    }
                }

                frame.add(tabs, BorderLayout.CENTER);
                frame.setVisible(true);

            } catch (SQLException e) {
                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        frame,
                        "Error displaying database: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
