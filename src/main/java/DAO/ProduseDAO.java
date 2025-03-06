package DAO;

import java.sql.Connection;
import connection.ConnectionFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Produse;

/**
 * Clasa ProduseDAO oferă funcționalități pentru gestionarea obiectelor de tip Produse în baza de date.
 */
public class ProduseDAO extends AbstractDAO<Produse> {

    protected static final Logger LOGGER = Logger.getLogger(ProduseDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO Produse (denumire, cantitate, pret) VALUES (?,?,?)";
    private final static String findStatementString = "SELECT * FROM Produse WHERE id = ?";
    private static final String deleteStatementString = "DELETE FROM Produse WHERE denumire = ?";
    private static final String reportStatementString = "SELECT * FROM Produse";
    private static final String selectStatementString = "SELECT pret FROM Produse WHERE denumire=?";
    private static final String updateComenziProduse = "UPDATE Produse SET cantitate = cantitate - 1 WHERE denumire = ?";
    private final static String gasesteDenumireProdus = "SELECT cantitate FROM Produse WHERE denumire = ?";
    private static final String updateStatementString = "UPDATE products SET cantitate = (SELECT p.cantitate FROM (SELECT * FROM products) AS p WHERE nume = ?) - ?";

    /**
     * Constructorul clasei ProduseDAO.
     */
    public ProduseDAO() {
        super();
    }

    /**
     * Returnează o listă cu toate produsele din baza de date.
     *
     * @return o listă cu toate produsele din baza de date
     * @throws SQLException dacă apare o excepție SQL
     */
    public List<Produse> getAllProducts() throws SQLException {
        List<Produse> products = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();

            String query = "SELECT * FROM produse";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String denumire = resultSet.getString("denumire");
                int cantitate = resultSet.getInt("cantitate");
                int pret = resultSet.getInt("pret");

                Produse product = new Produse(id, denumire, cantitate, pret);
                products.add(product);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return products;
    }

    /**
     * Generează un raport cu toate produsele din baza de date.
     *
     * @return un ResultSet cu toate produsele din baza de date
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
            LOGGER.log(Level.WARNING, "DenumireDAO: report " + e.getMessage());
        }
        return null;
    }
}
