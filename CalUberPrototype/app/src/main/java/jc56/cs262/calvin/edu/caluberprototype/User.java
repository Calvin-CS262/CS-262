package jc56.cs262.calvin.edu.caluberprototype;

/** User Class
 * Sets up user class for User Table
 * A User is defined by a userID, emailID, password, lastName and firstName
 * Primitive getters and setters defined for each instance variable
 */
public class User {
    int userId;
    String password; //encrypt
    String lastName;
    String firstName;
    String email;

    public User(int userId, String password, String lastName, String firstName, String emailId) {
        this.userId = userId;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = emailId + "@students.calvin.edu";
    }


    public int getUserId() { return userId; }

    public void setUserId(int newUserId) {this.userId = newUserId;}

    public String getPassword() { return password; }

    public void setPassword(String newPassword) { this.password = newPassword; }

    public String getLastName() { return lastName; }

    public void setLastName(String newLastName) { this.lastName = newLastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String newFirstName) { this.firstName = newFirstName; }

    public String getEmail() { return email; }

    public void setEmail(String newEmail) { this.email = newEmail; }

}
