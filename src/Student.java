public class Student extends Person {
    private int studentNumber;
    private String courseTitle;
    private String startDate;
    private float bursary;
    private String email;

    Student(String name, String gender, String dob, String address, String postcode, int sNumber, String cTitle, String startDate, float bursary, String email) {
        super(name, gender, dob, address, postcode);
        // Use the setters when validation implemented?
        this.studentNumber = sNumber;
        this.courseTitle = cTitle;
        this.startDate = startDate;
        this.bursary = bursary;
        this.email = email;
    }

    public int getStudentNumber() { return this.studentNumber; }
    public void setStudentNumber(int studentNumber) { this.studentNumber = studentNumber; }

    public String getCourseTitle() { return this.courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getStartDate() { return this.startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public float getBursary() { return this.bursary; }
    public void setBursary(float bursary) { this.bursary = bursary; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

}
