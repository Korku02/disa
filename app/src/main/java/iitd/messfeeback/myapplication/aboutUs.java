package iitd.messfeeback.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class aboutUs extends Fragment implements View.OnClickListener {

    private TextView aboutUs;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_about_us, container, false);




    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        aboutUs = (TextView) getActivity().findViewById(R.id.aboutUs);

        getActivity().setTitle("About Us");

        Typeface mont_light =Typeface.createFromAsset(getActivity().getAssets(), "font/Montserrat-Light.ttf");

        aboutUs.setTypeface(mont_light);




    }
    public void onClick(View v) {


    }

}
