/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nikita.Sersts
 */
public class Lietotajs {

    private int id;
    private String vards;
    private String uzvards;
    private String personasKodsEnc;
    private String paroleHash;
    private String loma;
    private String numurs;

    public Lietotajs() {
    }

    public Lietotajs(int id, String vards, String uzvards, String personasKodsEnc, String paroleHash, String loma, String numurs) {
        this.id = id;
        this.vards = vards;
        this.uzvards = uzvards;
        this.personasKodsEnc = personasKodsEnc;
        this.paroleHash = paroleHash;
        this.loma = loma;
        this.numurs = numurs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVards() {
        return vards;
    }

    public void setVards(String vards) {
        this.vards = vards;
    }

    public String getUzvards() {
        return uzvards;
    }

    public void setUzvards(String uzvards) {
        this.uzvards = uzvards;
    }

    public String getPersonasKodsEnc() {
        return personasKodsEnc;
    }

    public void setPersonasKodsEnc(String personasKodsEnc) {
        this.personasKodsEnc = personasKodsEnc;
    }

    public String getParoleHash() {
        return paroleHash;
    }

    public void setParoleHash(String paroleHash) {
        this.paroleHash = paroleHash;
    }

    public String getLoma() {
        return loma;
    }

    public void setLoma(String loma) {
        this.loma = loma;
    }

    public String getNumurs() {
        return numurs;
    }

    public void setNumurs(String numurs) {
        this.numurs = numurs;
    }
}
