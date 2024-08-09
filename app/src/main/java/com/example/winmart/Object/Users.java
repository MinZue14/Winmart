package com.example.winmart.Database;

public class Users {
    String userID, username, email, password, address;

    public Users(String userID, String username, String email, String password, String address) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public Users() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}