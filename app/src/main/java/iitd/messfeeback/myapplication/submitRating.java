package iitd.messfeeback.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class submitRating extends AppCompatActivity implements View.OnClickListener {


    public static final String SUBMIT_URL = "http://10.17.5.66:8080/api/meal/";

    public static final String HOSTEL = "hostel";
    public static final  String RATING = "rating";
    public static final String MEAL_TYPE = "meal_type";

    private Button buttonSubmit;
    private RatingBar ratingBar;
    private String datetimeString , timeString , dateString;
    private TextView messType;
    public static String messType1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        messType = (TextView) findViewById(R.id.messType);

        buttonSubmit.setOnClickListener(this);

    }



    protected void saveFeedback(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        final String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");

        final String rating =String.valueOf(ratingBar.getRating());

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


        System.out.println("Deepak Korku" + rating);


        if((st1 >=1 && st1<=3 && t2.equals("p")) || (st1 ==11 && t2.equals("a")) || (st1 ==12 && t2.equals("p"))){
            String mealType = "lunch";
            messType1 = mealType;
            messType.setText(mealType);


        }

        else if((st1 >=6 && st1<=10 && t2.equals("a"))){
            String mealType = "breakfast";
            messType1 = mealType;
            messType.setText(mealType);

        }

        else if((st1 >=6 && st1<=10 && t2.equals("p"))) {

            String mealType = "dinner";
            messType1 = mealType;
            messType.setText(mealType);


        }

        else{
            String mealType = "No meal is going on";
            messType1 = mealType;
            messType.setText(mealType);
        }

        System.out.println(messType1);
        //http request for submitting feedback

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject responseObj = new JSONObject(response);
                            String feedbackDate = responseObj.getString("created");
                            String feedbackType = responseObj.getString("meal_type");
                            String feedbackRating = responseObj.getString("rating");
                            System.out.println("korku" + responseObj);
                            Toast.makeText(submitRating.this, "Submitted Succesfully",Toast.LENGTH_LONG ).show();
                            startActivity(new Intent(getApplicationContext(), successfulFeedback.class));
                            finish();


                            SharedPreferences sharedPreferences =submitRating.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor

                            editor.putString(Config.FEEDBACK_DATE, feedbackDate);
                            editor.putString(Config.FEEDBACK_TYPE, feedbackType);
                            editor.putString(Config.FEEDBACK_RATING, feedbackRating);



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

//                        JSONObject errorObj = new JSONObject(error);
                        System.out.println(error);
                        System.out.println("error feedback");
                        Toast.makeText(submitRating.this,"Feedback is already completed for corresponding field.",Toast.LENGTH_LONG ).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        System.out.println("Deepak Korku no meal");
                        System.out.println(token);
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put(HOSTEL,hostel);
                params.put(RATING,rating);
                params.put(MEAL_TYPE, messType1);
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer"+" " +token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);





    }

    @Override
    public void onClick(View v) {

        if(v == buttonSubmit){
            saveFeedback();


        }
    }
}
