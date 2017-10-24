import java.sql.*;
import java.util.ArrayList;

public class StudentDAO {
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

    static ArrayList<Student> getAllStudents() throws SQLException {
        ArrayList<Student> students = new ArrayList<Student>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
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
            if(result != null) { result.close(); }
            if(stmt != null) { stmt.close(); }
            if(conn != null) { conn.close(); }
        }

        return students;
    }

    static Student getStudent(int stuNumber) throws SQLException {
        Student student = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        String query = "SELECT * FROM students WHERE StudentNumber = ?;";

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, stuNumber);
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

                return new Student(name, gender, dob, address, postcode, sNumber, cTitle, startDate, bursary, email);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(result != null) { result.close(); }
            if(stmt != null) { stmt.close(); }
            if(conn != null) { conn.close(); }
        }

        return student;
    }

    static boolean insertStudent(Student student) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "INSERT INTO students(Name, Gender, DOB, Address, Postcode, StudentNumber, CourseTitle, StartDate, Bursary, Email) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            conn = getDBConnection();
            conn.setAutoCommit(false);

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
            if(stmt != null) { stmt.close(); }
            if(conn != null) {
                conn.commit();
                conn.close();
            }
        }

        return false;
    }

    static boolean deleteStudent(int stuNumber) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "DELETE FROM students WHERE StudentNumber = ?;";

        try {
            conn = getDBConnection();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, stuNumber);
            stmt.executeUpdate();

            return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(stmt != null) { stmt.close(); }
            if(conn != null) {
                conn.commit();
                conn.close();
            }
        }

        return false;
    }

    static boolean updateStudent(Student student) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "UPDATE students SET Name = ?, Gender = ?, DOB = ?, Address = ?, Postcode = ?, StudentNumber = ?, CourseTitle = ?, StartDate = ?, Bursary = ?, Email = ?  WHERE StudentNumber = ?;";

        try {
            conn = getDBConnection();
            conn.setAutoCommit(false);

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
            if(stmt != null) { stmt.close(); }
            if(conn != null) {
                conn.commit();
                conn.close();
            }
        }

        return false;
    }

    boolean checkLoginCredentials(String username, String password) {
        return false;
    }

    boolean checkApiKey(String token) {
        return false;
    }
}
