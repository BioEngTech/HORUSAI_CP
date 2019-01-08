package vigi.patient.user;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applikeysolutions.cosmocalendar.view.CalendarView;

import vigi.patient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class accountFragment extends Fragment {


    public accountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_timeline, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendar_view);

        calendarView.setCalendarBackgroundColor(getResources().getColor(R.color.colorBlueLight));

        return view;

    }

}
