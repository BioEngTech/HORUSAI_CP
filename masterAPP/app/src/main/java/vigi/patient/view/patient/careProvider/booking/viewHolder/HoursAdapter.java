package vigi.patient.view.patient.careProvider.booking.viewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vigi.patient.R;


public class HoursAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> hours;
    private ArrayList<Boolean> states = new ArrayList<>();

    public HoursAdapter(Context context, ArrayList<String> hours) {
        this.context = context;
        this.hours = hours;
    }

    @Override
    public int getCount() {
        return hours.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.adapter_cprovider_booking_grid, parent,false);
        }

        TextView hour = convertView.findViewById(R.id.hour);
        hour.setText(String.format("%s",hours.get(position)));
        states.add(position,false);
        hour.setOnClickListener(view -> {
            if (!states.get(position)){
                hour.setBackgroundResource(R.drawable.utils_rectangle_rounded_200dp_full_blue_medium);
                hour.setTextColor(context.getResources().getColor(R.color.colorWhite));
                states.set(position, true);
            }else{
                hour.setBackgroundResource(R.drawable.utils_rectangle_rounded_200dp_full_white);
                hour.setTextColor(context.getResources().getColor(R.color.colorGrayStrong));
                states.set(position, false);
            }
        });

        return convertView;
    }

}