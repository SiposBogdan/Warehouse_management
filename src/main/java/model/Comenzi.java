package model;

/**
 * Clasa Comenzi reprezintă o entitate pentru comenzile efectuate de clienți.
 */
public class Comenzi {
    private int id;
    private String numeClient;
    private String numeProdus;
    private int cantitate;
    /**
     * Constructorul cu parametri pentru o comandă.
     *
     * @param id           Identificatorul comenzii
     * @param numeClient   Numele clientului care a plasat comanda
     * @param numeProdus   Numele produsului comandat
     * @param cantitate    Cantitatea de produs comandată
     */
    public Comenzi(int id, String numeClient, String numeProdus, int cantitate) {
        this.id = id;
        this.numeClient = numeClient;
        this.numeProdus = numeProdus;
        this.cantitate = cantitate;
    }
    /**
     * Constructorul cu parametri pentru o comandă nouă.
     *
     * @param numeClient   Numele clientului care a plasat comanda
     * @param numeProdus   Numele produsului comandat
     * @param cantitate    Cantitatea de produs comandată
     */
    public Comenzi(String numeClient, String numeProdus, int cantitate) {
        this.numeClient = numeClient;
        this.numeProdus = numeProdus;
        this.cantitate = cantitate;
    }
    /**
     * Returnează identificatorul comenzii.
     *
     * @return Identificatorul comenzii
     */
    public int getIdComanda() {
        return id;
    }
    /**
     * Setează identificatorul comenzii.
     *
     * @param idComanda Identificatorul comenzii
     */
    public void setIdComanda(int idComanda) {
        this.id = id;
    }
    /**
     * Returnează numele clientului care a plasat comanda.
     *
     * @return Numele clientului care a plasat comanda
     */
    public String getNumeClient() {
        return numeClient;
    }
    /**
     * Setează numele clientului care a plasat comanda.
     *
     * @param numeClient Numele clientului care a plasat comanda
     */
    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }
    /**
     * Returnează numele produsului comandat.
     *
     * @return Numele produsului comandat
     */
    public String getNumeProdus() {
        return numeProdus;
    }
    /**
     * Setează numele produsului comandat.
     *
     * @param numeProdus Numele produsului comandat
     */
    public void setNumeProdus(String numeProdus) {
        this.numeProdus = numeProdus;
    }
    /**
     * Returnează cantitatea de produs comandată.
     *
     * @return Cantitatea de produs comandată
     */
    public int getCantitate() {
        return cantitate;
    }
    /**
     * Setează cantitatea de produs comandată.
     *
     * @param cantitate Cantitatea de produs comandată
     */
    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }
    /**
     * Returnează o reprezentare text a obiectului Comenzi.
     *
     * @return Reprezentarea text a obiectului Comenzi
     */
    @Override
    public String toString() {
        return "Comanda cu id: " + id + " nume client: " + numeClient + " denumire produs: " + numeProdus + " cantitate: " + cantitate;
    }
}
