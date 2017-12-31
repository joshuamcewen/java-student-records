import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Entry point for testing {@link StudentDAO}.
 * @author Joshua McEwen (16012396)
 */

public class DatabaseTester {
    /**
     * Entry point for executing methods from the {@link StudentDAO} class. Used for demonstrating
     * the functionality of said methods.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // Retrieve and print all students.
        System.out.println("Retrieve all students");
        ArrayList<Student> students = StudentDAO.getAllStudents();

        for(Student s : students) {
            System.out.println(s.getName());
        }

        System.out.println();

        // Insert a student from an object.
        System.out.println("Inserting a new student from an object.");
        boolean inserted = StudentDAO.insertStudent(new Student("James May", "M", "20-01-1951", "Lancaster", "LA4 4RE", 10202344, "Mechanical Engineering", "19-09-2010", 0, "jmay@mmumail.com"));
        if(inserted) {
            System.out.println("New student added.");
        } else {
            System.out.println("Unable to add student.");
        }

        System.out.println();

        //  Update the student we just created.
        System.out.println("Updating student with the ID: 10202344");
        boolean updated = StudentDAO.updateStudent(new Student("James Mayle", "M", "20-01-1951", "Lancaster", "LA4 4RE", 10202344, "Mechanical Engineering", "19-09-2010", 0, "jmay@mmumail.com"));
        if(updated) {
            System.out.println("Student updated.");
        } else {
            System.out.println("Unable to update student.");
        }

        System.out.println();

        // Retrieve the student we just created.
        System.out.println("Retrieve a student with the ID: 10202344");
        Student student = StudentDAO.getStudent(10202344);
        System.out.println(student.getName());

        System.out.println();

        // Delete the student we just created.
        System.out.println("Deleting the student with the ID: 10202344");

        boolean deleted = StudentDAO.deleteStudent(10202344);
        if(deleted) {
            System.out.println("Student deleted.");
        } else {
            System.out.println("Unable to delete student.");
        }
    }
}
