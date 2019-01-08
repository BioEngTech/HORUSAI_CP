package vigi.patient.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import vigi.patient.R;

public class homeFragment extends Fragment implements View.OnClickListener {

    //general layout vars
    private Toolbar myToolbar;
    private Button askForHelp;
    private ImageView imageProfile;

    //recycler view vars
    private ArrayList<String> careProviderNames = new ArrayList<>();
    private ArrayList<Integer> days = new ArrayList<>();
    private ArrayList<String> months = new ArrayList<>();
    private ArrayList<Integer> times = new ArrayList<>();
    private ArrayList<String> procedures = new ArrayList<>();

    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test, container, false);

        myToolbar = view.findViewById(R.id.userHomeFragment_Toolbar);

        myToolbar.setNavigationIcon(R.drawable.icon_drawer);

        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        getAppointmentsForTheWeek();

        // initiate recycler view
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerViewAdapter adapter = new recyclerViewAdapter(getActivity(),careProviderNames,days,months,times,procedures);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_items, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_message:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAppointmentsForTheWeek(){

        careProviderNames.add("John");
        days.add(15);
        months.add("Nov");
        times.add(16);
        procedures.add("15");

        careProviderNames.add("Michael");
        days.add(15);
        months.add("Nov");
        times.add(12);
        procedures.add("15");

    }


    @Override
    public void onClick(View v) {


        if (v.getId() == askForHelp.getId()){ // Open skills layout


        }

    }
}
