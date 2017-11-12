package iitd.messfeeback.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONObject;

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
    private EditText editTextEmail, editTextPassword, editTextRePassword, editTextUsername, editTextEntryNo;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private Spinner spinnerHostel;
    private TextView textViewLogin, signupSubHeading, signupHeading;
    private Button wifi_button;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextRePassword);
        editTextEntryNo = (EditText) findViewById(R.id.editTextEntryNo);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        spinnerHostel = (Spinner) findViewById(R.id.spinnerHostel);
        signupSubHeading = (TextView) findViewById(R.id.signupSubHeading);
        signupHeading = (TextView) findViewById(R.id.signupHeading);

        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        Typeface mont_bold = Typeface.createFromAsset(getAssets(), "font/Montserrat-Bold.ttf");
        Typeface mont_med =Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        Typeface mont_light =Typeface.createFromAsset(getAssets(), "font/Montserrat-Light.ttf");
        textViewLogin.setTypeface(mont_med);
        editTextEntryNo.setTypeface(mont_med);
        editTextUsername.setTypeface(mont_med);
        editTextPassword.setTypeface(mont_med);
        editTextRePassword.setTypeface(mont_med);
        editTextEmail.setTypeface(mont_med);
        signupSubHeading.setTypeface(mont_med);
        buttonSignup.setTypeface(mont_med);
        signupHeading.setTypeface(mont_bold);


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

    private boolean validate() {
        boolean temp=true;
        String pass= editTextPassword.getText().toString();
        String cpass= editTextRePassword.getText().toString();
        if(!pass.equals(cpass)){
            Toast.makeText(this,"Password Not matching",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
    }

    private void registerUser() {

        final String user_name = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String user_id = editTextEntryNo.getText().toString().trim();
        final String user_hostel = spinnerHostel.getSelectedItem().toString().trim();

        if(validate()) {

            progressDialog.setMessage("Please Wait....");
            progressDialog.show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                        Toast.makeText(Signup.this,response,Toast.LENGTH_LONG).show();
                            Toast.makeText(Signup.this, "successfully registered", Toast.LENGTH_LONG).show();

                            if (response != null) {
                                startActivity(new Intent(getApplicationContext(), userlogin.class));
                                finish();
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
                                Toast.makeText(Signup.this, errorMessage, Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(Signup.this, "Connection Error", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }

//
//                        Toast.makeText(Signup.this,"Please connect to wifi",Toast.LENGTH_LONG).show();

                        }

                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(USER_NAME, user_name);
                    params.put(PASSWORD, password);
                    params.put(EMAIL, email);
                    params.put(ID, user_id);
                    params.put(HOSTEL, user_hostel);

                    return params;

                }


            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSignup){
            registerUser();

        }
        else if(v == textViewLogin){
            startActivity(new Intent(this, userlogin.class));
            finish();
        }
    }
}
