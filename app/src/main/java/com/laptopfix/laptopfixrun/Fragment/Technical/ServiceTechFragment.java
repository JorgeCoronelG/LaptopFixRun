package com.laptopfix.laptopfixrun.Fragment.Technical;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.laptopfix.laptopfixrun.R;


public class ServiceTechFragment extends Fragment {

    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_service_tech, container, false);

        return view;
    }



}
