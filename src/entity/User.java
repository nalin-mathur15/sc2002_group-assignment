package entity;

public abstract class User {
    // Abstract class for a user. attributes userID, name, password
    private final String userID;
    private String name;
    private String pwd;
    private String email;
    private final String role;

    public User(String userID, String name, String email, String pwd, String role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
    }

    //getters
    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPassword() { return pwd; }
    
    //setters
    public void setName(String name) { this.name = name; }
    public void setPassword(String newpwd) { this.pwd = newpwd; }
}
