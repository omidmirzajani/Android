package com.example.basketballcashapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mQuestionReference;
    private DatabaseReference mUsersReference;
    private DatabaseReference mRecordsReference;
    private EditText QuestionBox;
    private EditText CorrectAns;
    private EditText WrongAns1;
    private EditText WrongAns2;
    private EditText WrongAns3;
    private List<Question> listOfQuestions;
    private List<User> listOfUsers;
    private List<UserRecords> listOfRecords;
    private ChildEventListener mChildEventQuestionListener;
    private ChildEventListener mChildEventUserListener;
    private ChildEventListener mChildEventRecordListener;
    private int Score;
    private boolean answered;
    private String CorrectAnswer;
    private Timer mTimer;
    private int round;
    int mTime;
    boolean isFinished;
    private Question myQ;
    private TextView timerTextView;
    public List<Question> tempQuestions;
    public int questionIndex;
    ListView simpleList;
    private List<String> listOfUsernames;
    private String LogedInUser;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQuestionReference = mFirebaseDatabase.getReference().child("Questions");
        mUsersReference = mFirebaseDatabase.getReference().child("Users");
        mRecordsReference = mFirebaseDatabase.getReference().child("Records");

        listOfQuestions = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        listOfUsernames = new ArrayList<>();
        listOfRecords=new ArrayList<>();
        Score = 0;
        round = 2;
        mTime=6;
        tempQuestions = new ArrayList<>();
        questionIndex=0;
        LogedInUser="";

        mChildEventQuestionListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Question friendlyMessage = dataSnapshot.getValue(Question.class);
                listOfQuestions.add(friendlyMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mQuestionReference.addChildEventListener(mChildEventQuestionListener);

        mChildEventUserListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User friendlyMessage = dataSnapshot.getValue(User.class);
                listOfUsers.add(friendlyMessage);
                listOfUsernames.add(friendlyMessage.Username);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mUsersReference.addChildEventListener(mChildEventUserListener);

        mChildEventRecordListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserRecords friendlyMessage = dataSnapshot.getValue(UserRecords.class);
                listOfRecords.add(friendlyMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserRecords friendlyMessage = dataSnapshot.getValue(UserRecords.class);
                int index=0;
                int i=0;
                for (UserRecords ur:listOfRecords){
                    if(ur.Username.equals(friendlyMessage.Username)){
                        index=i;
                        break;
                    }
                    i++;
                }
                listOfRecords.remove(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mRecordsReference.addChildEventListener(mChildEventRecordListener);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }
    public void UsersClicked(View view){
        setContentView(R.layout.user_page);
        timerTextView=findViewById(R.id.timer);
        CancelingMTimer();
        TimerStartingOnUIThread();
        questionIndex=0;
        mTime=0;
        Score=0;
        for(Question q : listOfQuestions){
            tempQuestions.add(q);
        }
        tempQuestions.add(new Question("1","1","","",""));
        tempQuestions.add(new Question("2","2","","",""));
        tempQuestions.add(new Question("3","3","","",""));
        tempQuestions.add(new Question("4","4","","",""));
        tempQuestions.add(new Question("5","5","","",""));
        tempQuestions.add(new Question("6","6","","",""));
        tempQuestions.add(new Question("7","7","","",""));
        tempQuestions.add(new Question("8","8","","",""));
        tempQuestions.add(new Question("9","9","","",""));
        tempQuestions.add(new Question("10","10","","",""));
        tempQuestions = getRandomElement(tempQuestions);
        UI();
    }
    public void UI() {
        myQ = tempQuestions.get(questionIndex);
        UpdateAnswerQues(myQ);
        answered=false;
    }
    private void TimerStartingOnUIThread() {
        mTime = 0;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTime++;
                        timerTextView.setText(mTime + "");
                    }
                });
            }
        }, 0, 1000);
    }
    public void Ans1Clicked(View view){
        Button b=findViewById(R.id.Ans1);
        ChangingColorOfAnswers(b);
    }
    public void Ans2Clicked(View view){
        Button b=findViewById(R.id.Ans2);
        ChangingColorOfAnswers(b);
    }
    public void Ans3Clicked(View view){
        Button b=findViewById(R.id.Ans3);
        ChangingColorOfAnswers(b);
    }
    public void Ans4Clicked(View view){
        Button b=findViewById(R.id.Ans4);
        ChangingColorOfAnswers(b);
    }
    private void ChangingColorOfAnswers(final Button b) {
        if(!answered) {
            answered=true;
            if (b.getText().equals(CorrectAnswer)) {
                Score += 10;
                UpdateScore();
                b.setBackgroundResource(R.drawable.correct_button);
            } else {
                b.setBackgroundResource(R.drawable.wrong_button);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    questionIndex++;
                    if (questionIndex == 10){
                        CancelingMTimer();
                        InsertingInfo();
                    }
                    else
                        UI();
                }
            }, 200);
        }
    }
    private void InsertingInfo() {
        setContentView(R.layout.insert_info_player);
        TextView tv = findViewById(R.id.congratulation);
        boolean flag=false;
        List<UserRecords> tempListOfRecords=new ArrayList<>();
        for (UserRecords ur:listOfRecords) {
            tempListOfRecords.add(ur);
        }
        for (UserRecords ur:tempListOfRecords){
            if(ur.Username.equals(LogedInUser) && !flag){
                if(ur.Score>Score){
                    flag=true;

                }
                else if(ur.Score==Score){
                    if(ur.Time<=mTime){
                        flag=true;

                    }
                    else {
                        deleteData(ur);
                        mRecordsReference.push().setValue(new UserRecords(LogedInUser, Score, mTime));
                        flag=true;

                        break;
                    }
                }
                else{
                    deleteData(ur);
                    mRecordsReference.push().setValue(new UserRecords(LogedInUser, Score, mTime));
                    flag=true;

                    break;
                }
                flag=true;
                break;
            }
        }
        if(!flag){
            mRecordsReference.push().setValue(new UserRecords(LogedInUser, Score, mTime));
            Sort(listOfRecords);
            if(listOfRecords.size()>10){
                deleteData(new UserRecords(listOfRecords.get(10).Username, 1, 1));
            }
        }
        tv.setText("Congratulation!\nYour earned " + Score + " points in " + mTime + " seconds.");
    }

    private void Sort(List<UserRecords> listOfRecords) {
        for(int i=0;i<listOfRecords.size();i++){
            for(int j=i+1;j<listOfRecords.size();j++){
                if(listOfRecords.get(i).Score<listOfRecords.get(j).Score){
                    Swap(listOfRecords,i,j);
                }
                else if(listOfRecords.get(i).Score==listOfRecords.get(j).Score){
                    if(listOfRecords.get(i).Time>listOfRecords.get(j).Time){
                        Swap(listOfRecords,i,j);
                    }
                }
            }
        }
    }
    private void Swap(List<UserRecords> listOfRecords, int i, int j) {
        String temp = listOfRecords.get(i).Username;
        listOfRecords.get(i).Username = listOfRecords.get(j).Username;
        listOfRecords.get(j).Username = temp;

        int temp1 = listOfRecords.get(i).Score;
        listOfRecords.get(i).Score = listOfRecords.get(j).Score;
        listOfRecords.get(j).Score = temp1;

        temp1 = listOfRecords.get(i).Time;
        listOfRecords.get(i).Time = listOfRecords.get(j).Time;
        listOfRecords.get(j).Time = temp1;
    }

    public void AdminsClicked(View view){
        setContentView(R.layout.admin_verification);
    }
    public void BackIconClicked(View view){
        setContentView(R.layout.activity_main);
        CancelingMTimer();
    }
    public List<Question> getRandomElement(List<Question> list) {
        Random rand = new Random();

        List<Question> newList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int randomIndex = rand.nextInt(list.size());
            newList.add(list.get(randomIndex));
            list.remove(randomIndex);
        }
        return newList;
    }
    public void ShowUserInfo(View view){
        setContentView(R.layout.users_info);
        simpleList = (ListView) findViewById(R.id.UserListView);
        List<String> mine=new ArrayList<>();
        for (User u : listOfUsers)
            mine.add(u.toString());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, mine);
        simpleList.setAdapter(arrayAdapter);
    }
    public void InsertQuestion(View view){
        setContentView(R.layout.admin_page);
        QuestionBox= findViewById(R.id.Question);
        CorrectAns= findViewById(R.id.CorrectAns);
        WrongAns1= findViewById(R.id.WrongAns1);
        WrongAns2= findViewById(R.id.WrongAns2);
        WrongAns3= findViewById(R.id.WrongAns3);
    }
    public void SignInAdminClicked(View view){
        EditText u = findViewById(R.id.UsernameOfAdminEditText);
        String username=u.getText().toString();
        EditText p = findViewById(R.id.PasswordOfAdminEditText);
        String password=p.getText().toString();

        if((username.equals("Jorge")) && (password.equals("12345"))){
            setContentView(R.layout.all_admin_page);
        }
        else{
            WrongAdminVerifyAlert();
        }
    }
    public void SubmitQuestion(View view){
        Alert();
    }
    public void Alert() {
        String s = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(s + "Do you want to add this question?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: Send messages on click
                        String question = QuestionBox.getText().toString();
                        String cAns = CorrectAns.getText().toString();
                        String wAns1 = WrongAns1.getText().toString();
                        String wAns2 = WrongAns2.getText().toString();
                        String wAns3 = WrongAns3.getText().toString();
                        Question myQuestion = new Question(question, cAns, wAns1, wAns2, wAns3);

                        mQuestionReference.push().setValue(myQuestion);
                        // Clear input box
                        QuestionBox.setText("");
                        CorrectAns.setText("");
                        WrongAns1.setText("");
                        WrongAns2.setText("");
                        WrongAns3.setText("");

                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Closed",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create();
        builder.show();
    }
    public void FinishedAlert() {
        CancelingMTimer();
        String s = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(s + "Congratulation!\nYour earned "+Score+" points in "+mTime+" seconds.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
        setContentView(R.layout.activity_main);
    }
    public void CancelingMTimer(){
        try {
            mTimer.cancel();
        }
        catch (Exception e){}
    }
    public void WrongAdminVerifyAlert() {
        String s = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(s + "Username or password is wrong.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }
    public void UpdateScore() {
        TextView tv = (TextView) findViewById(R.id.ScoreTextView);
        tv.setText(Score+"");
    }
    public void UpdateAnswerQues(Question q) {
        CorrectAnswer=q.getQ1();
        TextView qTV = findViewById(R.id.QuestionTextView);
        TextView a1TV = findViewById(R.id.Ans1);
        TextView a2TV = findViewById(R.id.Ans2);
        TextView a3TV = findViewById(R.id.Ans3);
        TextView a4TV = findViewById(R.id.Ans4);

        String[] arr = new String[4];
        arr[0] = q.getQ1();
        arr[1] = q.getQ2();
        arr[2] = q.getQ3();
        arr[3] = q.getQ4();

        int[] numArray = new int[4];
        numArray[0] = 0;
        numArray[1] = 1;
        numArray[2] = 2;
        numArray[3] = 3;
        shuffleArray(numArray);

        qTV.setText(q.getText());
        a1TV.setText(arr[numArray[0]]);
        a2TV.setText(arr[numArray[1]]);
        a3TV.setText(arr[numArray[2]]);
        a4TV.setText(arr[numArray[3]]);

        a1TV.setBackgroundResource(R.drawable.normal_button);
        a2TV.setBackgroundResource(R.drawable.normal_button);
        a3TV.setBackgroundResource(R.drawable.normal_button);
        a4TV.setBackgroundResource(R.drawable.normal_button);
    }
    static void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    public void ShowScoreboard(View view){
        setContentView(R.layout.scoreboard);

        simpleList = (ListView) findViewById(R.id.simpleListView);
        Sort(listOfRecords);
        List<String> ScoreBoard=new ArrayList<>();
        for (UserRecords ur : listOfRecords){
            ScoreBoard.add(ur.toString());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, ScoreBoard);
        simpleList.setAdapter(arrayAdapter);
    }
    public void CheckUsernamePass(View view){
        LogedInUser="";
        boolean flag=false;
        String un=((EditText)findViewById(R.id.userEdittext)).getText().toString();
        String pw=((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        for (User u:listOfUsers){
            if((u.Username.equals(un))&&(u.Password.equals(pw))){
                flag=true;
                break;
            }
        }
        if(flag) {
            findViewById(R.id.wrongUserOrPass).setVisibility(View.INVISIBLE);
            findViewById(R.id.userEdittext).setBackgroundResource(R.drawable.my_edittext);
            findViewById(R.id.passwordEditText).setBackgroundResource(R.drawable.my_edittext);
            LogedInUser = un;
            setContentView(R.layout.user_page);
            UsersClicked(view);
        }
        else {
            findViewById(R.id.wrongUserOrPass).setVisibility(View.VISIBLE);
            findViewById(R.id.userEdittext).setBackgroundResource(R.drawable.wrong_user_or_pass);
            findViewById(R.id.passwordEditText).setBackgroundResource(R.drawable.wrong_user_or_pass);
        }
    }
    public void SignUpUser(View view){
        setContentView(R.layout.sign_up_users);
        CancelingMTimer();
        mTimer = new Timer();
        final EditText un=findViewById(R.id.RegisteruserEdittext);
        final EditText pw=findViewById(R.id.RegisterPasswordEdittext);
        final EditText rp=findViewById(R.id.RepeatRegisterPasswordEdittext);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!un.getText().toString().equals("")) {
                            if (MyContain(listOfUsernames, un.getText().toString())) {
                                un.setBackgroundResource(R.drawable.wrong_user_or_pass);
                                findViewById(R.id.CorrectUsername).setVisibility(View.INVISIBLE);
                                findViewById(R.id.takenUsername).setVisibility(View.VISIBLE);
                            } else {
                                un.setBackgroundResource(R.drawable.correct_user_or_pass);
                                findViewById(R.id.takenUsername).setVisibility(View.INVISIBLE);
                                findViewById(R.id.CorrectUsername).setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            un.setBackgroundResource(R.drawable.my_edittext);
                            findViewById(R.id.CorrectUsername).setVisibility(View.INVISIBLE);
                            findViewById(R.id.takenUsername).setVisibility(View.INVISIBLE);

                        }

                        if(!pw.getText().toString().equals("") || !rp.getText().toString().equals("")) {
                            if (pw.getText().toString().equals(rp.getText().toString())) {
                                pw.setBackgroundResource(R.drawable.correct_user_or_pass);
                                rp.setBackgroundResource(R.drawable.correct_user_or_pass);
                            } else {
                                pw.setBackgroundResource(R.drawable.wrong_user_or_pass);
                                rp.setBackgroundResource(R.drawable.wrong_user_or_pass);
                            }
                        }
                        else{
                            pw.setBackgroundResource(R.drawable.my_edittext);
                            rp.setBackgroundResource(R.drawable.my_edittext);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private boolean MyContain(List<String> listOf, String toString) {
        for (String s : listOf) {
            if (s.equals(toString))
                return true;
        }
        return false;
    }
    public void RegisterAccount(View view) {
        final EditText un = findViewById(R.id.RegisteruserEdittext);
        final EditText pw = findViewById(R.id.RegisterPasswordEdittext);
        final EditText rp = findViewById(R.id.RepeatRegisterPasswordEdittext);
        String s = "";
        int i = 1;
        if (un.getText().toString().equals("")) {
            s += i + ". Username cannot be empty\n";
            i++;
        }
        if (findViewById(R.id.takenUsername).getVisibility() == View.VISIBLE) {
            s += i + ". This username is taken\n";
            i++;
        }
        if (pw.getText().toString().equals("") || rp.getText().toString().equals("")) {
            s += i + ". Password cannot be empty\n";
            i++;
        }
        if (!pw.getText().toString().equals(rp.getText().toString())) {
            s += i + ". Missmatch in password\n";
            i++;
        }
        if(!s.equals(""))
            WrongRegistering(s);
        else {
            User mUser = new User(un.getText().toString(), pw.getText().toString());
            mUsersReference.push().setValue(mUser);
            CancelingMTimer();
            CancelingMTimer();
            setContentView(R.layout.activity_main);
        }
    }
    public void WrongRegistering(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Note that\n\n"+s)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }
    public void adminShowing(View v){
        CancelingMTimer();
        setContentView(R.layout.all_admin_page);
    }
    private void deleteData(UserRecords strTitle){
        Query deleteQuery = mRecordsReference.orderByChild("Username").equalTo(strTitle.Username);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot delData: dataSnapshot.getChildren()){
                    delData.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
//        Toast.makeText(getApplicationContext(),"User button is clicked",Toast.LENGTH_LONG).show();
