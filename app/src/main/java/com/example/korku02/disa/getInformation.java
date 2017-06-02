package com.example.korku02.disa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class getInformation extends AppCompatActivity implements View.OnClickListener {


    //firebase auth object
    private FirebaseAuth firebaseAuth;
    //defining a database reference
    private DatabaseReference databaseReference;

    //our new views
    private EditText editTextHostelName, editTextName;
    private Button buttonSubmitInformation;
    private Button buttonLogout;
    private Spinner spinnnerHostel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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


        FirebaseUser user = firebaseAuth.getCurrentUser();

        String uid = user.getUid();

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(uid);

        //getting the views from xml resource

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonSubmitInformation = (Button) findViewById(R.id.buttonSubmitInformation);
        spinnnerHostel = (Spinner)findViewById(R.id.spinnerHostel);

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSubmitInformation.setOnClickListener(this);




    }

    private void saveUserInformation() {

        //Getting values from database
        String name = editTextName.getText().toString().trim();
        String hostel = spinnnerHostel.getSelectedItem().toString().trim();

        //creating a userinformation object
        UserInformation userInformation = new UserInformation(name, hostel);

        //getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();



        //sending information to database
        databaseReference.setValue(userInformation);

        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();


        editTextName.setText("");



    }


    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        }

        if(v == buttonSubmitInformation){
            saveUserInformation();

            startActivity(new Intent(this, profile.class));
            finish();
        }

    }
}
