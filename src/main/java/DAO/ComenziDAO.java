package DAO;

import java.sql.Connection;
import connection.ConnectionFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Comenzi;

/**
 * ComenziDAO oferă funcționalități specifice pentru gestionarea obiectelor de tip Comenzi
 * în baza de date, extinzând funcționalitățile generice ale clasei AbstractDAO.
 */
public class ComenziDAO extends AbstractDAO<Comenzi> {

    protected static final Logger LOGGER = Logger.getLogger(ClientiDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO Comenzi (numeClient, numeProdus, cantitate) VALUES (?,?,?)";
    private final static String findStatementString = "SELECT * FROM Comenzi WHERE idClient = ?";
    private static final String deleteStatementString = "DELETE FROM Comenzi WHERE nume = ?";
    private static final String reportStatementString = "SELECT * FROM Comenzi";

    /**
     * Constructorul pentru ComenziDAO.
     */
    public ComenziDAO() {
        super();
    }

    /**
     * Generează un raport cu toate înregistrările din tabelul Comenzi.
     *
     * @return un ResultSet cu toate înregistrările din tabelul Comenzi
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
            LOGGER.log(Level.WARNING, "ComenziDAO: report " + e.getMessage());
        }
        return null;
    }

    /*
     * Metode comentate:
     * Metodele de mai jos sunt comentate. Acestea pot fi utilizate pentru
     * funcționalități adiționale cum ar fi găsirea, inserarea și ștergerea comenzilor.
     */

    /*
    public static Comenzi findById(int idComanda) {
        Comenzi toReturn = null;
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setLong(1, idComanda);
            rs = findStatement.executeQuery();
            rs.next();
            String numeClient = rs.getString("numeClient");
            String numeProdus = rs.getString("numeProdus");
            int cantitate = rs.getInt("cantitate");
            toReturn = new Comenzi(numeClient, numeProdus, cantitate);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ComenziDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    public static int insert(Comenzi comenzi) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, comenzi.getNumeClient());
            insertStatement.setString(2, comenzi.getNumeProdus());
            insertStatement.setInt(3, comenzi.getCantitate());
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ComenziDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }

    public static void delete(String numeClient) {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = dbConnection.prepareStatement(deleteStatementString, Statement.RETURN_GENERATED_KEYS);
            deleteStatement.setString(1, numeClient);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ComenziDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
    }
    */
}
