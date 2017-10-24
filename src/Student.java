public class Student extends Person {
    private int studentNumber;
    private String courseTitle;
    private String startDate;
    private float bursary;
    private String email;

    Student(String name, String gender, String dob, String address, String postcode, int sNumber, String cTitle, String startDate, float bursary, String email) {
        super(name, gender, dob, address, postcode);
        // Use the setters when validation implemented?
        this.setStudentNumber(sNumber);
        this.setCourseTitle(cTitle);
        this.setStartDate(startDate);
        this.setBursary(bursary);
        this.setEmail(email);
    }

    public int getStudentNumber() { return this.studentNumber; }
    public void setStudentNumber(int studentNumber) {
        if(Integer.toString(studentNumber).matches("^\\d{8}$")) {
            this.studentNumber = studentNumber;
        } else {
            throw new IllegalArgumentException("Student number must be an 8 digit number.");
        }
    }

    public String getCourseTitle() { return this.courseTitle; }
    public void setCourseTitle(String courseTitle) {
        if(Integer.toString(studentNumber).matches("^[\\w\\s']{1,45}+$")) {
            this.courseTitle = courseTitle;
        } else {
            throw new IllegalArgumentException("Course title must contain letters, spaces and ('_) only.");
        }
    }

    public String getStartDate() { return this.startDate; }
    public void setStartDate(String startDate) {
        if(startDate.matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
            this.startDate = startDate;
        } else {
            throw new IllegalArgumentException("Date must be formatted DD-MM-YYYY.");
        }
    }

    public float getBursary() { return this.bursary; }
    public void setBursary(float bursary) {
        if(Float.toString(bursary).matches("^[\\d]+[.]?[\\d]{1,45}$")) {
            this.bursary = bursary;
        } else {
            throw new IllegalArgumentException("Bursary must be numeric.");
        }
    }

    public String getEmail() { return this.email; }
    public void setEmail(String email) {
        if(email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email must be of a standard email format.");
        }
    }

}
