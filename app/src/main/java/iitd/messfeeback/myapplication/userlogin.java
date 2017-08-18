package iitd.messfeeback.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class userlogin extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_URL = "http://10.17.5.66:8080/login/";

    public static  final String CLIENT_ID = "client_id";
    public static  final  String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";



    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView textViewLogin;
    private  TextView  loginSubHeading;

    //progress dialog
    private ProgressDialog progressDialog;

    private String email;
    private String password;
    private String grant_type = "client_credentials";
    private String client_id = "5aR3S6Y3rFOJ0793DsmbLDLfYJHp7K9Wb0Pknegu";
    private String client_secret = "msPUDtanIPnw4Y1daTPoE9WZrlIdnlhhqXybUpfJUcjvxy7BTH6KJLYucp10Ay13zG55AqVvs62AyLCeLklok4nDzHf4inORMHU2l5ybOpatnHrOFV9coDRCDF6yWOGZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);


        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        loginSubHeading = (TextView) findViewById(R.id.loginSubHeading);

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

        Typeface mont_bold = Typeface.createFromAsset(getAssets(), "font/Montserrat-Bold.ttf");
        Typeface mont_med =Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        Typeface mont_light =Typeface.createFromAsset(getAssets(), "font/Montserrat-Light.ttf");
        textViewLogin.setTypeface(mont_bold);
        buttonSignIn.setTypeface(mont_med);
        editTextEmail.setTypeface(mont_med);
        editTextPassword.setTypeface(mont_med);
        textViewSignup.setTypeface(mont_med);
        loginSubHeading.setTypeface(mont_med);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(userlogin.this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void userLogin() {

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        // these two lines are for testing purposed delete after your activity
        //Intent intent = new Intent(userlogin.this, MainActivity.class);
        //startActivity(intent);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.setMessage("Please Wait...");

//                        int status=json
//                        System.out.println("korku"+response);
//                        if(response.trim().equals("success")){
//                            openProfile();
//                        }else{
//                            Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
//                        }
//                    }


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            String token = jObj.getString("access_token");
                            String email = jObj.getString("email");
                            String name = jObj.getString("name");
                            String hostel = jObj.getString("hostel");
                            String id = jObj.getString("id");
                            progressDialog.dismiss();



                            Toast.makeText(getApplicationContext(), "Succesfully LoggedIn", Toast.LENGTH_LONG).show();
                            System.out.println("Deepak Korku"+response);
                            // Launch main activity
                            Intent intent = new Intent(userlogin.this, MainActivity.class);
//                                intent.putExtra("email", email);
//                                intent.putExtra("token", token);
//                                intent.putExtra("name", name);
//                                intent.putExtra("hostel", hostel);
//                                intent.putExtra("id", id);

                            startActivity(intent);

                            SharedPreferences sharedPreferences = userlogin.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.KEY_EMAIL, email);
                            editor.putString(Config.KEY_HOSTEL, hostel);
                            editor.putString(Config.KEY_ID, id);
                            editor.putString(Config.KEY_NAME, name);
                            editor.putString(Config.KEY_TOKEN, token);

                            //Saving values to editor
                            editor.apply();



                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error.toString());
                        String connection_error = "com.android.volley.NoConnectionError: java.net.ConnectException: failed to connect to /10.17.5.66 (port 8080) after 2500ms: isConnected failed: ECONNREFUSED (Connection refused)";
                        if(error.toString().equals(connection_error)){
                            Toast.makeText(userlogin.this,"Please connect to IITD Wifi",Toast.LENGTH_LONG ).show();
                        }
                        else{
                            Toast.makeText(userlogin.this,"Please check username or password",Toast.LENGTH_LONG ).show();
                        }

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(EMAIL,email);
                map.put(PASSWORD,password);
                map.put(CLIENT_ID, client_id);
                map.put(CLIENT_SECRET,client_secret);
                map.put(GRANT_TYPE,grant_type);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    private void openProfile(){
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(EMAIL, email);
//        startActivity(intent);
//    }



    @Override
    public void onClick(View view) {


        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, Signup.class));
        }

        if(view == buttonSignIn){
            userLogin();

        }
    }


}

