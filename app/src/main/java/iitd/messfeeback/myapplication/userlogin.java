package iitd.messfeeback.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private Button wifi_button;
    Context context = this;

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
            finish();
        }
    }

    public void checkWifi(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                System.out.println("connected to wifi");
//                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
//                System.out.println("Please connect to wifi");
                // custom dialog

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.wifidialog);


                try
                {
                    Thread.sleep(2000);//1sec
                    dialog.show();
                    wifi_button = (Button)dialog.findViewById(R.id.wifi_button);
                    wifi_button.setOnClickListener(new View.OnClickListener() // the error is on this line (specifically the .setOnClickListener)
                    {
                        @Override
                        public void onClick(View v)
                        {
                            System.out.println("wifi_button_clicked");
//                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            // TODO Auto-generated method stub
                            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                            intent.setComponent(cn);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            dialog.dismiss();
                            startActivity(intent);

                        }
                    });
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                }

//                Toast.makeText(context, "Switch to IITD/edurom wifi from Mobile Data", Toast.LENGTH_SHORT).show();
            }
        } else {
            // not connected to the internet
            Toast.makeText(context, "Please connect to IITD/edurom wifi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkWifi();
    }

    private void userLogin() {

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        // these two lines are for testing purposed delete after your activity
        //Intent intent = new Intent(userlogin.this, MainActivity.class);
        //startActivity(intent);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
                            finish();

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

                        try {
                            String errorString = new String(error.networkResponse.data);
                            JSONObject errorObj = new JSONObject(errorString);
                            String errorMessage = errorObj.getString("error");
                            Toast.makeText(userlogin.this,errorMessage,Toast.LENGTH_LONG ).show();
                            progressDialog.dismiss();
                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(userlogin.this,"Connection Error",Toast.LENGTH_LONG ).show();

                        }
//                        System.out.println("error volley");
//                        String connection_error = "com.android.volley.NoConnectionError: java.net.ConnectException: failed to connect to /10.17.5.66 (port 8080) after 2500ms: isConnected failed: ECONNREFUSED (Connection refused)";
//                        if(error.toString().equals(connection_error)){
//                            Toast.makeText(userlogin.this,"Please connect to IITD Wifi",Toast.LENGTH_LONG ).show();
//                        }
//                        else{
//                            Toast.makeText(userlogin.this,"Please check username or password",Toast.LENGTH_LONG ).show();
//                        }

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
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View view) {


        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, Signup.class));
        }

        if(view == buttonSignIn){
//            finish();
            userLogin();


        }
    }


}
