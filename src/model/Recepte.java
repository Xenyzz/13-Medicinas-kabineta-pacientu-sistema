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

public class Recepte {

    private int id;
    private int pacientsId;
    private String medikamentaNosaukums;
    private String apraksts;
    private Date izsniegsanasDatums;

    public Recepte() {
    }

    public Recepte(int id, int pacientsId, String medikamentaNosaukums, String apraksts, Date izsniegsanasDatums) {
        this.id = id;
        this.pacientsId = pacientsId;
        this.medikamentaNosaukums = medikamentaNosaukums;
        this.apraksts = apraksts;
        this.izsniegsanasDatums = izsniegsanasDatums;
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

    public String getMedikamentaNosaukums() {
        return medikamentaNosaukums;
    }

    public void setMedikamentaNosaukums(String medikamentaNosaukums) {
        this.medikamentaNosaukums = medikamentaNosaukums;
    }

    public String getApraksts() {
        return apraksts;
    }

    public void setApraksts(String apraksts) {
        this.apraksts = apraksts;
    }

    public Date getIzsniegsanasDatums() {
        return izsniegsanasDatums;
    }

    public void setIzsniegsanasDatums(Date izsniegsanasDatums) {
        this.izsniegsanasDatums = izsniegsanasDatums;
    }
}
