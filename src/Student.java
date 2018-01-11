/**
 * Represents a student which is a subclass of {@link Person}.
 * @author Joshua McEwen (16012396)
 */

public class Student extends Person {
    private int studentNumber;
    private String courseTitle;
    private String startDate;
    private float bursary;
    private String email;

    /**
     * Constructor for Student class.
     *
     * @see Person#Person
     * @param sNumber Student's student ID number
     * @param cTitle Student's course title
     * @param startDate Student's start date
     * @param bursary Student's bursary entitlement value
     * @param email Student's email address
     */
    Student(String name, String gender, String dob, String address, String postcode, int sNumber, String cTitle, String startDate, float bursary, String email) {
        super(name, gender, dob, address, postcode);
        this.setStudentNumber(sNumber);
        this.setCourseTitle(cTitle);
        this.setStartDate(startDate);
        this.setBursary(bursary);
        this.setEmail(email);
    }

    public int getStudentNumber() { return this.studentNumber; }

    /**
     * Validates the student number provided as being numeric and having a length of 8 before setting.
     * @param studentNumber Student's eight digit student number
     * @throws IllegalArgumentException if validation fails
     */
    public void setStudentNumber(int studentNumber) {
        if(Integer.toString(studentNumber).matches("^\\d{8}$")) {
            this.studentNumber = studentNumber;
        } else {
            throw new IllegalArgumentException("Student number must be an 8 digit number.");
        }
    }

    public String getCourseTitle() { return this.courseTitle; }

    /**
     * Validates the course title provided as containing word characters, spaces and ('_) only, before setting.
     * @param courseTitle Student's course title
     * @throws IllegalArgumentException if validation fails
     */
    public void setCourseTitle(String courseTitle) {
        if(courseTitle.matches("^[\\w\\s']{1,45}+$")) {
            this.courseTitle = courseTitle;
        } else {
            throw new IllegalArgumentException("Course title must contain letters, spaces and ('_) only.");
        }
    }

    public String getStartDate() { return this.startDate; }

    /**
     * Validates the start date provided as being in the format DD-MM-YYYY, before setting.
     * @param startDate Student's start date
     * @throws IllegalArgumentException if validation fails
     */
    public void setStartDate(String startDate) {
        if(startDate.matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
            this.startDate = startDate;
        } else {
            throw new IllegalArgumentException("Date must be formatted DD-MM-YYYY.");
        }
    }

    public float getBursary() { return this.bursary; }

    /**
     * Validates the bursary value provided as being a float value 1-45 in length, before setting.
     * @param bursary Student's bursary entitlement
     * @throws IllegalArgumentException if validation fails
     */
    public void setBursary(float bursary) {
        if(Float.toString(bursary).matches("^[\\d]+[.]?[\\d]{1,45}$")) {
            this.bursary = bursary;
        } else {
            throw new IllegalArgumentException("Bursary must be numeric.");
        }
    }

    public String getEmail() { return this.email; }

    /**
     * Validates the email as being of standard format, before setting.
     * @param email Student's email address
     * @throws IllegalArgumentException if validation fails
     */
    public void setEmail(String email) {
        if(email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email must be of a standard email format.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                getStudentNumber() + ", " +
                getCourseTitle() + ", " +
                getStartDate() + ", " +
                getBursary() + ", " +
                getEmail();
    }
}
