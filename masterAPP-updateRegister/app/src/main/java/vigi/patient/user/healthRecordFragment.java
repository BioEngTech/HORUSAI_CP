package vigi.patient.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vigi.patient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class  healthRecordFragment extends Fragment {


    public healthRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.user_health_record_frag, container, false);

        //




        return view;
    }

}
