package controller;

import java.util.*;
import entity.User;

public class AuthManager {
    // authenticate log in and password change

    private Map<String, User> users;
    private Set<String> loggedInUsers;

    public AuthManager(Map<String, User> users) {
        this.users = users;
        loggedInUsers = new HashSet<>();
    }

    // Add user (used during initialization)
    public void addUser(User user) {
        users.put(user.getUserID(), user);
    }

    // Retrieve a user
    public User getUser(String userId) {
        return users.get(userId);
    }

    // Login authentication
    public boolean login(String userId, String password) {
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsers.add(userId);
            System.out.println("Login successful. Welcome, " + user.getName() + "!");
            return true;
        }
        System.out.println("Invalid user ID or password.");
        return false;
    }

    // Logout
    public void logout(User user) {
        if (user != null) {
            loggedInUsers.remove(user.getUserID());
            System.out.println("You have been logged out.");
        }
    }

    // Change password
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            System.out.println("Password changed successfully!");
            return true;
        } else {
            System.out.println("Incorrect old password.");
            return false;
        }
    }

    // Check if logged in
    public boolean isLoggedIn(String userId) {
        return loggedInUsers.contains(userId);
    }
}
