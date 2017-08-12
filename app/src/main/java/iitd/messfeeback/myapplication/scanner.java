package iitd.messfeeback.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import static iitd.messfeeback.myapplication.attendance.giveAttendance;
import static java.lang.Boolean.FALSE;

public class scanner extends Fragment implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView editTextUsername;

    //qr code scanner object
    private IntentIntegrator qrScan;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_scanner, container, false);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Give Feedback");
        //Fetching data from shared preferences
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String name = sharedPreferences.getString(Config.KEY_NAME,"Not Available");
        String hostel = sharedPreferences.getString(Config.KEY_HOSTEL,"Not Available");
        //intizialing view objects
        editTextUsername = (TextView) getActivity().findViewById(R.id.editTextUsername);
        buttonScan = (Button) getActivity().findViewById(R.id.buttonScan);


        //setText for dynamic strings
        editTextUsername.setText(name);
        //intializing scan object
        qrScan = new IntentIntegrator(getActivity());
        //attaching onclick listener
        buttonScan.setOnClickListener(this);

    }






    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        if(view == buttonScan){
            giveAttendance = FALSE;
            qrScan.initiateScan();
        }
    }
}

