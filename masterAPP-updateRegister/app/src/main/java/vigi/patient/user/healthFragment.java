package vigi.patient.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vigi.patient.R;

public class healthFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public healthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_health, container, false);

        mViewPager = view.findViewById(R.id.userHealthFragment_ViewPager);
        setupViewPager(mViewPager);

        mTabLayout = view.findViewById(R.id.userHealthFragment_TabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }


    private void setupViewPager(ViewPager viewPager){

        sectionsPageAdapter adapter = new sectionsPageAdapter(getFragmentManager());
        adapter.addFragment(new medicalFileFragment(),getString(R.string.medical_file_text));
        adapter.addFragment(new medicalFileFragment(),getString(R.string.health_record_text));
        viewPager.setAdapter(adapter);

    }

}
