/**
 * Represents a person
 * @author Joshua McEwen (16012396)
 */

public class Person {

    private String name;
    private String gender;
    private String dob;
    private String address;
    private String postcode;

    /**
     * Constructor for Person class.
     *
     * @param name Person's full name
     * @param gender Person's gender
     * @param dob Person's date of birth
     * @param address Person's address
     * @param postcode Person's postcode
     */
    Person(String name, String gender, String dob, String address, String postcode) {
        this.setName(name);
        this.setGender(gender);
        this.setDob(dob);
        this.setAddress(address);
        this.setPostcode(postcode);
    }
    
    String getName() { return this.name; }
    public void setName(String name) {
        if(name.matches("^[A-Za-z\\s]{1,45}+$")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must contain letters and spaces only.");
        }
    }

    String getGender() { return this.gender; }

    /**
     * Validates the gender as containing letters only and being of 1-7 characters in length, before setting.
     * @param gender Person's gender
     * @throws IllegalArgumentException if validation fails
     */
    public void setGender(String gender) {
        if(gender.matches("^[A-Za-z]{1,7}+$")) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Gender must contain letters only.");
        }
    }

    String getDob() { return this.dob; }

    /**
     * Validates the date of birth provided as being in the format DD-MM-YYYY, before setting.
     * @param dob Person's date of birth
     * @throws IllegalArgumentException if validation fails
     */
    public void setDob(String dob) {
        if(dob.matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
            this.dob = dob;
        } else {
            throw new IllegalArgumentException("Date must be formatted DD-MM-YYYY.");
        }
    }

    String getAddress() { return this.address; }

    /**
     * Validates the address as containing letters, whitespace and (.) only as well as being 1-60 characters in length, before setting.
     * @param address Person's address
     * @throws IllegalArgumentException if validation fails
     */
    public void setAddress(String address) {
        if(address.matches("^[A-Za-z'.\\s]{1,60}+$")) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Address must contain letters, numbers and ('.) only.");
        }
    }

    String getPostcode() { return this.postcode; }

    /**
     * Validates the postcode as being in a standard UK format, before setting.
     * @param postcode Person's postcode
     * @throws IllegalArgumentException if validation fails
     */
    public void setPostcode(String postcode) {
        if(postcode.matches("^[A-Z]{1,2}[0-9][0-9A-Z]?\\s?[0-9][A-Z][A-Z]$")) {
            this.postcode = postcode;
        } else {
            throw new IllegalArgumentException("Postcode must be formatted in UK standard.");
        }
    }

    @Override
    public String toString() {
        return getName() + ", " +
                getGender() + ", " +
                getDob() + ", " +
                getAddress() + ", " +
                getPostcode();
    }
}

