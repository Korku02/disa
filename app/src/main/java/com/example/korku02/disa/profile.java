package com.example.korku02.disa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com.google.firebase.database.ValueEventListener;


public class profile extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;


    //defining a database reference
    private DatabaseReference databaseReference;

    //our new views
    private TextView editTextUserName, editTextUserHostel;

    private Button buttonScanner;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        editTextUserHostel = (TextView) findViewById(R.id.editTextUserHostel);
        editTextUserName = (TextView) findViewById(R.id.editTextUserName);
        buttonScanner = (Button) findViewById(R.id.buttonScanner);






        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);



        //adding listener to button
        buttonLogout.setOnClickListener(this);

        buttonScanner.setOnClickListener(this);

    }



    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Getting the data from snapshot
                UserInformation person = dataSnapshot.getValue(UserInformation.class);



                //Adding it to a string
                String userName = "Name: "+person.getName();
                if(userName == null){
                    userName = " ";
                }
                String userHostel = "Address: "+person.getHostel();

                if(userHostel == null){
                    userHostel = " ";
                }
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //displaying logged in user name
                textViewUserEmail.setText("Welcome " + person.getName());

                //Displaying it on textview
                editTextUserName.setText(userName);
                editTextUserHostel.setText(userHostel);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());


            }
        });
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if (view == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        }



        if(view == buttonScanner){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Getting the data from snapshot
                    UserInformation person = dataSnapshot.getValue(UserInformation.class);
                    String username  = person.getName();
                    String hostel  = person.getHostel();

                    Intent intent = new Intent(getApplicationContext(), scanner.class);
                    intent.putExtra("username", username);
                    intent.putExtra("hostel", hostel);
                    startActivity(intent);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());


                }
            });
        }

        //if(view == buttonFeedback){
            //startActivity(new Intent(this, feedback.class));

         //   databaseReference.addValueEventListener(new ValueEventListener() {
           //     @Override
            //    public void onDataChange(DataSnapshot dataSnapshot) {
                    //Getting the data from snapshot
                //    UserInformation person = dataSnapshot.getValue(UserInformation.class);
                  //   String username  = person.getName();
                    // String hostel  = person.getHostel();

                  //  Toast.makeText(profile.this, username , Toast.LENGTH_LONG).show();
                 //   Toast.makeText(profile.this, hostel , Toast.LENGTH_LONG).show();
                  //  Intent intent = new Intent(getApplicationContext(), feedback.class);
                 //   intent.putExtra("username", username);
                  //  intent.putExtra("hostel", hostel);
                  //  startActivity(intent);

               // }

              //  @Override
              //  public void onCancelled(DatabaseError databaseError) {
                ///    System.out.println("The read failed: " + databaseError.getMessage());


               // }
            //});

       // }
    }

}
