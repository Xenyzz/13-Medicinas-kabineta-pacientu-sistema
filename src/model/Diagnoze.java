/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Nikita.Sersts
 */
public class Diagnoze {
    private int id;
    private int pacientsId;
    private String apraksts;
    private Date datums;

    public Diagnoze() {}

    public Diagnoze(int id, int pacientsId, String apraksts, Date datums) {
        this.id = id;
        this.pacientsId = pacientsId;
        this.apraksts = apraksts;
        this.datums = datums;
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

    public String getApraksts() {
        return apraksts;
    }

    public void setApraksts(String apraksts) {
        this.apraksts = apraksts;
    }

    public Date getDatums() {
        return datums;
    }

    public void setDatums(Date datums) {
        this.datums = datums;
    }
}
