/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Klase, kas reprezentē sistēmas lietotāju.
 * Saglabā lietotāja personas datus, pieslēgšanās informāciju,
 * lietotāja lomu un tālruņa numuru.
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

    /**
     * Izveido tukšu lietotāja objektu.
     */
    public Lietotajs() {
    }

    /**
     * Izveido lietotāja objektu ar norādītajām vērtībām.
     *
     * @param id lietotāja unikālais ID
     * @param vards lietotāja vārds
     * @param uzvards lietotāja uzvārds
     * @param personasKodsEnc šifrēts personas kods
     * @param paroleHash paroles hash vērtība
     * @param loma lietotāja loma sistēmā
     * @param numurs lietotāja tālruņa numurs
     */
    public Lietotajs(int id, String vards, String uzvards, String personasKodsEnc, String paroleHash, String loma, String numurs) {
        this.id = id;
        this.vards = vards;
        this.uzvards = uzvards;
        this.personasKodsEnc = personasKodsEnc;
        this.paroleHash = paroleHash;
        this.loma = loma;
        this.numurs = numurs;
    }

    /**
     * Iegūst lietotāja ID.
     *
     * @return lietotāja unikālais ID
     */
    public int getId() {
        return id;
    }

    /**
     * Iestata lietotāja ID.
     *
     * @param id lietotāja unikālais ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Iegūst lietotāja vārdu.
     *
     * @return lietotāja vārds
     */
    public String getVards() {
        return vards;
    }

    /**
     * Iestata lietotāja vārdu.
     *
     * @param vards lietotāja vārds
     */
    public void setVards(String vards) {
        this.vards = vards;
    }

    /**
     * Iegūst lietotāja uzvārdu.
     *
     * @return lietotāja uzvārds
     */
    public String getUzvards() {
        return uzvards;
    }

    /**
     * Iestata lietotāja uzvārdu.
     *
     * @param uzvards lietotāja uzvārds
     */
    public void setUzvards(String uzvards) {
        this.uzvards = uzvards;
    }

    /**
     * Iegūst šifrēto personas kodu.
     *
     * @return šifrētais personas kods
     */
    public String getPersonasKodsEnc() {
        return personasKodsEnc;
    }

    /**
     * Iestata šifrēto personas kodu.
     *
     * @param personasKodsEnc šifrētais personas kods
     */
    public void setPersonasKodsEnc(String personasKodsEnc) {
        this.personasKodsEnc = personasKodsEnc;
    }

    /**
     * Iegūst paroles hash vērtību.
     *
     * @return paroles hash vērtība
     */
    public String getParoleHash() {
        return paroleHash;
    }

    /**
     * Iestata paroles hash vērtību.
     *
     * @param paroleHash paroles hash vērtība
     */
    public void setParoleHash(String paroleHash) {
        this.paroleHash = paroleHash;
    }

    /**
     * Iegūst lietotāja lomu sistēmā.
     *
     * @return lietotāja loma
     */
    public String getLoma() {
        return loma;
    }

    /**
     * Iestata lietotāja lomu sistēmā.
     *
     * @param loma lietotāja loma
     */
    public void setLoma(String loma) {
        this.loma = loma;
    }

    /**
     * Iegūst lietotāja tālruņa numuru.
     *
     * @return tālruņa numurs
     */
    public String getNumurs() {
        return numurs;
    }

    /**
     * Iestata lietotāja tālruņa numuru.
     *
     * @param numurs tālruņa numurs
     */
    public void setNumurs(String numurs) {
        this.numurs = numurs;
    }
}