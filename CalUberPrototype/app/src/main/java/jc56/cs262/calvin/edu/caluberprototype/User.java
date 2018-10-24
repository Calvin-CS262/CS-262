package jc56.cs262.calvin.edu.caluberprototype;

public class User {
    int userId;
    String studentId;
    String password; //encrypt
    String lastName;
    String firstName;
    String email;

    public User(int userId, String studentId, String password, String lastName, String firstName) {
        this.userId = userId;
        this.studentId = studentId;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
    }


    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getStudentId() { return studentId; }

    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void getEmail(String studentId){
        email = studentId + "@studnets.calvin.edu";
    }

}
