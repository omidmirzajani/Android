package dimo.basketballcashapp;


public class User {
    public String Username;
    public String Email;
    public String Password;
    public int NumOfPlays;

    public User() {
    }

    public User(String name, String mail, String password, int num) {
        this.Username = name;
        this.Email = mail;
        this.Password = password;
        this.NumOfPlays = num;
    }

    @Override
    public String toString() {
        return String.format("User: " + Username + ", Email: " + Email + ", Plays: " + NumOfPlays);
    }
}
