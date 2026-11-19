/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 * Klase, kas reprezentē pacienta recepti.
 * Saglabā receptes ID, pacienta ID, medikamenta nosaukumu,
 * aprakstu un izsniegšanas datumu.
 *
 * @author Nikita.Sersts
 */
public class Recepte {

    private int id;
    private int pacientsId;
    private String medikamentaNosaukums;
    private String apraksts;
    private Date izsniegsanasDatums;

    /**
     * Izveido tukšu receptes objektu.
     */
    public Recepte() {
    }

    /**
     * Izveido receptes objektu ar norādītajām vērtībām.
     *
     * @param id receptes unikālais ID
     * @param pacientsId pacienta unikālais ID
     * @param medikamentaNosaukums medikamenta nosaukums
     * @param apraksts receptes apraksts
     * @param izsniegsanasDatums receptes izsniegšanas datums
     */
    public Recepte(int id, int pacientsId, String medikamentaNosaukums, String apraksts, Date izsniegsanasDatums) {
        this.id = id;
        this.pacientsId = pacientsId;
        this.medikamentaNosaukums = medikamentaNosaukums;
        this.apraksts = apraksts;
        this.izsniegsanasDatums = izsniegsanasDatums;
    }

    /**
     * Iegūst receptes ID.
     *
     * @return receptes unikālais ID
     */
    public int getId() {
        return id;
    }

    /**
     * Iestata receptes ID.
     *
     * @param id receptes unikālais ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Iegūst pacienta ID, kuram pieder recepte.
     *
     * @return pacienta unikālais ID
     */
    public int getPacientsId() {
        return pacientsId;
    }

    /**
     * Iestata pacienta ID.
     *
     * @param pacientsId pacienta unikālais ID
     */
    public void setPacientsId(int pacientsId) {
        this.pacientsId = pacientsId;
    }

    /**
     * Iegūst medikamenta nosaukumu.
     *
     * @return medikamenta nosaukums
     */
    public String getMedikamentaNosaukums() {
        return medikamentaNosaukums;
    }

    /**
     * Iestata medikamenta nosaukumu.
     *
     * @param medikamentaNosaukums medikamenta nosaukums
     */
    public void setMedikamentaNosaukums(String medikamentaNosaukums) {
        this.medikamentaNosaukums = medikamentaNosaukums;
    }

    /**
     * Iegūst receptes aprakstu.
     *
     * @return receptes apraksts
     */
    public String getApraksts() {
        return apraksts;
    }

    /**
     * Iestata receptes aprakstu.
     *
     * @param apraksts receptes apraksts
     */
    public void setApraksts(String apraksts) {
        this.apraksts = apraksts;
    }

    /**
     * Iegūst receptes izsniegšanas datumu.
     *
     * @return receptes izsniegšanas datums
     */
    public Date getIzsniegsanasDatums() {
        return izsniegsanasDatums;
    }

    /**
     * Iestata receptes izsniegšanas datumu.
     *
     * @param izsniegsanasDatums receptes izsniegšanas datums
     */
    public void setIzsniegsanasDatums(Date izsniegsanasDatums) {
        this.izsniegsanasDatums = izsniegsanasDatums;
    }
}