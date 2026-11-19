/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 * Klase, kas glabā informāciju par pacienta vizīti pie ārsta.
 * Satur vizītes ID, pacienta un ārsta ID, datumu un laiku,
 * kā arī informāciju par vizītes apstiprināšanas statusu.
 *
 * @author Nikita.Sersts
 */
public class Vizite {

    private int id;
    private int pacientsId;
    private int arstsId;
    private Date datumsLaiks;
    private boolean irApstiprinats;

    /**
     * Izveido jaunu tukšu vizītes objektu.
     */
    public Vizite() {
    }

    /**
     * Izveido jaunu vizītes objektu ar norādītajām vērtībām.
     *
     * @param id vizītes unikālais identifikators
     * @param pacientsId pacienta identifikators
     * @param arstsId ārsta identifikators
     * @param datumsLaiks vizītes datums un laiks
     * @param irApstiprinats norāda, vai vizīte ir apstiprināta
     */
    public Vizite(int id, int pacientsId, int arstsId, Date datumsLaiks, boolean irApstiprinats) {
        this.id = id;
        this.pacientsId = pacientsId;
        this.arstsId = arstsId;
        this.datumsLaiks = datumsLaiks;
        this.irApstiprinats = irApstiprinats;
    }

    /**
     * Atgriež vizītes identifikatoru.
     *
     * @return vizītes ID
     */
    public int getId() {
        return id;
    }

    /**
     * Iestata vizītes identifikatoru.
     *
     * @param id vizītes ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Atgriež pacienta identifikatoru.
     *
     * @return pacienta ID
     */
    public int getPacientsId() {
        return pacientsId;
    }

    /**
     * Iestata pacienta identifikatoru.
     *
     * @param pacientsId pacienta ID
     */
    public void setPacientsId(int pacientsId) {
        this.pacientsId = pacientsId;
    }

    /**
     * Atgriež ārsta identifikatoru.
     *
     * @return ārsta ID
     */
    public int getArstsId() {
        return arstsId;
    }

    /**
     * Iestata ārsta identifikatoru.
     *
     * @param arstsId ārsta ID
     */
    public void setArstsId(int arstsId) {
        this.arstsId = arstsId;
    }

    /**
     * Atgriež vizītes datumu un laiku.
     *
     * @return vizītes datums un laiks
     */
    public Date getDatumsLaiks() {
        return datumsLaiks;
    }

    /**
     * Iestata vizītes datumu un laiku.
     *
     * @param datumsLaiks vizītes datums un laiks
     */
    public void setDatumsLaiks(Date datumsLaiks) {
        this.datumsLaiks = datumsLaiks;
    }

    /**
     * Pārbauda, vai vizīte ir apstiprināta.
     *
     * @return true, ja vizīte ir apstiprināta, pretējā gadījumā false
     */
    public boolean isIrApstiprinats() {
        return irApstiprinats;
    }

    /**
     * Iestata vizītes apstiprināšanas statusu.
     *
     * @param irApstiprinats true, ja vizīte ir apstiprināta
     */
    public void setIrApstiprinats(boolean irApstiprinats) {
        this.irApstiprinats = irApstiprinats;
    }
}