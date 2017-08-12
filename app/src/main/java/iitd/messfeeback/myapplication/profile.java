package iitd.messfeeback.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class profile extends Fragment {



    private TextView editTextUsername, editTextHostelname,  lastAttendanceDate, lastAttendanceType;
    private TextView editTextUserid, editTextUseremail, lastFeedbackDateType, lastFeedbackRating;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
        //Fetching data from shared preferences
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.KEY_EMAIL,"Not Available");
        String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        String name = sharedPreferences.getString(Config.KEY_NAME,"Not Available");
        String id = sharedPreferences.getString(Config.KEY_ID,"Not Available");
        String token = sharedPreferences.getString(Config.KEY_TOKEN,"Not Available");
        String feedbackType = sharedPreferences.getString(Config.FEEDBACK_TYPE,"Not Available");
        String feedbackDate = sharedPreferences.getString(Config.FEEDBACK_DATE,"Not Available");
        String feedbackRating = sharedPreferences.getString(Config.FEEDBACK_RATING,"Not Available");
        String attendanceType = sharedPreferences.getString(Config.ATTENDANCE_TYPE,"Not Available");
        String attendanceDate = sharedPreferences.getString(Config.ATTENDANCE_DATE,"Not Available");




        editTextUsername = (TextView) getActivity().findViewById(R.id.editTextUsername);
        editTextHostelname = (TextView) getActivity().findViewById(R.id.editTextHostelname);
        editTextUserid = (TextView) getActivity().findViewById(R.id.editTextUserid);
        editTextUseremail = (TextView) getActivity().findViewById(R.id.editTextUseremail);
        lastFeedbackDateType = (TextView) getActivity().findViewById(R.id.lastFeedbackDateType);
        lastFeedbackRating = (TextView) getActivity().findViewById(R.id.lastFeedbackRating);
        lastAttendanceDate = (TextView) getActivity().findViewById(R.id. lastAttendanceDate);
        lastAttendanceType = (TextView) getActivity().findViewById(R.id. lastAttendanceType);

        Typeface mont_bold = Typeface.createFromAsset(getActivity().getAssets(), "font/Montserrat-Bold.ttf");
        Typeface mont_med =Typeface.createFromAsset(getActivity().getAssets(), "font/Montserrat-Medium.ttf");
        Typeface mont_light =Typeface.createFromAsset(getActivity().getAssets(), "font/Montserrat-Light.ttf");
        editTextUsername.setTypeface(mont_bold);
        editTextHostelname.setTypeface(mont_bold);
        editTextUserid.setTypeface(mont_light);
        editTextUseremail.setTypeface(mont_light);
        editTextUsername.setText(name);
        editTextHostelname.setText(hostel);
        editTextUserid.setText(id);
        editTextUseremail.setText(email);
        lastFeedbackDateType.setText(feedbackDate.substring(0,10) + " | " + feedbackType );
        lastFeedbackRating.setText("Rating: "+ feedbackRating);
        lastAttendanceType.setText(attendanceType.toUpperCase());
        lastAttendanceDate.setText(attendanceDate.substring(0,10));


    }


}
