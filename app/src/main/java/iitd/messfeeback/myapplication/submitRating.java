package iitd.messfeeback.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static iitd.messfeeback.myapplication.R.layout.menuwise_ratingdialog;




public class submitRating extends AppCompatActivity implements View.OnClickListener {




    public static final String HOSTEL = "hostel";
    public static final  String RATING = "rating";
    public static final String MEAL_TYPE = "meal_type";
    public static final String COMMENT = "comment";


    private Button buttonSubmit;
    private RatingBar ratingBar;
    private RatingBar ratingBar2;
    private String datetimeString , timeString , dateString;
    private TextView messType;
    private EditText Comment;
    public String overallcomment;
    private Button cancel_dialog;
    private Button submit_menu_wise;
//    public ListView menu_list_item;
//    private TextView testtext;
    public static String messType1;

    public float[] saveRating;
    public String[] savemealId;
    public Dialog menuDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflater = LayoutInflater.from(this);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        messType = (TextView) findViewById(R.id.messType);
        Comment = (EditText) findViewById(R.id.comment);
        buttonSubmit.setOnClickListener(this);



        final Dialog dialog = new Dialog(submitRating.this);
        dialog.setContentView(menuwise_ratingdialog);
        cancel_dialog = (Button) dialog.findViewById(R.id.cancel_dialog);
        submit_menu_wise = (Button) dialog.findViewById(R.id.submit_menu_wise);

        menuDialog = dialog;

        cancel_dialog.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // I want the dialog to close at this point
                saveFeedback();
                dialog.dismiss();
            }
        });

        submit_menu_wise.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // I want the dialog to close at this point
                System.out.println(saveRating[0] + " this is meal id");
                saveFeedback();
                submitMenuwise();
                dialog.dismiss();

            }
        });















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


        if((st1 >=1 && st1<=3 && (t2.equals("p") ||t2.equals("P") )) || (st1 ==11 && (t2.equals("a") || t2.equals("A"))  )  || (st1 ==12 && (t2.equals("p") || t2.equals("P")) )){
            String mealType = "lunch";
            messType1 = mealType;
            messType.setText(mealType);
        }

        else if((st1 >=6 && st1<=10 && (t2.equals("a") || t2.equals("A") )  )){
            String mealType = "breakfast";
            messType1 = mealType;
            messType.setText(mealType);
        }

        else if((st1 >=6 && st1<=10 && (t2.equals("p") ||t2.equals("P") )  )) {

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject responseObj = new JSONObject(response);
                            System.out.println(responseObj.toString()+"korku");
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
                            System.out.println(e);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        JSONObject errorObj = new JSONObject(error);

                        try {
                            String errorString = new String(error.networkResponse.data);
                            JSONObject errorObj = new JSONObject(errorString);
                            String errorMessage = errorObj.getString("error");
                            Toast.makeText(submitRating.this,errorMessage,Toast.LENGTH_LONG ).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(submitRating.this,"Conection error",Toast.LENGTH_LONG ).show();

                        }

                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put(HOSTEL,hostel);
                params.put(RATING,rating);
                params.put(MEAL_TYPE, messType1);
                params.put(COMMENT, overallcomment);
                System.out.println(params + " basic feedback");
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

        System.out.println(stringRequest);


    }

    protected void getMenuDaily(final Dialog menudialog){
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        final String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.GET_MENU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            System.out.println(jsonArray);
                            System.out.println("this is daily menu response");

//                            String[] web1 = {
//                                    "Google Plus",
//                                    "Twitter",
//                                    "Windows",
//                                    "Bing",
//                                    "Itunes",
//                                    "Wordpress",
//                                    "Drupal"
//                            } ;






                            String[] web1 = new String[jsonArray.length()];
                            String[] mealid = new String[jsonArray.length()];
                            float[] rating = new float[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                web1[i] = obj.getString("item");
                                mealid[i] = obj.getString("meal_id");
                                rating[i] = 0;
                                System.out.println(obj.getString("item"));
                            }
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(submitRating.this, android.R.layout.simple_list_item_1, mealName);
//                            listView.setAdapter(arrayAdapter);
//                            System.out.println(mealName.length);





                            ListView menu_list_item = (ListView) menudialog.findViewById(R.id.menu_list_item);

                            CustomList adapter = new
                                    CustomList(submitRating.this, web1, rating);
                            menu_list_item.setAdapter(adapter);

                            System.out.println(adapter);

                            menudialog.show();

                            saveRating = rating;
                            savemealId = mealid;



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

                        try {
                            String errorString = new String(error.networkResponse.data);
                            System.out.println(errorString);
                            System.out.println("this is daily menu error");
                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(submitRating.this,"Conection error",Toast.LENGTH_LONG ).show();

                        }

                    }
                }){


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

    protected void submitMenuwise(){

        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        final String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");



        Map<String, String> queryMap = new HashMap<String,String>();

        queryMap.put("hostel", "Nilgiri");
        queryMap.put("meal_type", "lunch");


        Map<String, List<Map<String, String>>> idratingMap = new HashMap<String,List<Map<String, String>>>();


        List<Map<String, String>> listOfMaps = new ArrayList<Map<String, String>>();



        for(int j =0; j<savemealId.length; j++){
            Map<String, String> temp = new HashMap<String,String>();
            temp.put("id",savemealId[j]);
            temp.put("rating",Float.toString(saveRating[j]));
            listOfMaps.add(temp);
        }



        idratingMap.put("menuwise_rating", listOfMaps);


        Map finalMap = new HashMap();
        finalMap.putAll(queryMap);
        finalMap.putAll(idratingMap);
        System.out.println(finalMap);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.GET_MENU_URL,
                new JSONObject(finalMap),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("success");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error");
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer"+" " +token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);

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
        if(v == buttonSubmit){
//            saveFeedback();
            getMenuDaily(menuDialog);
            overallcomment = Comment.getText().toString().trim();
        }


    }
}
