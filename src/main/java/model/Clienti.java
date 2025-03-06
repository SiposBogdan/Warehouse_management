package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static connection.ConnectionFactory.getConnection;
/**
 * Clasa Clienti reprezintă o entitate pentru clienții înregistrati în sistem.
 */
public class Clienti {
    private int id;
    private String nume;
    private String email;
    private String adresa;
    /**
     * Constructorul cu parametri pentru un client.
     *
     * @param id      Identificatorul clientului
     * @param nume    Numele clientului
     * @param email   Adresa de email a clientului
     * @param adresa  Adresa fizică a clientului
     */
    public Clienti(int id, String nume, String email, String adresa) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.adresa = adresa;
    }
    /**
     * Constructorul cu parametri pentru un client nou.
     *
     * @param nume    Numele clientului
     * @param email   Adresa de email a clientului
     * @param adresa  Adresa fizică a clientului
     */
    public Clienti(String nume, String email, String adresa) {
        this.nume = nume;
        this.email = email;
        this.adresa = adresa;
    }
    /**
     * Constructorul cu parametri pentru un client nou, fără adresa de email.
     *
     * @param nume    Numele clientului
     * @param adresa  Adresa fizică a clientului
     */
    public Clienti(String nume, String adresa) {
        this.nume = nume;
        this.adresa = adresa;
    }
    /**
     * Constructorul implicit pentru un client nou, cu valorile implicite pentru nume, adresa și email.
     */
    public Clienti() {
        this.nume = "NUME";
        this.adresa = "ADRESA";
        this.email ="EMAIL";
    }
    /**
     * Returnează identificatorul clientului.
     *
     * @return Identificatorul clientului
     */
    public int getId() {
        return id;
    }
    /**
     * Setează identificatorul clientului.
     *
     * @param id Identificatorul clientului
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Returnează numele clientului.
     *
     * @return Numele clientului
     */
    public String getNume() {
        return nume;
    }
    /**
     * Setează numele clientului.
     *
     * @param nume Numele clientului
     */
    public void setNume(String nume) {
        this.nume = nume;
    }
    /**
     * Returnează adresa de email a clientului.
     *
     * @return Adresa de email a clientului
     */
    public String getEmail() {
        return email;
    }
    /**
     * Setează adresa de email a clientului.
     *
     * @param email Adresa de email a clientului
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Returnează adresa fizică a clientului.
     *
     * @return Adresa fizică a clientului
     */
    public String getAdresa() {
        return adresa;
    }
    /**
     * Setează adresa fizică a clientului.
     *
     * @param adresa Adresa fizică a clientului
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     * Returnează o reprezentare text a obiectului Clienti.
     *
     * @return Reprezentarea text a obiectului Clienti
     */
    @Override
    public String toString(){
        return "Client cu id: " + id + " nume: " + nume + " email: " + email + " adresa: " + adresa;
    }
}
