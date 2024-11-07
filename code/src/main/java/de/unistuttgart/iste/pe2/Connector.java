package de.unistuttgart.iste.pe2;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import de.unistuttgart.iste.pe2.models.Letter;
import com.j256.ormlite.dao.Dao;
import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.logging.Level;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Connector {
    private static Logger LOGGER = Logger.getLogger(Connector.class.getName());

    // database connection settings
    private String connectionString = "jdbc:mariadb://bilbao.informatik.uni-stuttgart.de/pe2-db-a1";
    private String username = "pe2-nutzer";
    private String password = "esJLtFm6ksCT4mCyOS";


    private ConnectionSource connectionSource;
    private Dao<Letter, Integer> letterDao;


    public void connect() throws RuntimeException {
        boolean connected = this.connectToDB(this.connectionString, username, password);
        if (!connected) {
            throw new RuntimeException("Could not connect to database");
        }

        // init DAO
        this.initDao();
    }


    public void aufgabeA(int[] arrayIndexes) {
        System.out.println("Aufgabe 2.2.a ");
        // Get Each letter in the array
        for (int id : arrayIndexes) {
            var letter = this.getById(id);
            if (letter != null) {
                System.out.print(letter.getLetter());
            }
        }
        System.out.println();
    }


    public void aufgabeB(char[] arrayLetters) {
        System.out.println("Aufgabe 2.2.b ");
        // Get Each letter in the array
        for (char letter : arrayLetters) {
            var letters = this.getByLetter(letter);
            String ids = letters.stream()
                    .map(Letter::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            System.out.println("IDs für '" + letter + "' = " + ids);
        }
    }


    public void aufgabeC() {
        System.out.println("Aufgabe 2.2.c ");
        var letters = this.getAll();

        int sum = 0;
        int count = 0;

        for (Letter letter : letters) {
            sum += letter.getId();
            count++;
        }

        double average = count > 0 ? (double) sum / count : 0;

        System.out.println("Summe = " + sum);
        System.out.println("Durchschnittswert = " + average);
    }


    /**
     * Initialize DAO
     */
    private void initDao() {
        try {
            this.letterDao = DaoManager.createDao(connectionSource, Letter.class);
        } catch (SQLException exception) {
            this.logSQLException(exception);
        }
    }


    /**
     * Get Letter by ID
     *
     * @param id record id
     * @return the letter
     */
    private Letter getById(int id) {
        try {
            return letterDao.queryForId(id);
        } catch (SQLException exception) {
            this.logSQLException(exception);
        }
        return null;
    }


    /**
     * Get Letter by letter
     *
     * @param letter record letter
     * @return the letter
     */
    private List<Letter> getByLetter(char letter) {
        try {
            return letterDao.queryForEq("letter", letter);
        } catch (SQLException exception) {
            this.logSQLException(exception);
        }
        return null;
    }


    /**
     * Get ALL
     *
     * @return list of letters
     */
    private List<Letter> getAll() {
        try {
            return letterDao.queryForAll();
        } catch (SQLException exception) {
            this.logSQLException(exception);
        }
        return null;
    }


    /*
     * Connects to a database
     */
    private boolean connectToDB(String connectionString, String user, String password) {
        try {
            this.connectionSource = new JdbcConnectionSource(connectionString, user, password);
            return true;
        } catch (SQLException exception) {
            this.logSQLException(exception);
        }
        return false;
    }


    /*
     * Closes connection to the database
     */
    public void disconnect() {
        try {
            this.connectionSource.close();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error message: " + exception.getMessage());
        }
    }


    private void logSQLException(SQLException exception) {
        LOGGER.log(Level.SEVERE, "Error code: " + exception.getErrorCode());
        LOGGER.log(Level.SEVERE, "Error message: " + exception.getMessage());
    }


}
