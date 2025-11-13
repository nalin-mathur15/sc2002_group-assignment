package entity;

/** Abstract base class for all users in the system. */
public abstract class User {
    /** The unique ID for the user. */
    private final String userID;

    /** The user's full name. */
    private String name;

    /** The user's password. */
    private String pwd;

    /** The user's email address. */
    private String email;

    /** The user's role (e.g., "Student"). */
    private final String role;

    /** Constructs a new User. 
     * @param userID User ID. 
     * @param name Full name. 
     * @param email Email. 
     * @param pwd Password. 
     * @param role Role. 
    */
    public User(String userID, String name, String email, String pwd, String role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
    }

    //getters
    
    /**
     * Gets the user's unique ID.
     * @return The user ID.
     */
    public String getUserID() { return userID; }
    
    /**
     * Gets the user's full name.
     * @return The user's name.
     */
    public String getName() { return name; }
    
    /**
     * Gets the user's email address.
     * @return The user's email.
     */
    public String getEmail() { return email; }
    
    /**
     * Gets the user's role (e.g., "Student", "Staff").
     * @return The user's role.
     */
    public String getRole() { return role; }
    
    /**
     * Gets the user's password.
     * @return The user's password.
     */
    public String getPassword() { return pwd; }
    
    //setters
    
    /**
     * Sets the user's full name.
     * @param name The new name.
     */
    public void setName(String name) { this.name = name; }
    
    /**
     * Sets the user's password.
     * @param newpwd The new password.
     */
    public void setPassword(String newpwd) { this.pwd = newpwd; }
}
