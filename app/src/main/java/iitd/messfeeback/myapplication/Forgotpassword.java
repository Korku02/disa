package iitd.messfeeback.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static iitd.messfeeback.myapplication.intermediate.FORGOT_PASS;
import static iitd.messfeeback.myapplication.intermediate.USER_EMAIL;
import static iitd.messfeeback.myapplication.intermediate.forgotemail;

public class Forgotpassword extends AppCompatActivity implements View.OnClickListener {

    public static final String RESET_PASS = "http://10.17.5.66:8080/resetpassword/";

    public static final String NEW_PASSWORD="new_password";
    public static final String FORGET_OTP="forget_otp";

    private Button buttonUpdatepassword;
    private EditText editTextNewPassword, editTextNewRePassword, editTextOTP;
    private TextView nootptext;

    private ProgressDialog progressDialog;

    private String password;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        editTextNewRePassword = (EditText) findViewById(R.id.editTextNewRePassword);
        editTextOTP = (EditText) findViewById(R.id.editTextOTP);
        buttonUpdatepassword = (Button) findViewById(R.id.buttonUpdatepassword);
        nootptext  = (TextView) findViewById(R.id.nootptext);

        nootptext.setOnClickListener(this);
        buttonUpdatepassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

    }

    private boolean validate() {
        boolean temp=true;
        password = editTextNewPassword.getText().toString().trim();
        otp = editTextOTP.getText().toString().trim();
        String pass= editTextNewPassword.getText().toString();
        String cpass= editTextNewRePassword.getText().toString();
        if(!pass.equals(cpass) || otp.equals("") || pass.equals("") || cpass.equals("")){
            Toast.makeText(this,"Please check your fields",Toast.LENGTH_SHORT).show();
            temp=false;
        }
        return temp;
    }

    @Override
    public void onClick(View view) {
        if(view == nootptext){
            forgotpassword();
        }
        if(view == buttonUpdatepassword){
            if(validate()) {
                forgotpass();
            }
        }
    }

    public void forgotpassword(){

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest forgotpass = new StringRequest(Request.Method.POST, FORGOT_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jObj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), "OTP successfully resent to your email", Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        try {
                            String errorString = new String(error.networkResponse.data);
                            JSONObject errorObj = new JSONObject(errorString);
                            String errorMessage = errorObj.getString("error");
                            Toast.makeText(Forgotpassword.this,errorMessage,Toast.LENGTH_LONG ).show();

                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(Forgotpassword.this,"Connection Error",Toast.LENGTH_LONG ).show();

                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(USER_EMAIL,forgotemail);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(forgotpass);
    }

    public void forgotpass(){
        password = editTextNewPassword.getText().toString().trim();
        otp = editTextOTP.getText().toString().trim();

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest forgotpass = new StringRequest(Request.Method.POST, RESET_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jObj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_LONG).show();

                            // Launch main activity
                            Intent intent = new Intent( Forgotpassword.this, userlogin.class);

                            startActivity(intent);
                            finish();


                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        try {
                            String errorString = new String(error.networkResponse.data);
                            JSONObject errorObj = new JSONObject(errorString);
                            String errorMessage = errorObj.getString("error");
                            Toast.makeText(Forgotpassword.this,errorMessage,Toast.LENGTH_LONG ).show();

                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(Forgotpassword.this,"Connection Error",Toast.LENGTH_LONG ).show();

                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(NEW_PASSWORD,password);
                map.put(FORGET_OTP,otp);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(forgotpass);
    }
}
