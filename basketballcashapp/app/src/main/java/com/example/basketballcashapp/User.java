package com.example.basketballcashapp;

public class User {
    public String Username;
    public String Password;
    public User() {
    }
    public User(String name,String password) {
        this.Username = name;
        this.Password = password;
    }
    @Override
    public String toString() {
        return String.format("User: " + Username+", Pass: " + Password);
    }

}
