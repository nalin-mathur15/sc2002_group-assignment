package controller;

import java.util.*;

import entity.CompanyRepresentative;
import entity.User;

public class AuthManager {
    private Map<String, User> allUsers;
    private Map<String, CompanyRepresentative> allCompanyReps;

    public AuthManager(Map<String, User> users, Map<String, CompanyRepresentative> reps) {
        this.allUsers = users;
        this.allCompanyReps = reps;
    }

    public boolean userExists(String userID) {
        return allUsers.containsKey(userID);
    }

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

    // Change password
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) { return false; }
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        } else {
            return false;
        }
    }

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
