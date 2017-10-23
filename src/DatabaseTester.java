import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseTester {
    public static void main(String[] args) {
        // Retrieve and print all students.
        System.out.println("Retrieve all students");
        try {
            ArrayList<Student> students = StudentDAO.getAllStudents();

            for(Student s : students) {
                System.out.println(s.getName());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Insert a student from an object.
        System.out.println("Inserting a new student from an object.");
        try {
            boolean inserted = StudentDAO.insertStudent(new Student("James May", "M", "20-01-1951", "Lancaster", "LA4 4RE", 10202344, "Mechanical Engineering", "19-09-2010", 0, "jmay@mmumail.com"));
            if(inserted) {
                System.out.println("New student added.");
            } else {
                System.out.println("Unable to add student.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Update the student we just created.
        System.out.println("Updating student with the ID: 10202344");
        try {
            boolean deleted = StudentDAO.updateStudent(StudentDAO.getStudent(10202344));
            if(deleted) {
                System.out.println("Student updated.");
            } else {
                System.out.println("Unable to update student.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Retrieve the student we just created.
        System.out.println("Retrieve a student with the ID: 10202344");
        try {
            Student student = StudentDAO.getStudent(10202344);
            System.out.println(student.getName());
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Delete the student we just created.
        System.out.println("Deleting the student with the ID: 10202344");
        try {
            boolean deleted = StudentDAO.deleteStudent(10202344);
            if(deleted) {
                System.out.println("Student deleted.");
            } else {
                System.out.println("Unable to delete student.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
