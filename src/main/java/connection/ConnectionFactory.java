package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectionFactory este o clasă singleton responsabilă pentru crearea și gestionarea
 * conexiunilor la baza de date. Utilizează JDBC pentru a se conecta la o bază de date MySQL.
 *
 */
public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/management";
    private static final String USER = "root";
    private static final String PASS = "MySQL";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Returnează instanța unică a clasei ConnectionFactory.
     *
     * @return instanța unică a ConnectionFactory
     */
    public static ConnectionFactory getSingleInstance() {
        return singleInstance;
    }

    /**
     * Setează instanța unică a clasei ConnectionFactory. Această metodă permite
     * înlocuirea instanței singleton cu o altă instanță, dacă este necesar.
     *
     * @param singleInstance noua instanță a ConnectionFactory
     */
    public static void setSingleInstance(ConnectionFactory singleInstance) {
        ConnectionFactory.singleInstance = singleInstance;
    }

    /**
     * Constructorul privat al clasei ConnectionFactory. Încarcă driverul JDBC.
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creează o conexiune la baza de date.
     *
     * @return conexiunea la baza de date sau null dacă apare o eroare
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "A apărut o eroare în încercarea de a se conecta la baza de date");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Returnează o conexiune la baza de date.
     *
     * @return conexiunea la baza de date
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Închide conexiunea la baza de date.
     *
     * @param connection conexiunea care trebuie închisă
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "A apărut o eroare în încercarea de a închide conexiunea");
            }
        }
    }

    /**
     * Închide declarația SQL.
     *
     * @param statement declarația SQL care trebuie închisă
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "A apărut o eroare în încercarea de a închide declarația");
            }
        }
    }

    /**
     * Închide setul de rezultate SQL.
     *
     * @param resultSet setul de rezultate care trebuie închis
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "A apărut o eroare în încercarea de a închide setul de rezultate");
            }
        }
    }
}
