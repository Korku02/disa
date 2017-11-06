package iitd.messfeeback.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static iitd.messfeeback.myapplication.attendance.giveAttendance;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    public static final String SUBMIT_URL = "http://10.17.5.66:8080/api/attendance/";
    public static final String UPDATE_URL = "http://10.17.5.66:8080/api/getlastdata/";


    public static final String HOSTEL = "hostel";
    public static final  String ATTENDANCE = "attendance";
    public static final String MEAL_TYPE = "meal_type";
    public static String email,token,hostel,id,name,submitAttendance;
    public static String getSubmitAttendance = "True";
    private Boolean attendance;
    private TextView editTextUsername;
    private TextView editTextUseremail;
    private Fragment currentFragment;
    private String datetimeString , timeString , dateString;
    private TextView messType;
    public static String messType1;
    public static Boolean Mess;
    public Button wifi_button;
    private Boolean exit = false;
    Context context = this;
    private ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new profile()).commit();
        }

        checkWifi();


        //Fetching data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.KEY_EMAIL,"Not Available");
        System.out.println("Deepak Korku" + email);
        String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        String name = sharedPreferences.getString(Config.KEY_NAME,"Not Available");
        String lastFeedbackDateType = sharedPreferences.getString(Config.FEEDBACK_DATE,"Not Available");
        String lastAttendanceDate = sharedPreferences.getString(Config.ATTENDANCE_DATE,"Not Available");

//        if(lastFeedbackDateType.equals("Not Available" ) || lastAttendanceDate.equals("Not Available" )){
            updateInfo();
//        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView= navigationView.getHeaderView(0);
        editTextUsername = (TextView) headerView.findViewById(R.id.editTextUsername);
        editTextUseremail = (TextView) headerView.findViewById(R.id.editTextUseremail);
        progressDialog = new ProgressDialog(this);



        editTextUsername.setText(name);
        editTextUseremail.setText(email);

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

        System.out.println(t1 +" "+ t2 + " "+st1);


        if((st1 >=1 && st1<=3 && (t2.equals("p") ||t2.equals("P") )) || (st1 ==11 && (t2.equals("a") || t2.equals("A"))  )  || (st1 ==12 && (t2.equals("p") || t2.equals("P")) )){
            String mealType = "lunch";
            messType1 = mealType;
            Mess = Boolean.TRUE;



        }

        else if((st1 >=6 && st1<=10 && (t2.equals("a") || t2.equals("A") )  )){
            String mealType = "breakfast";
            messType1 = mealType;
            Mess = Boolean.TRUE;


        }

        else if((st1 >=6 && st1<=10 && (t2.equals("p") ||t2.equals("P") )  )) {

            String mealType = "dinner";
            messType1 = mealType;
            Mess = Boolean.TRUE;



        }

        else{
            String mealType = "No meal is going on";
            messType1 = mealType;
            Mess = Boolean.FALSE;

        }


    }


    public void updateInfo(){

//        progressDialog.setMessage("Please wait....");
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jObj = new JSONObject(response);
//                            System.out.println(jObj.get("last_attendance"));
                            JSONArray attendance_data = jObj.getJSONArray("last_attendance");
                            JSONArray feedback_data = jObj.getJSONArray("last_feedback");

                            String feedbackDate = feedback_data.getJSONObject(0).getString("created");
                            String feedbackType = feedback_data.getJSONObject(0).getString("meal_type");
                            String feedbackRating = feedback_data.getJSONObject(0).getString("rating");


                            String attendanceDate = attendance_data.getJSONObject(0).getString("created");
                            String attendanceType = attendance_data.getJSONObject(0).getString("meal_type");
//


//                            System.out.println("Deepak Korku"+response);


                            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor

                            editor.putString(Config.FEEDBACK_DATE, feedbackDate);
                            editor.putString(Config.FEEDBACK_TYPE, feedbackType);
                            editor.putString(Config.FEEDBACK_RATING, feedbackRating);
                            editor.putString(Config.ATTENDANCE_DATE, attendanceDate);
                            editor.putString(Config.ATTENDANCE_TYPE, attendanceType);

                            //Saving values to editor
                            editor.apply();

                            Toast.makeText(MainActivity.this,"Data synced succesfully",Toast.LENGTH_LONG ).show();



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
//                            System.out.println(errorMessage);
                            Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_LONG ).show();
//                            progressDialog.dismiss();
                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_LONG ).show();

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

            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);



            String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");

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
    public void checkWifi(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                System.out.println("connected to wifi");
//                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                //return false;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
//                System.out.println("Please connect to wifi");
                // custom dialog
                //return true;
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
            //return true;
            // not connected to the internet
            Toast.makeText(context, "Please connect to IITD/edurom wifi", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        checkWifi();
//        updateInfo();
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        finish();

//        if (exit) {
//            finish(); // finish activity
//        } else {
//            Toast.makeText(this, "Press Back again to Exit.",
//                    Toast.LENGTH_SHORT).show();
//            exit = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    exit = false;
//                }
//            }, 1000);
//
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_camera:
                fragment = new profile();
                break;
            case R.id.nav_slideshow:
                fragment = new scanner();
                break;
            case R.id.nav_manage:
                fragment = new attendance();
                break;
            default:
                fragment = new profile();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
    }


    private void submitAttendance(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject responseObj = new JSONObject(response);
                            System.out.println("korku" + responseObj);
                            String attendanceDate = responseObj.getString("created");
                            String attendanceType = responseObj.getString("meal_type");

                            startActivity(new Intent(getApplicationContext(), succesfulAttendance.class));
                            finish();

                            SharedPreferences sharedPreferences =MainActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor

                            editor.putString(Config.ATTENDANCE_DATE, attendanceDate);
                            editor.putString(Config.ATTENDANCE_TYPE, attendanceType);


                            //Saving values to editor
                            editor.apply();





                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();

                        }

                    }
                },
//                    public void onResponse(String response) {
//
//                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
//
//                        if(response !=null ){
//                            startActivity(new Intent(getApplicationContext(), succesfulAttendance.class));
//                            finish();
//                        }
//                    }
//                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String errorString = new String(error.networkResponse.data);
                            JSONObject errorObj = new JSONObject(errorString);
                            String errorMessage = errorObj.getString("error");
                            System.out.println(errorMessage);
                            Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_LONG ).show();
                        }
                        catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            System.out.println(e);
                            Toast.makeText(MainActivity.this,"Connection error",Toast.LENGTH_LONG ).show();

                        }
//
//                        System.out.println("Deepak Korku no meal");
                    }
                }){


            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);


            String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
            String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");

            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put(HOSTEL,hostel);
                params.put(ATTENDANCE,"True");
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
    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it

            if (result.getContents() == null) {

                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
                System.out.println("deepak feedback");
                System.out.println(Mess);
                System.out.println(giveAttendance);
                if(result.getContents().equals(hostel) && Mess && !giveAttendance){
//                if(result.getContents().equals(hostel) && Mess){
                    System.out.println("giveFeedback");
                    System.out.println(giveAttendance);
                    startActivity(new Intent(this, submitRating.class));
                }

                else if(result.getContents().equals(hostel) && Mess && giveAttendance){
                    System.out.println("giveAttendane1");
                    System.out.println(giveAttendance);
                    submitAttendance();
                }
                else{
                    Toast.makeText(this, "Either you are not resident of"+ " "+ result.getContents() +" or No meal right now", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    @Override
//    public void onClick(View v) {
//
//        if(v == wifi_button){
//            System.out.println("wifi_button_clicked");
////            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//        }
//    }
}
