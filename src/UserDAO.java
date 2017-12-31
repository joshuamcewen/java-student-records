import com.sun.net.httpserver.HttpExchange;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Map;
import java.util.Random;

/**
 * Provides methods that access the database and manipulate the user data in some way (CRUD).
 * @author Joshua McEwen (16012396)
 */

public class UserDAO {

    private static Connection getDBConnection() {
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            String url = "jdbc:sqlite:studentdb.sqlite";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Checks for a user record with the supplied username and password, returning a boolean based on the outcome.
     * Used to validate a user as having correct credentials for access.
     * @param username User's username
     * @param password User's password in plain text
     * @return true/false depending on the outcome of the retrieval.
     */
    public static boolean checkLoginCredentials(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        String query = "SELECT Token FROM users WHERE Username = ? AND Password = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            String hashedPassword = generateHash(getSalt(username), password);
            stmt.setString(2, hashedPassword);
            result = stmt.executeQuery();

            if(result.next()) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Always attempt to close resources
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }

    public static boolean checkUserExists(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        String query = "SELECT Username FROM users WHERE Username = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            result = stmt.executeQuery();

            if(result.next()) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Always attempt to close resources
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }

    public static boolean insertUser(String username, String password) {
        if(!checkUserExists(username)) {
            Connection conn = null;
            PreparedStatement stmt = null;
            String query = "INSERT INTO users(Username, Salt, Password, Token) VALUES(?, ?, ?, ?);";

            try {
                conn = getDBConnection();
                conn.setAutoCommit(false); // Transaction start

                // Generate unique API key
                String apiKey = generateApiKey();
                while(checkApiKey(apiKey)) {
                    apiKey = generateApiKey();
                }

                // Generate salt and hashed password
                String salt = generateSalt();
                password = generateHash(salt, password);

                stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, salt);
                stmt.setString(3, password);
                stmt.setString(4, apiKey);
                stmt.executeUpdate();

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                // Always attempt to close resources
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.commit(); // Transaction end
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Returns the API token from the database for the supplied username.
     * @param username User's username
     * @return API token
     */
    public static String getApiKey(String username) {
        String apiKey = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        // Retrieve the API token for the username passed.
        String query = "SELECT Token FROM users WHERE Username = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            result = stmt.executeQuery();

            if(result.next()) {
                return result.getString("token");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Always attempt to close resources
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return apiKey;
    }

    /**
     * Uses Random library to generate random string which can then be used as an API key.
     * @return Random 18 length string
     */
    private static String generateApiKey() {

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder apiKey = new StringBuilder();
        Random rnd = new Random();

        // Append a random character from the above array until a length of 18 is met.
        while (apiKey.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * characters.length());
            apiKey.append(characters.charAt(index));
        }

        return apiKey.toString();
    }

    /**
     * Checks the database against the supplied API token for a match. Returns a boolean based on the outcome.
     * Used for validating genuine requests.
     * @param token User's API token
     * @return true/false depending on the outcome of the retrieval.
     */
    public static boolean checkApiKey(String token) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        // Retrieve a record to where the token passed, exists.
        String query = "SELECT Token FROM users WHERE Token = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, token);
            result = stmt.executeQuery();

            // If a token exists, it must be valid. Return true.
            if(result.next()) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Always attempt to close resources
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // A valid token hasn't been found or logic has failed, return false.
        return false;
    }

    /**
     * Checks the API token supplied as a URL parameter and returns a boolean based on the outcome of that check.
     * @param exch HttpExchange object to access request details
     * @return true/false depending on whether the token exists or not.
     */
    public static boolean isValidToken(HttpExchange exch) {
        String queryString = exch.getRequestURI().getQuery();

        if(queryString != null) {
            Map<String, String> params = Controller.getParameters(queryString);
            String apiKey = params.get("token");

            if(apiKey != null && UserDAO.checkApiKey(apiKey)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Uses cryptographically strong SecureRandom library to generate random salts used to provide additional
     * strength to hashed passwords.
     * @return Random 32 length salt
     */
    private static String generateSalt() {
        // Using SecureRandom as cryptographically secure random number generator.
        SecureRandom ng = new SecureRandom();
        byte[] salt = new byte[16];

        // Generates 16 random bytes.
        ng.nextBytes(salt);

        // Returns byte array as 32 length string.
        return DatatypeConverter.printHexBinary(salt);
    }

    /**
     * Retrieves the salt of an account for password matching post-hashing.
     * @param username Username of account being logged into
     * @return 32 length salt corresponding to username
     */
    private static String getSalt(String username) {
        String salt = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        // Retrieve the salt for the username passed.
        String query = "SELECT Salt FROM users WHERE Username = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            result = stmt.executeQuery();

            if(result.next()) {
                return result.getString("salt");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Always attempt to close resources
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return salt;
    }

    /**
     * Takes a salt and password before hashing the combination using the SHA-256 hashing algorithm and
     * returning the result.
     * @param salt A salt to add additional strength to hash.
     * @param password A password to be hashed in plain text.
     * @return 64 length hashed salt and password.
     */
    private static String generateHash(String salt, String password) {

        String hashedPassword = null;
        String toHash = salt + password;

        try {
            // Use the SHA-256 hashing algorithm.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(toHash.getBytes(), 0, toHash.length());

            // Convert bytes to string using DatatypeConverter class.
            hashedPassword = DatatypeConverter.printHexBinary(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

        return hashedPassword;
    }
}
