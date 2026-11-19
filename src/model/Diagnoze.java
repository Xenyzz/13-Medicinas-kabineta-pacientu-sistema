/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 * Klase, kas reprezentē pacienta diagnozi.
 * Saglabā diagnozes ID, pacienta ID, aprakstu un datumu.
 *
 * @author Nikita.Sersts
 */
public class Diagnoze {

    private int id;
    private int pacientsId;
    private String apraksts;
    private Date datums;

    /**
     * Izveido tukšu diagnozes objektu.
     */
    public Diagnoze() {}

    /**
     * Izveido diagnozes objektu ar norādītajām vērtībām.
     *
     * @param id diagnozes unikālais ID
     * @param pacientsId pacienta unikālais ID
     * @param apraksts diagnozes apraksts
     * @param datums diagnozes noteikšanas datums
     */
    public Diagnoze(int id, int pacientsId, String apraksts, Date datums) {
        this.id = id;
        this.pacientsId = pacientsId;
        this.apraksts = apraksts;
        this.datums = datums;
    }

    /**
     * Iegūst diagnozes ID.
     *
     * @return diagnozes unikālais ID
     */
    public int getId() {
        return id;
    }

    /**
     * Iestata diagnozes ID.
     *
     * @param id diagnozes unikālais ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Iegūst pacienta ID, kuram pieder diagnoze.
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
     * Iegūst diagnozes aprakstu.
     *
     * @return diagnozes apraksts
     */
    public String getApraksts() {
        return apraksts;
    }

    /**
     * Iestata diagnozes aprakstu.
     *
     * @param apraksts diagnozes apraksts
     */
    public void setApraksts(String apraksts) {
        this.apraksts = apraksts;
    }

    /**
     * Iegūst diagnozes datumu.
     *
     * @return diagnozes datums
     */
    public Date getDatums() {
        return datums;
    }

    /**
     * Iestata diagnozes datumu.
     *
     * @param datums diagnozes datums
     */
    public void setDatums(Date datums) {
        this.datums = datums;
    }
}