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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import model.Vizite;

public class ViziteDAO {

    public List<Vizite> getVisitsByPatientId(int pacientsId) {
        List<Vizite> visits = new ArrayList<>();

        String sql = "SELECT vizites_id, pacienta_id, arsta_id, "
                + "datums_laiks, ir_apstiprinats "
                + "FROM vizites "
                + "WHERE pacienta_id = ? "
                + "ORDER BY datums_laiks";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacientsId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vizite visit = new Vizite();

                    visit.setId(rs.getInt("vizites_id"));
                    visit.setPacientsId(rs.getInt("pacienta_id"));
                    visit.setArstsId(rs.getInt("arsta_id"));
                    visit.setDatumsLaiks(rs.getTimestamp("datums_laiks"));
                    visit.setIrApstiprinats(
                            rs.getBoolean("ir_apstiprinats"));

                    visits.add(visit);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visits;
    }

    public List<Vizite> getVisitsByDoctorId(int arstsId) {
        List<Vizite> visits = new ArrayList<>();

        String sql = "SELECT vizites_id, pacienta_id, arsta_id, "
                + "datums_laiks, ir_apstiprinats "
                + "FROM vizites "
                + "WHERE arsta_id = ? "
                + "ORDER BY datums_laiks";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, arstsId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vizite visit = new Vizite();

                    visit.setId(rs.getInt("vizites_id"));
                    visit.setPacientsId(rs.getInt("pacienta_id"));
                    visit.setArstsId(rs.getInt("arsta_id"));
                    visit.setDatumsLaiks(rs.getTimestamp("datums_laiks"));
                    visit.setIrApstiprinats(
                            rs.getBoolean("ir_apstiprinats"));

                    visits.add(visit);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visits;
    }

    public List<Date> getOccupiedDates(int arstsId) {
        List<Date> dates = new ArrayList<>();

        String sql = "SELECT datums_laiks FROM vizites WHERE arsta_id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, arstsId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("datums_laiks");

                    if (timestamp != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(timestamp);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        dates.add(cal.getTime());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dates;
    }

    public boolean isDateAvailable(int arstsId, Date datums) {
        if (datums == null) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(datums);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Timestamp start = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Timestamp end = new Timestamp(cal.getTimeInMillis());

        String sql = "SELECT COUNT(*) FROM vizites "
                + "WHERE arsta_id = ? "
                + "AND datums_laiks >= ? AND datums_laiks < ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, arstsId);
            ps.setTimestamp(2, start);
            ps.setTimestamp(3, end);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean addVizite(Vizite vizite) {
        if (vizite == null || vizite.getDatumsLaiks() == null) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(vizite.getDatumsLaiks());
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date appointmentDate = cal.getTime();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar selectedDay = Calendar.getInstance();
        selectedDay.setTime(appointmentDate);
        selectedDay.set(Calendar.HOUR_OF_DAY, 0);
        selectedDay.set(Calendar.MINUTE, 0);
        selectedDay.set(Calendar.SECOND, 0);
        selectedDay.set(Calendar.MILLISECOND, 0);

        if (!selectedDay.after(today)) {
            return false;
        }

        String linkSql
                = "SELECT COUNT(*) FROM arsta_pacienta_saites "
                + "WHERE pacienta_id = ? AND arsta_id = ?";

        String insertSql
                = "INSERT INTO vizites "
                + "(arsta_id, pacienta_id, datums_laiks, ir_apstiprinats) "
                + "VALUES (?, ?, ?, FALSE)";

        try (Connection conn = DatabaseManager.getConnection()) {

            // Pārbaudām, vai pacients ir piesaistīts šim ārstam.
            try (PreparedStatement ps = conn.prepareStatement(linkSql)) {
                ps.setInt(1, vizite.getPacientsId());
                ps.setInt(2, vizite.getArstsId());

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();

                    if (rs.getInt(1) == 0) {
                        return false;
                    }
                }
            }

            // Pārbaudām, vai ārsts šajā dienā jau ir aizņemts.
            Calendar nextDay = (Calendar) selectedDay.clone();
            nextDay.add(Calendar.DAY_OF_MONTH, 1);

            String occupiedSql
                    = "SELECT COUNT(*) FROM vizites "
                    + "WHERE arsta_id = ? "
                    + "AND datums_laiks >= ? AND datums_laiks < ?";

            try (PreparedStatement ps = conn.prepareStatement(occupiedSql)) {
                ps.setInt(1, vizite.getArstsId());
                ps.setTimestamp(2,
                        new Timestamp(selectedDay.getTimeInMillis()));
                ps.setTimestamp(3,
                        new Timestamp(nextDay.getTimeInMillis()));

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();

                    if (rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            // Saglabājam vizīti.
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, vizite.getArstsId());
                ps.setInt(2, vizite.getPacientsId());
                ps.setTimestamp(3, new Timestamp(appointmentDate.getTime()));

                if (ps.executeUpdate() != 1) {
                    return false;
                }
            }

            vizite.setDatumsLaiks(appointmentDate);
            vizite.setIrApstiprinats(false);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getPastVisitsByPatientId(int pacientsId) {
        List<Object[]> visits = new ArrayList<>();

        String sql = "SELECT v.datums_laiks, l.vards, l.uzvards "
                + "FROM vizites v "
                + "JOIN lietotajs l ON v.arsta_id = l.lietotaja_id "
                + "WHERE v.pacienta_id = ? "
                + "AND v.datums_laiks < CURRENT_TIMESTAMP "
                + "ORDER BY v.datums_laiks DESC";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacientsId);

            try (ResultSet rs = ps.executeQuery()) {
                java.text.SimpleDateFormat timeFormat
                        = new java.text.SimpleDateFormat("HH:mm");
                java.text.SimpleDateFormat dateFormat
                        = new java.text.SimpleDateFormat("dd.MM.yyyy");

                while (rs.next()) {
                    Timestamp dateTime = rs.getTimestamp("datums_laiks");

                    String time = timeFormat.format(dateTime);
                    String date = dateFormat.format(dateTime);
                    String doctor = rs.getString("vards") + " "
                            + rs.getString("uzvards");

                    visits.add(new Object[]{time, date, doctor});
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visits;
    }

    public boolean cancelUpcomingVisit(int viziteId, int pacientsId) {
        String sql = "DELETE FROM vizites "
                + "WHERE vizites_id = ? "
                + "AND pacienta_id = ? "
                + "AND datums_laiks > CURRENT_TIMESTAMP";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, viziteId);
            ps.setInt(2, pacientsId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmVisit(int viziteId, int arstsId) {
        String sql = "UPDATE vizites "
                + "SET ir_apstiprinats = TRUE "
                + "WHERE vizites_id = ? "
                + "AND arsta_id = ? "
                + "AND ir_apstiprinats = FALSE "
                + "AND datums_laiks > CURRENT_TIMESTAMP";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, viziteId);
            ps.setInt(2, arstsId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
