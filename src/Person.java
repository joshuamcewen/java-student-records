import java.util.IllegalFormatException;

public class Person {
    private String name;
    private String gender;
    private String dob;
    private String address;
    private String postcode;

    Person(String name, String gender, String dob, String address, String postcode) {
        this.setName(name);
        this.setGender(gender);
        this.setDob(dob);
        this.setAddress(address);
        this.setPostcode(postcode);
    }
    
    String getName() { return this.name; }
    void setName(String name) {
        if(name.matches("^[A-Za-z\\s]{1,45}+$")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must contain letters and spaces only.");
        }
    }

    String getGender() { return this.gender; }
    void setGender(String gender) {
        if(gender.matches("^[A-Za-z]{1,7}+$")) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Gender must contain letters only.");
        }
    }

    String getDob() { return this.dob; }
    void setDob(String dob) {
        if(dob.matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
            this.dob = dob;
        } else {
            throw new IllegalArgumentException("Date must be formatted DD-MM-YYYY.");
        }
    }

    String getAddress() { return this.address; }
    void setAddress(String address) {
        if(address.matches("^[A-Za-z'.\\s]{1,60}+$")) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Address must contain letters, numbers and ('.) only.");
        }
    }

    String getPostcode() { return this.postcode; }
    void setPostcode(String postcode) {
        if(postcode.matches("^[A-Z]{1,2}[0-9][0-9A-Z]?\\s?[0-9][A-Z][A-Z]$")) {
            this.postcode = postcode;
        } else {
            throw new IllegalArgumentException("Postcode must be formatted in UK standard.");
        }
    }
}

