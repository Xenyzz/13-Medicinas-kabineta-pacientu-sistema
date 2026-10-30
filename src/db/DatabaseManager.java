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
import util.PasswordUtil;

public class DatabaseManager {

    private static final String URL = "jdbc:derby:database/med_db;create=true";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            System.out.println("Database connected sucsessfuly");
            //skatam vai viss ir labi ar datubasi

            ResultSet lietotajs = meta.getTables(null, null, "LIETOTAJS", null);
            if (!lietotajs.next()) {
                conn.createStatement().executeUpdate(
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

            ResultSet saites = meta.getTables(null, null, "ARSTA_PACIENTA_SAITES", null);
            if (!saites.next()) {
                conn.createStatement().executeUpdate(
                        "CREATE TABLE arsta_pacienta_saites ("
                        + "saites_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "arsta_id INT NOT NULL, "
                        + "pacienta_id INT NOT NULL, "
                        + "FOREIGN KEY (arsta_id) REFERENCES lietotajs(lietotaja_id), "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            ResultSet vizites = meta.getTables(null, null, "VIZITES", null);
            if (!vizites.next()) {
                conn.createStatement().executeUpdate(
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

            ResultSet receptes = meta.getTables(null, null, "RECEPTES", null);
            if (!receptes.next()) {
                conn.createStatement().executeUpdate(
                        "CREATE TABLE receptes ("
                        + "receptes_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "pacienta_id INT NOT NULL, "
                        + "medikamenta_nosaukums VARCHAR(255) NOT NULL, "
                        + "apraksts VARCHAR(1000), "
                        + "izsniegsanas_datums DATE, "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            ResultSet diagnozes = meta.getTables(null, null, "DIAGNOZES", null);
            if (!diagnozes.next()) {
                conn.createStatement().executeUpdate(
                        "CREATE TABLE diagnozes ("
                        + "diagnozes_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "pacienta_id INT NOT NULL, "
                        + "apraksts VARCHAR(1000), "
                        + "datums DATE, "
                        + "FOREIGN KEY (pacienta_id) REFERENCES lietotajs(lietotaja_id))"
                );
            }

            ResultSet checkUsers = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM lietotajs"
            );

            checkUsers.next();
            if (checkUsers.getInt(1) > 0) {
                return;
            }

            String sql
                    = "INSERT INTO lietotajs "
                    + "(personas_kods_enc, numurs, parole_hash, vards, uzvards, loma) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, "010101-10001");
                ps.setString(2, "20000001");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Jānis");
                ps.setString(5, "Bērziņš");
                ps.setString(6, "Ārsts");
                ps.executeUpdate();

                ps.setString(1, "020202-20002");
                ps.setString(2, "20000002");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Anna");
                ps.setString(5, "Kalniņa");
                ps.setString(6, "Ārsts");
                ps.executeUpdate();

                ps.setString(1, "030303-30003");
                ps.setString(2, "20000003");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Mārtiņš");
                ps.setString(5, "Ozols");
                ps.setString(6, "Ārsts");
                ps.executeUpdate();

                ps.setString(1, "040404-40004");
                ps.setString(2, "20000004");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Nikita");
                ps.setString(5, "Sersts");
                ps.setString(6, "Pacients");
                ps.executeUpdate();

                ps.setString(1, "050505-50005");
                ps.setString(2, "20000005");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Artūrs");
                ps.setString(5, "Kalniņš");
                ps.setString(6, "Pacients");
                ps.executeUpdate();

                ps.setString(1, "060606-60006");
                ps.setString(2, "20000006");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Elīna");
                ps.setString(5, "Bērziņa");
                ps.setString(6, "Pacients");
                ps.executeUpdate();

                ps.setString(1, "070707-70007");
                ps.setString(2, "20000007");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Kārlis");
                ps.setString(5, "Jansons");
                ps.setString(6, "Pacients");
                ps.executeUpdate();

                ps.setString(1, "080808-80008");
                ps.setString(2, "20000008");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Sofija");
                ps.setString(5, "Ozola");
                ps.setString(6, "Pacients");
                ps.executeUpdate();

                ps.setString(1, "090909-90009");
                ps.setString(2, "20000009");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Roberts");
                ps.setString(5, "Vilsons");
                ps.setString(6, "Pacients");
                ps.executeUpdate();

                ps.setString(1, "101010-10010");
                ps.setString(2, "20000010");
                ps.setString(3, PasswordUtil.hash("1234"));
                ps.setString(4, "Marta");
                ps.setString(5, "Lapiņa");
                ps.setString(6, "Pacients");
                ps.executeUpdate();
            }

            Statement st = conn.createStatement();

            st.executeUpdate(
                    "INSERT INTO arsta_pacienta_saites "
                    + "(arsta_id, pacienta_id) VALUES "
                    + "(1, 4), "
                    + "(1, 5), "
                    + "(1, 6), "
                    + "(2, 5), "
                    + "(2, 7), "
                    + "(2, 8), "
                    + "(3, 6), "
                    + "(3, 9), "
                    + "(3, 10), "
                    + "(1, 10)"
            );

            st.executeUpdate(
                    "INSERT INTO vizites "
                    + "(arsta_id, pacienta_id, datums_laiks, ir_apstiprinats) VALUES "
                    + "(1, 4, TIMESTAMP('2026-09-01 10:00:00'), TRUE), "
                    + "(1, 5, TIMESTAMP('2026-09-03 11:30:00'), TRUE), "
                    + "(1, 6, TIMESTAMP('2026-09-05 09:00:00'), FALSE), "
                    + "(2, 5, TIMESTAMP('2026-09-08 14:00:00'), TRUE), "
                    + "(2, 7, TIMESTAMP('2026-09-10 15:30:00'), TRUE), "
                    + "(2, 8, TIMESTAMP('2026-09-12 10:30:00'), FALSE), "
                    + "(3, 6, TIMESTAMP('2026-09-15 13:00:00'), TRUE), "
                    + "(3, 9, TIMESTAMP('2026-09-17 09:30:00'), TRUE), "
                    + "(3, 10, TIMESTAMP('2026-09-20 11:00:00'), FALSE), "
                    + "(1, 10, TIMESTAMP('2026-09-22 16:00:00'), TRUE), "
                    + "(1, 4, TIMESTAMP('2026-09-25 10:00:00'), TRUE), "
                    + "(2, 5, TIMESTAMP('2026-09-27 12:00:00'), TRUE), "
                    + "(3, 6, TIMESTAMP('2026-09-29 14:30:00'), FALSE), "
                    + "(1, 8, TIMESTAMP('2026-10-01 09:00:00'), TRUE), "
                    + "(2, 7, TIMESTAMP('2026-10-03 11:30:00'), TRUE)"
            );

            st.executeUpdate(
                    "INSERT INTO receptes "
                    + "(pacienta_id, medikamenta_nosaukums, apraksts, izsniegsanas_datums) VALUES "
                    + "(4, 'Paracetamols', 'Sāpju un temperatūras mazināšanai', DATE('2026-09-01')), "
                    + "(5, 'Ibuprofēns', 'Pret sāpēm un iekaisumu', DATE('2026-09-03')), "
                    + "(6, 'Amoksicilīns', 'Bakteriālas infekcijas ārstēšanai', DATE('2026-09-05')), "
                    + "(7, 'Loratadīns', 'Alerģijas simptomu mazināšanai', DATE('2026-09-08')), "
                    + "(8, 'Omeprazols', 'Kuņģa skābes samazināšanai', DATE('2026-09-10')), "
                    + "(9, 'Paracetamols', 'Temperatūras samazināšanai', DATE('2026-09-12')), "
                    + "(10, 'D vitamīns', 'D vitamīna līmeņa papildināšanai', DATE('2026-09-15')), "
                    + "(4, 'Ibuprofēns', 'Pret sāpēm', DATE('2026-09-17')), "
                    + "(5, 'Cetirizīns', 'Alerģijas simptomu mazināšanai', DATE('2026-09-20')), "
                    + "(6, 'Magnijs', 'Magnija papildināšanai', DATE('2026-09-22')), "
                    + "(7, 'Loratadīns', 'Sezonālas alerģijas ārstēšanai', DATE('2026-09-25')), "
                    + "(8, 'Paracetamols', 'Sāpju mazināšanai', DATE('2026-09-27')), "
                    + "(9, 'Omeprazols', 'Kuņģa problēmu ārstēšanai', DATE('2026-09-29')), "
                    + "(10, 'D vitamīns', 'Vitamīna D papildināšanai', DATE('2026-10-01')), "
                    + "(4, 'Ibuprofēns', 'Pret iekaisumu un sāpēm', DATE('2026-10-03'))"
            );

            st.executeUpdate(
                    "INSERT INTO diagnozes "
                    + "(pacienta_id, apraksts, datums) VALUES "
                    + "(4, 'Akūta augšējo elpceļu infekcija', DATE('2026-09-01')), "
                    + "(5, 'Galvassāpes', DATE('2026-09-03')), "
                    + "(6, 'Bakteriāla infekcija', DATE('2026-09-05')), "
                    + "(7, 'Sezonāla alerģija', DATE('2026-09-08')), "
                    + "(8, 'Gastrīts', DATE('2026-09-10')), "
                    + "(9, 'Paaugstināta temperatūra', DATE('2026-09-12')), "
                    + "(10, 'D vitamīna deficīts', DATE('2026-09-15')), "
                    + "(4, 'Muskuļu sāpes', DATE('2026-09-17')), "
                    + "(5, 'Alerģisks rinīts', DATE('2026-09-20')), "
                    + "(6, 'Nogurums', DATE('2026-09-22')), "
                    + "(7, 'Sezonāla alerģija', DATE('2026-09-25')), "
                    + "(8, 'Vīrusu infekcija', DATE('2026-09-27')), "
                    + "(9, 'Kuņģa iekaisums', DATE('2026-09-29')), "
                    + "(10, 'Vitamīna D deficīts', DATE('2026-10-01')), "
                    + "(4, 'Muguras sāpes', DATE('2026-10-03'))"
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static void printDatabase() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            System.out.println("===== LIETOTAJS =====");

            ResultSet rs = st.executeQuery(
                    "SELECT * FROM lietotajs"
            );

            while (rs.next()) {
                System.out.println(
                        rs.getInt("lietotaja_id") + " | "
                        + rs.getString("vards") + " "
                        + rs.getString("uzvards") + " | "
                        + rs.getString("numurs") + " | "
                        + rs.getString("loma")
                );
            }

            System.out.println("\n===== ARSTA_PACIENTA_SAITES =====");

            rs = st.executeQuery(
                    "SELECT * FROM arsta_pacienta_saites"
            );

            while (rs.next()) {
                System.out.println(
                        rs.getInt("saites_id") + " | Ārsts: "
                        + rs.getInt("arsta_id") + " | Pacients: "
                        + rs.getInt("pacienta_id")
                );
            }

            System.out.println("\n===== VIZITES =====");

            rs = st.executeQuery(
                    "SELECT * FROM vizites"
            );

            while (rs.next()) {
                System.out.println(
                        rs.getInt("vizites_id") + " | Ārsts: "
                        + rs.getInt("arsta_id") + " | Pacients: "
                        + rs.getInt("pacienta_id") + " | "
                        + rs.getTimestamp("datums_laiks") + " | Apstiprināts: "
                        + rs.getBoolean("ir_apstiprinats")
                );
            }

            System.out.println("\n===== RECEPTES =====");

            rs = st.executeQuery(
                    "SELECT * FROM receptes"
            );

            while (rs.next()) {
                System.out.println(
                        rs.getInt("receptes_id") + " | Pacients: "
                        + rs.getInt("pacienta_id") + " | "
                        + rs.getString("medikamenta_nosaukums") + " | "
                        + rs.getString("apraksts") + " | "
                        + rs.getDate("izsniegsanas_datums")
                );
            }

            System.out.println("\n===== DIAGNOZES =====");

            rs = st.executeQuery(
                    "SELECT * FROM diagnozes"
            );

            while (rs.next()) {
                System.out.println(
                        rs.getInt("diagnozes_id") + " | Pacients: "
                        + rs.getInt("pacienta_id") + " | "
                        + rs.getString("apraksts") + " | "
                        + rs.getDate("datums")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
