package iitd.messfeeback.myapplication;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static iitd.messfeeback.myapplication.attendance.giveAttendance;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String SUBMIT_URL = "http://10.17.5.66:8080/api/attendance/";

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
    Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new profile()).commit();
        }





//         Intent intent = getIntent();
//         email = intent.getExtras().getString("email");
//         token = intent.getExtras().getString("token");
//         hostel = intent.getExtras().getString("hostel");
//         id = intent.getExtras().getString("id");
//         name = intent.getExtras().getString("name");

        //Fetching data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.KEY_EMAIL,"Not Available");
        System.out.println("Deepak Korku" + email);
        String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        String name = sharedPreferences.getString(Config.KEY_NAME,"Not Available");
        String id = sharedPreferences.getString(Config.KEY_ID,"Not Available");
        String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");















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



        if((st1 >=1 && st1<=3 && t2.equals("p")) || (st1 ==11 && t2.equals("a")) || (st1 ==12 && t2.equals("p"))){
            String mealType = "lunch";
            messType1 = mealType;
            Mess = Boolean.TRUE;



        }

        else if((st1 >=6 && st1<=10 && t2.equals("a"))){
            String mealType = "breakfast";
            messType1 = mealType;
            Mess = Boolean.TRUE;


        }

        else if((st1 >=6 && st1<=10 && t2.equals("p"))) {

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



    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                System.out.println("connected to wifi");
//                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
//                System.out.println("Please connect to wifi");
                Toast.makeText(context, "Switch to IITD/edurom wifi from Mobile Data", Toast.LENGTH_SHORT).show();
            }
        } else {
            // not connected to the internet
            Toast.makeText(context, "Please connect to IITD/edurom wifi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                        Toast.makeText(MainActivity.this,"You may have already updated the attendance",Toast.LENGTH_LONG ).show();
                        System.out.println("Deepak Korku no meal");
                    }
                }){
//            @Override
//            public Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String,String>();
//                params.put(HOSTEL,hostel);
//                params.put(ATTENDANCE,"True");
//                params.put(MEAL_TYPE, messType1);
//                return params;

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
//                if(result.getContents().equals(hostel) && Mess && !giveAttendance){
                if(result.getContents().equals(hostel) && Mess){
                    System.out.println("deepak feedback");
                    startActivity(new Intent(this, submitRating.class));
                }

                else if(result.getContents().equals(hostel) && Mess && giveAttendance){
                    System.out.println("deepak give attendance");
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
}
