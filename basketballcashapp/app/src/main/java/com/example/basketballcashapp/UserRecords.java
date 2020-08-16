package com.example.basketballcashapp;

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
        return String.format("User: " + Username+", Score: "+Score+", Time:" + Time);
    }
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof UserRecords)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        UserRecords c = (UserRecords) o;

        // Compare the data members and return accordingly
        return c.Username==this.Username;
    }
}
