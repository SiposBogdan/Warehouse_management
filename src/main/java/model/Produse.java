package model;

/**
 * Clasa Produse reprezintă o entitate pentru produsele disponibile în magazin.
 */
public class Produse {
    private int id;
    private String denumire;
    private int cantitate;
    private int pret;
    /**
     * Constructorul cu parametri pentru un produs.
     *
     * @param id         Identificatorul produsului
     * @param denumire   Denumirea produsului
     * @param cantitate  Cantitatea disponibilă în stoc
     * @param pret       Prețul produsului
     */
    public Produse(int id, String denumire, int cantitate, int pret) {
        this.id = id;
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.pret = pret;
    }
    /**
     * Constructorul cu parametri pentru un produs nou.
     *
     * @param denumire   Denumirea produsului
     * @param cantitate  Cantitatea disponibilă în stoc
     * @param pret       Prețul produsului
     */
    public Produse(String denumire, int cantitate, int pret) {
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.pret = pret;
    }
    /**
     * Returnează identificatorul produsului.
     *
     * @return Identificatorul produsului
     */
    public int getIdProdus() {
        return id;
    }
    /**
     * Setează identificatorul produsului.
     *
     * @param idProdus Identificatorul produsului
     */
    public void setIdProdus(int idProdus) {
        this.id = id;
    }
    /**
     * Returnează denumirea produsului.
     *
     * @return Denumirea produsului
     */
    public String getDenumire() {
        return denumire;
    }
    /**
     * Setează denumirea produsului.
     *
     * @param denumire Denumirea produsului
     */
    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
    /**
     * Returnează cantitatea disponibilă în stoc a produsului.
     *
     * @return Cantitatea disponibilă în stoc a produsului
     */
    public int getCantitate() {
        return cantitate;
    }
    /**
     * Setează cantitatea disponibilă în stoc a produsului.
     *
     * @param cantitate Cantitatea disponibilă în stoc a produsului
     */
    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }
    /**
     * Returnează prețul produsului.
     *
     * @return Prețul produsului
     */
    public int getPret() {
        return pret;
    }
    /**
     * Setează prețul produsului.
     *
     * @param pret Prețul produsului
     */
    public void setPret(int pret) {
        this.pret = pret;
    }
    /**
     * Returnează o reprezentare text a obiectului Produse.
     *
     * @return Reprezentarea text a obiectului Produse
     */
    @Override
    public String toString(){
        return "Produsul are id-ul: " + id + " denumirea: " + denumire + " pretul: " + pret + " cantitatea din stoc: " + cantitate;
    }
}
