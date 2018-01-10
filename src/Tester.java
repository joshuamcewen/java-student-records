/**
 * Testing of {@link Person} and {@link Student} classes.
 * @author Joshua McEwen (16012396)
 */
public class Tester {
    /**
     * Instantiates new test objects of the Person and Student class for testing.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        Person testPerson = new Person("James Mayle", "M", "20-01-1951", "Lancaster", "LA4 4RE");
        System.out.println(testPerson);

        Student testStudent = new Student("James Mayle", "M", "20-01-1951", "Lancaster", "LA4 4RE", 10202344, "Mechanical Engineering", "19-09-2010", 0, "jmay@mmumail.com");
        System.out.println(testStudent);
    }
}
