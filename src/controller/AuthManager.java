package controller;

import java.util.*;

import entity.CompanyRepresentative;
import entity.User;

/** Controller class to manage user authentication and registration. */
public class AuthManager {
    /** Reference to the in-memory map of all users. */
    private Map<String, User> allUsers;
    
    /** In-memory map of all company representatives. */
    private Map<String, CompanyRepresentative> allCompanyReps;

    /** Constructs the AuthManager. 
     * @param users Map of all users. 
     * @param reps Map of all company reps. 
    */
    public AuthManager(Map<String, User> users, Map<String, CompanyRepresentative> reps) {
        this.allUsers = users;
        this.allCompanyReps = reps;
    }

    /** Checks if a user exists in the system. 
     * @param userID The User ID to check. 
     * @return true if user exists, false otherwise. 
    */
    public boolean userExists(String userID) {
        return allUsers.containsKey(userID);
    }

    /** Validates user credentials. 
     * @param userID The User ID. 
     * @param password The password. 
     * @return The User object on success, or null on failure. 
    */
    public User login(String userID, String password) {
        User user = allUsers.get(userID);
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /** Changes a user's password. 
     * @param user The User object. 
     * @param oldPassword The user's current password. 
     * @param newPassword The user's new password. 
     * @return true on success, false otherwise. 
    */
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) { return false; }
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        } else {
            return false;
        }
    }

    /** Registers a new company representative. 
     * @param userID The user ID (email). 
     * @param name The user's full name. 
     * @param email The user's email. 
     * @param companyName The company name. 
     * @param department The department. 
     * @param position The position. 
     * @return The new CompanyRepresentative object, or null if user ID already exists. 
    */
    public CompanyRepresentative registerRepresentative(String userID, String name, String email, String companyName, String department, String position) {
        if (userExists(userID)) {
            return null;
        }

        CompanyRepresentative newRep = new CompanyRepresentative( 
            userID, name, email, "password", companyName, department, position
        );
        allUsers.put(userID, newRep);
        allCompanyReps.put(userID, newRep);
        return newRep;
    }
}
