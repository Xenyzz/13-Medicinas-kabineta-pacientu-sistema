/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Nikita.Sersts
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Vizite;

public class ViziteDAO {

    public void add(Vizite vizite) {

    }

    public void delete(int id) {

    }

    public List<Date> getOccupiedDates(int arstsId) {

        return new ArrayList<>();
    }

    public boolean isDateAvailable(int arstsId, Date datums) {

        return false;
    }

    public boolean addVizite(Vizite vizite) {

        return false;
    }

    public List<Vizite> getHistoryByPacientsId(int pacientsId) {

        return new ArrayList<>();
    }

    public boolean confirmVizite(int viziteId) {

        return false;
    }
}
