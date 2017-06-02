package com.example.korku02.disa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scanner extends AppCompatActivity implements View.OnClickListener{
    //View Objects
    private Button buttonScan;
    private static String TAG;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String hostel;
    private Button buttonLogout;

    //qr code scanner object
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TAG = "app";
        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);


        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);


    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                //try {
                    //converting the data to json
                   // JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    Log.d(TAG,result.getContents()+"     "+getIntent().getExtras().getString("hostel"));
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
                databaseReference = FirebaseDatabase.getInstance().getReference(uid);
               // } catch (JSONException e) {
                 //   e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                String hostel = getIntent().getExtras().getString("hostel");
                if (hostel == null){
                    hostel = "";
                }
                if(hostel.equals(result.getContents())){
                    Log.d(TAG, "done");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Getting the data from snapshot
                            UserInformation person = dataSnapshot.getValue(UserInformation.class);
                            String username  = person.getName();
                            String hostel  = person.getHostel();

                            Intent intent = new Intent(getApplicationContext(), feedback.class);
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

                else{
                    Toast.makeText(this, "You are not a resident of"+result.getContents(), Toast.LENGTH_LONG).show();
                }

                    Toast.makeText(this, "Retrieved Data Successfully", Toast.LENGTH_LONG).show();
               // }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == buttonLogout) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        }
        //initiating the qr code scan
        if(view == buttonScan){
        qrScan.initiateScan();
        }

    }

    //if

}
