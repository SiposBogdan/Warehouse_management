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
import model.Clienti;

import static connection.ConnectionFactory.getConnection;

/**
 * ClientiDAO oferă funcționalități specifice pentru gestionarea obiectelor de tip Clienti
 * în baza de date, extinzând funcționalitățile generice ale clasei AbstractDAO.
 */
public class ClientiDAO extends AbstractDAO<Clienti> {

    protected static final Logger LOGGER = Logger.getLogger(ClientiDAO.class.getName());
    private static final String insertStatementString = "INSERT INTO Clienti (nume, email, adresa) VALUES (?,?,?)";
    private static final String deleteStatementString = "DELETE FROM Clienti WHERE nume = ?";
    private static final String reportStatementString= "SELECT * from Clienti";

    /**
     * Constructorul pentru ClientiDAO.
     */
    public ClientiDAO() {
        super();
    }

    /**
     * Inserează un obiect de tip Clienti în baza de date.
     *
     * @param client obiectul Clienti de inserat
     * @return obiectul Clienti inserat cu ID-ul generat
     */
    public static Clienti inserti(Clienti client) {
        Connection dbConnection = null;
        PreparedStatement insertStatement = null;
        ResultSet rs = null;
        try {
            dbConnection = ConnectionFactory.getConnection();
            insertStatement = dbConnection.prepareStatement(insertStatementString, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, client.getNume());
            insertStatement.setString(2, client.getEmail());
            insertStatement.setString(3, client.getAdresa());
            insertStatement.executeUpdate();
            rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1)); // Se presupune că există o metodă setId în Clienti
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientiDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return client;
    }

    /**
     * Generează un raport cu toate înregistrările din tabelul Clienti.
     *
     * @return un ResultSet cu toate înregistrările din tabelul Clienti
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
            LOGGER.log(Level.WARNING, "ClientiDAO:report " + e.getMessage());
        }
        return null;
    }

    /**
     * Șterge o înregistrare din tabelul Clienti pe baza numelui.
     *
     * @param nume numele clientului de șters
     */
    public static void deleteNume(String nume) {
        Connection dbConnection = null;
        PreparedStatement deleteStatement = null;
        try {
            dbConnection = ConnectionFactory.getConnection();
            deleteStatement = dbConnection.prepareStatement(deleteStatementString);
            deleteStatement.setString(1, nume);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientiDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    /**
     * Returnează o listă cu toți clienții din tabelul Clienti.
     *
     * @return o listă de obiecte Clienti
     * @throws SQLException dacă apare o eroare de SQL
     */
    public List<Clienti> getAllClients() throws SQLException {
        List<Clienti> clients = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            String query = "SELECT * FROM clienti";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nume = resultSet.getString("nume");
                String email = resultSet.getString("email");
                String adresa = resultSet.getString("adresa");

                Clienti client = new Clienti(id, nume, email, adresa);
                clients.add(client);
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

        return clients;
    }
}
