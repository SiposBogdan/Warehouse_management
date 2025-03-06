package DAO;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;

import static java.lang.Integer.parseInt;

/**
 * AbstractDAO este o clasă generică abstractă care oferă funcționalități comune pentru
 * operațiuni CRUD (Creare, Citire, Actualizare, Ștergere) utilizând JDBC și reflecție.
 *
 *
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructorul pentru AbstractDAO. Utilizează reflecția pentru a determina
     * tipul generic al clasei.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Creează o interogare SELECT pentru un câmp specificat.
     *
     * @param field numele câmpului pentru care se va crea interogarea
     * @return interogarea SELECT ca șir de caractere
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" =?");
        return sb.toString();
    }

    /**
     * Creează o interogare SELECT pentru toate înregistrările din tabel.
     *
     * @return interogarea SELECT pentru toate înregistrările ca șir de caractere
     */
    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Găsește un obiect de tip T după ID.
     *
     * @param id ID-ul obiectului de găsit
     * @return obiectul de tip T găsit sau null dacă nu este găsit
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Creează o listă de obiecte de tip T dintr-un ResultSet.
     *
     * @param resultSet setul de rezultate din care se vor crea obiectele
     * @return lista de obiecte de tip T
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (Constructor ctor1 : ctors) {
            ctor = ctor1;
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException |
                 InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserează un obiect de tip T în baza de date.
     *
     * @param t obiectul de inserat
     * @return null
     * @throws IllegalAccessException dacă accesul la câmpul obiectului este ilegal
     */
    public T insert(T t) throws IllegalAccessException {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(type.getSimpleName()).append(" VALUES (");
        int ctr = 0;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(t);
            if (ctr > 0) sb.append(", ");
            if (value instanceof String) {
                sb.append("'").append(value).append("'");
            } else {
                sb.append(value);
            }
            ctr++;
        }
        sb.append(")");
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Actualizează un obiect de tip T în baza de date.
     *
     * @param t obiectul de actualizat
     * @return null
     * @throws IllegalAccessException dacă accesul la câmpul obiectului este ilegal
     */
    public T update(T t) throws IllegalAccessException {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(type.getSimpleName()).append(" SET ");
        int ctr = 0;
        int id = 0;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(t);
            if ("id".equals(field.getName())) {
                id = parseInt(String.valueOf(value));
            } else {
                if (ctr > 0) sb.append(", ");
                if (value instanceof String) {
                    sb.append(field.getName()).append(" = '").append(value).append("'");
                } else {
                    sb.append(field.getName()).append(" = ").append(value);
                }
                ctr++;
            }
        }
        sb.append(" WHERE id = ").append(id);
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Șterge un obiect de tip T din baza de date după ID.
     *
     * @param id ID-ul obiectului de șters
     * @return null
     * @throws IllegalAccessException dacă accesul la câmpul obiectului este ilegal
     */
    public T delete(int id) throws IllegalAccessException {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(type.getSimpleName()).append(" WHERE id = ").append(id);
        String sql = sb.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:Delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}
