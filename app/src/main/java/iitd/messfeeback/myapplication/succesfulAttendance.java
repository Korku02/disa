package iitd.messfeeback.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class succesfulAttendance extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private TextView sucessText;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_succesful_attendance);

        sucessText = (TextView) findViewById(R.id.successText);
        Typeface mont_bold = Typeface.createFromAsset(getAssets(), "font/Montserrat-Bold.ttf");
        Typeface mont_med =Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        Typeface mont_light =Typeface.createFromAsset(getAssets(), "font/Montserrat-Light.ttf");
        sucessText.setTypeface(mont_bold);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            public void run() {


                startActivity(new Intent(succesfulAttendance.this, userlogin.class));
                finish();



            }
        },  SPLASH_DISPLAY_LENGTH);
    }

}