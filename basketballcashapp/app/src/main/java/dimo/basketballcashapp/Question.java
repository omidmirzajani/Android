package dimo.basketballcashapp;


public class Question {

    private String text;
    private String q1;
    private String q2;
    private String q3;
    private String q4;

    public Question() {
    }

    public Question(String text, String q1,String q2,String q3,String q4) {
        this.text = text;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQ1() {
        return q1;
    }
    public void setQ1(String text) {
        this.q1 = text;
    }

    public String getQ2() {
        return q2;
    }
    public void setQ2(String text) {
        this.q2 = text;
    }

    public String getQ3() {
        return q3;
    }
    public void setQ3(String text) {
        this.q3 = text;
    }

    public String getQ4() {
        return q4;
    }
    public void setQ4(String text) {
        this.q4 = text;
    }
}
