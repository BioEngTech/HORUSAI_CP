package vigi.care_provider.view.authentication.home;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import vigi.care_provider.R;

public class HomeAuthSliderAdapter extends PagerAdapter{
    private Activity context;
    private LayoutInflater inflater;

    public HomeAuthSliderAdapter(Activity context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3 ; // number of pages of the sliderAdapter
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        switch (position){

            case 0:
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.initiation_home_one,container,false);

                container.addView(view);
                break;
            case 1:
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.initiation_home_two,container, false);

                container.addView(view);
                break;
            case 2:
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.initiation_home_three,container, false);

                container.addView(view);
                break;
            default:
                view = new View(context.getBaseContext());
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

}


