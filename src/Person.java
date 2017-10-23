public class Person {
    private String name;
    private String gender;
    private String dob;
    private String address;
    private String postcode;

    Person(String name, String gender, String dob, String address, String postcode) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.postcode = postcode;
    }
    
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return this.gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return this.dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }

    public String getPostcode() { return this.postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }
}

