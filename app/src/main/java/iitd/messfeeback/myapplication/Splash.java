package iitd.messfeeback.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private TextView textView2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        textView2 = (TextView) findViewById(R.id.textView2);
        Typeface mont_bold = Typeface.createFromAsset(getAssets(), "font/Montserrat-Bold.ttf");
        Typeface mont_med =Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        Typeface mont_light =Typeface.createFromAsset(getAssets(), "font/Montserrat-Light.ttf");
        textView2.setTypeface(mont_bold);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            public void run() {

//                startActivity(new Intent(Splash.this, userlogin.class));

                Intent launchNextActivity;
                launchNextActivity = new Intent(Splash.this, userlogin.class);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(launchNextActivity);
                finish();

            }
        },  SPLASH_DISPLAY_LENGTH);
    }



}