/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nikita.Sersts
 */
import java.util.Date;

public class Vizite {

    private int id;
    private int pacientsId;
    private int arstsId;
    private Date datumsLaiks;
    private boolean irApstiprinats;

    public Vizite() {
    }

    public Vizite(int id, int pacientsId, int arstsId, Date datumsLaiks, boolean irApstiprinats) {
        this.id = id;
        this.pacientsId = pacientsId;
        this.arstsId = arstsId;
        this.datumsLaiks = datumsLaiks;
        this.irApstiprinats = irApstiprinats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacientsId() {
        return pacientsId;
    }

    public void setPacientsId(int pacientsId) {
        this.pacientsId = pacientsId;
    }

    public int getArstsId() {
        return arstsId;
    }

    public void setArstsId(int arstsId) {
        this.arstsId = arstsId;
    }

    public Date getDatumsLaiks() {
        return datumsLaiks;
    }

    public void setDatumsLaiks(Date datumsLaiks) {
        this.datumsLaiks = datumsLaiks;
    }

    public boolean isIrApstiprinats() {
        return irApstiprinats;
    }

    public void setIrApstiprinats(boolean irApstiprinats) {
        this.irApstiprinats = irApstiprinats;
    }
}
