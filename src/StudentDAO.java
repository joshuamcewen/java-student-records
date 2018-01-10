import com.sun.net.httpserver.HttpExchange;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Provides methods that access the database and manipulate the student data in some way (CRUD).
 * @author Joshua McEwen (16012396)
 */

public class StudentDAO {
    /**
     * Makes and returns a connection to the database.
     * @return Instance of the Connection class for the corresponding database
     */
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
     * Retrieves all students from the database, converts them into Student objects and returns as an ArrayList.
     * @return ArrayList of Student objects
     */
    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        // Retrieve all students.
        String query = "SELECT * FROM students;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();

            while(result.next()) {
                String name = result.getString("Name");
                String gender = result.getString("Gender");
                String dob = result.getString("Dob");
                String address = result.getString("Address");
                String postcode = result.getString("Postcode");
                int sNumber = result.getInt("StudentNumber");
                String cTitle = result.getString("courseTitle");
                String startDate = result.getString("startDate");
                float bursary = result.getFloat("bursary");
                String email = result.getString("email");

                students.add(new Student(name, gender, dob, address, postcode, sNumber, cTitle, startDate, bursary, email));
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

        return students;
    }

    /**
     * Retrieves a specific student from the database based on student number and returns as Student object.
     * @param stuNumber Student's student ID number
     * @return Student object
     */
    public static Student getStudent(int stuNumber) {
        Student student = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        // Retrieve student by student number.
        String query = "SELECT * FROM students WHERE StudentNumber = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, stuNumber);
            result = stmt.executeQuery();

            if(result.next()) {
                String name = result.getString("Name");
                String gender = result.getString("Gender");
                String dob = result.getString("Dob");
                String address = result.getString("Address");
                String postcode = result.getString("Postcode");
                int sNumber = result.getInt("StudentNumber");
                String cTitle = result.getString("courseTitle");
                String startDate = result.getString("startDate");
                float bursary = result.getFloat("bursary");
                String email = result.getString("email");

                return new Student(name, gender, dob, address, postcode, sNumber, cTitle, startDate, bursary, email);
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

        return student;
    }

    /**
     * Inserts a student record into the database from a supplied Student object.
     * @param student Student object
     * @return true/false depending on the outcome of the insertion.
     */
    public static boolean insertStudent(Student student) {
        Connection conn = null;
        PreparedStatement stmt = null;

        // Insert a new record into the students table.
        String query = "INSERT INTO students(Name, Gender, DOB, Address, Postcode, StudentNumber, CourseTitle, StartDate, Bursary, Email) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            conn = getDBConnection();
            conn.setAutoCommit(false); // Transaction start

            stmt = conn.prepareStatement(query);
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getGender());
            stmt.setString(3, student.getDob());
            stmt.setString(4, student.getAddress());
            stmt.setString(5, student.getPostcode());
            stmt.setInt(6, student.getStudentNumber());
            stmt.setString(7, student.getCourseTitle());
            stmt.setString(8, student.getStartDate());
            stmt.setFloat(9, student.getBursary());
            stmt.setString(10, student.getEmail());
            stmt.executeUpdate();

            return true;
        } catch(SQLException e) {
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

        return false;
    }

    /**
     * Deletes a student record into the database from a supplied student ID number.
     * @param stuNumber Student's student ID number
     * @return true/false depending on the outcome of the deletion.
     */
    public static boolean deleteStudent(int stuNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;

        // Delete student record by student number.
        String query = "DELETE FROM students WHERE StudentNumber = ?;";

        try {
            conn = getDBConnection();
            conn.setAutoCommit(false); // Transaction start

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, stuNumber);
            stmt.executeUpdate();

            return true;
        } catch(SQLException e) {
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

        return false;
    }

    /**
     * Updates a student record into the database from a supplied Student object, using the supplied student number
     * to identify existing record.
     * @param student Student object
     * @return true/false depending on the outcome of the update.
     */
    public static boolean updateStudent(Student student) {
        Connection conn = null;
        PreparedStatement stmt = null;

        // Update a student record.
        String query = "UPDATE students SET Name = ?, Gender = ?, DOB = ?, Address = ?, Postcode = ?, StudentNumber = ?, CourseTitle = ?, StartDate = ?, Bursary = ?, Email = ?  WHERE StudentNumber = ?;";

        try {
            conn = getDBConnection();
            conn.setAutoCommit(false); // Transaction start

            stmt = conn.prepareStatement(query);
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getGender());
            stmt.setString(3, student.getDob());
            stmt.setString(4, student.getAddress());
            stmt.setString(5, student.getPostcode());
            stmt.setInt(6, student.getStudentNumber());
            stmt.setString(7, student.getCourseTitle());
            stmt.setString(8, student.getStartDate());
            stmt.setFloat(9, student.getBursary());
            stmt.setString(10, student.getEmail());
            stmt.setInt(11, student.getStudentNumber());
            stmt.executeUpdate();

            return true;
        } catch(SQLException e) {
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

        return false;
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
        String query = "SELECT Password, Token FROM users WHERE Username = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            result = stmt.executeQuery();

            if(result.next() && BCrypt.checkpw(password, result.getString("Password"))) {
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

    /**
     * Checks for an existing user. Usage for registration and preventing duplicate account names.
     * @param username Candidate username
     * @return true/false depending on the outcome of the retrieval.
     */
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

    /**
     * Inserts a new user record into the Users table with a hashed password and randomly generated API token.
     * @param username Candidate username
     * @param password Candidate password
     * @return true/false depending on the outcome of the insertion.
     */
    public static boolean insertUser(String username, String password) {
        if(!checkUserExists(username)) {
            Connection conn = null;
            PreparedStatement stmt = null;
            String query = "INSERT INTO users(Username, Password, Token) VALUES(?, ?, ?);";

            try {
                conn = getDBConnection();
                conn.setAutoCommit(false); // Transaction start

                // Generate unique API key
                String apiKey = generateApiKey();
                while(checkApiKey(apiKey)) {
                    apiKey = generateApiKey();
                }

                // Generate hashed password
                password = generateHash(password);

                stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, apiKey);
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

            if(apiKey != null && checkApiKey(apiKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Take a password before hashing using the BCrypt hashing algorithm and
     * returning the result.
     * @param password A password to be hashed in plain text.
     * @return 60 length hashed salt and password.
     */
    private static String generateHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
