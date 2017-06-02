package com.example.korku02.disa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class feedback extends AppCompatActivity implements View.OnClickListener{
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private String username, hostel ,date;
    //defining a database reference
    private DatabaseReference databaseReference;
    private EditText submitRating;
    private Button buttonSubmit ;
    private String datetimeString;
    private String timeString;
    private String dateString;
    private RatingBar ratingBar;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        hostel = intent.getExtras().getString("hostel");


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();


        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        }
        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        submitRating = (EditText) findViewById(R.id.submitRating);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //add listner to button
        buttonSubmit.setOnClickListener(this);




    }

    private void saveFeedback() {

        //Getting values from database
        //String rating = submitRating.getText().toString().trim();
        String rating1 =String.valueOf(ratingBar.getRating());

        String rating = rating1.trim();
        //creating a userinformation object
        feedbackData data = new feedbackData(rating);

        //getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //sending information to database


        //timestamp from system
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();

        long millisecond = Long.parseLong(ts);
        // or you already have long value of date, use this instead of milliseconds variable.
        datetimeString = DateFormat.format("MM-dd-yyyy hh:mm:ss a", new Date(millisecond)).toString();
        timeString = datetimeString.substring(11);
        dateString = datetimeString.substring(0,10);

        String t1  = datetimeString.substring(11,13);
        String t2 = datetimeString.substring(20,21);


        int st1 = Integer.parseInt(t1);


        if((st1 >=1 && st1<=3 && t2.equals("p")) || (st1 ==11 && t2.equals("a")) || (st1 ==12 && t2.equals("p"))){
            String type = "Lunch";

            databaseReference.child("Hostels/"+hostel+"/"+dateString+"/"+username+"/"+type).setValue(data);
            //displaying a success toast
            Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
        }

        else if((st1 >=6 && st1<=10 && t2.equals("a"))){
            String type = "Breakfast";

            databaseReference.child("Hostels/"+hostel+"/"+dateString+"/"+username+"/"+type).setValue(data);
            //displaying a success toast
            Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
        }

        else if((st1 >=6 && st1<=10 && t2.equals("p"))) {

                String type = "Dinner";

            databaseReference.child("Hostels/"+hostel+"/"+dateString+"/"+username+"/"+type).setValue(data);
            //displaying a success toast
            Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
        }

        else{
            Toast.makeText(this, "No meal is going on Right now", Toast.LENGTH_LONG).show();
        }

       // String id = databaseReference.push().getKey();








    }


    @Override
    public void onClick(View v) {
        if (v == buttonSubmit){
            saveFeedback();
            finish();
            startActivity(new Intent(this, profile.class));
        }
    }
}
