package dimo.basketballcashapp;


public class UserRecords {
    public String Username;
    public int Score;
    public int Time;
    public UserRecords() {
    }
    public UserRecords(String name,int score,int time) {
        this.Username = name;
        this.Score = score;
        this.Time = time;
    }
    @Override
    public String toString() {
        return String.format("User: " + Username + ", Score: " + Score + ", Time:" + Time );
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserRecords)) {
            return false;
        }
        UserRecords c = (UserRecords) o;
        return c.Username==this.Username;
    }
}
