package iitd.messfeeback.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class intermediate extends AppCompatActivity implements View.OnClickListener {

    public static final String EMAIL = "email";
    public static final String FORGOT_PASS = "http://10.17.5.66:8080/forgotpasswordemail/";

    public static final String USER_EMAIL="user_email";

    private Button buttonNext;
    private EditText editTextForgotEmail;

    private ProgressDialog progressDialog;

    public static String forgotemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        editTextForgotEmail = (EditText) findViewById(R.id.editTextForgotEmail);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    public void forgotpassword(){
        forgotemail = editTextForgotEmail.getText().toString().trim();

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest forgotpass = new StringRequest(Request.Method.POST, FORGOT_PASS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jObj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), "OTP successfully sent to your email", Toast.LENGTH_LONG).show();

                            // Launch main activity
                            Intent intent = new Intent( intermediate.this, Forgotpassword.class);

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
                            Toast.makeText(intermediate.this,errorMessage,Toast.LENGTH_LONG ).show();

                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(intermediate.this,"Connection Error",Toast.LENGTH_LONG ).show();

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

    @Override
    public void onClick(View view) {
        if(view == buttonNext){
            forgotpassword();
        }
    }
}
