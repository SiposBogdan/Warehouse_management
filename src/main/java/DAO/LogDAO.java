package DAO;

import connection.ConnectionFactory;
import model.Bill;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa LogDAO oferă funcționalități pentru gestionarea obiectelor de tip Bill în tabelul Log din baza de date.
 */
public class LogDAO {
    private static final Logger LOGGER = Logger.getLogger(LogDAO.class.getName());
    private static final String reportStatementString = "SELECT * from Log";

    /**
     * Metodă pentru inserarea unui obiect Bill în tabelul Log.
     *
     * @param bill obiectul Bill care urmează să fie inserat
     * @throws IllegalAccessException dacă inserarea nu reușește
     */
    public void insert(Bill bill) throws IllegalAccessException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String query = "INSERT INTO Log (id, numeClient, numeProdus, cantitate, pret) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bill.id());
                statement.setString(2, bill.numeClient());
                statement.setString(3, bill.numeProdus());
                statement.setInt(4, bill.cantitate());
                statement.setInt(5, bill.pret());

                // Execută declarația de inserare
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted <= 0) {
                    throw new IllegalAccessException("Inserarea facturii în tabelul Log a eșuat.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Eroare la inserarea facturii în tabelul Log", e);
            throw new IllegalAccessException("Inserarea facturii în tabelul Log a eșuat: " + e.getMessage());
        }
    }

    /**
     * Metodă pentru obținerea tuturor înregistrărilor din tabelul Log.
     *
     * @return un ResultSet cu toate înregistrările din tabelul Log
     */
    public static ResultSet getAllLogs() {
        ResultSet resultSet = null;
        try (Connection connection = ConnectionFactory.getConnection()) {
            String query = "SELECT * FROM Log";
            PreparedStatement statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Eroare la preluarea logurilor din tabelul Log", e);
        }
        return resultSet;
    }

    /**
     * Generează un raport cu toate înregistrările din tabelul Log.
     *
     * @return un ResultSet cu toate înregistrările din tabelul Log
     */
    public static ResultSet report() {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement reportStatement = null;
        ResultSet rs = null;
        try {
            reportStatement = dbConnection.prepareStatement(reportStatementString, Statement.RETURN_GENERATED_KEYS);
            rs = reportStatement.executeQuery();
            return rs;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "LogDAO: report " + e.getMessage());
        }
        return null;
    }
}
