package model;

/**
 * Clasa Bill reprezintă o factură înregistrată în sistem.
 * Această clasă este o clasă înregistrare (record) ce conține informații despre o factură.
 */
public record Bill(int id, String numeClient, String numeProdus, int cantitate, int pret) {

}