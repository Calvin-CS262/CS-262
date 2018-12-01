package jc56.cs262.calvin.edu.caluberprototype;

public class Person {
    private int personId;
    private String email;
    private String password; //encrypt
    private String lastName;
    private String firstName;

    public Person() {
        // The JSON marshaller used by Endpoints requires this default constructor.
    }

    public Person(int personId, String emailId, String password, String lastName, String firstName) {
        this.personId = personId;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        //This makes sure that if the incoming email ends with @students.calvin.edu, it doesn't get added again
        String[] atEmail = emailId.split("@");
        if (atEmail.length > 1) {
            this.email = emailId;
        } else {
            this.email = emailId + "@students.calvin.edu";
        }
    }


    public int getPersonId() { return personId; }

    public void setPersonId(int personId) {this.personId = personId;}

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
