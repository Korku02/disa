package iitd.messfeeback.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    public static final String REGISTER_URL = "http://10.17.5.66:8080/register/";

    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String ID = "user_id";
    public static final String HOSTEL = "user_hostel";
    public static final String EMAIL = "email";

    //defining view objects
    private EditText editTextEmail, editTextPassword, editTextUsername, editTextEntryNo;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private Spinner spinnerHostel;
    private TextView textViewLogin, signupSubHeading, signupHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEntryNo = (EditText) findViewById(R.id.editTextEntryNo);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        spinnerHostel = (Spinner) findViewById(R.id.spinnerHostel);
        signupSubHeading = (TextView) findViewById(R.id.signupSubHeading);
        signupHeading = (TextView) findViewById(R.id.signupHeading);

        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        Typeface mont_bold = Typeface.createFromAsset(getAssets(), "font/Montserrat-Bold.ttf");
        Typeface mont_med =Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        Typeface mont_light =Typeface.createFromAsset(getAssets(), "font/Montserrat-Light.ttf");
        textViewLogin.setTypeface(mont_med);
        editTextEntryNo.setTypeface(mont_med);
        editTextUsername.setTypeface(mont_med);
        editTextPassword.setTypeface(mont_med);
        editTextEmail.setTypeface(mont_med);
        signupSubHeading.setTypeface(mont_med);
        buttonSignup.setTypeface(mont_med);
        signupHeading.setTypeface(mont_bold);


    }

    private void registerUser() {

        final String user_name = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String user_id = editTextEntryNo.getText().toString().trim();
        final String user_hostel = spinnerHostel.getSelectedItem().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(Signup.this,response,Toast.LENGTH_LONG).show();
                        Toast.makeText(Signup.this,"successfully registered",Toast.LENGTH_LONG).show();

                        if(response !=null ){
                            startActivity(new Intent(getApplicationContext(), userlogin.class));
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Signup.this,error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(Signup.this,"Please connect to wifi",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(USER_NAME,user_name);
                params.put(PASSWORD,password);
                params.put(EMAIL, email);
                params.put(ID, user_id);
                params.put(HOSTEL, user_hostel);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignup){
            registerUser();

        }
    }
}
