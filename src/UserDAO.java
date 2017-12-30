import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
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

    private static String getSalt(String username) {
        String salt = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
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
     * Returns the API token from the database for the supplied username.
     * @param username User's username
     * @return API token
     */
    public static String getApiKey(String username) {
        String apiKey = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
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

    private static String generateApiKey() {

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder apiKey = new StringBuilder();
        Random rnd = new Random();
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
        String query = "SELECT Token FROM users WHERE Token = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, token);
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

    private static String generateSalt() {
        SecureRandom ng = new SecureRandom();
        byte[] salt = new byte[16];
        ng.nextBytes(salt);

        return DatatypeConverter.printHexBinary(salt);
    }

    private static String generateHash(String salt, String password) {

        String hashedPassword = null;
        String toHash = salt + password;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(toHash.getBytes(), 0, toHash.length());
            hashedPassword = DatatypeConverter.printHexBinary(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

        return hashedPassword;
    }
}
