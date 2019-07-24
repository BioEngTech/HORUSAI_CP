package vigi.patient.view.patient.careProvider.booking.viewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import vigi.patient.R;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

public class BookingAdapter extends EmptyRecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private ArrayList<String> days;
    private HashMap<String, ArrayList<String>> availableHours;
    private Context context;


    public BookingAdapter(Context context, ArrayList<String> days, HashMap<String, ArrayList<String>> availableHours) {
        this.context = context;
        this.days = days;
        this.availableHours = availableHours;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cprovider_booking, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String keyName = days.get(i);
        viewHolder.dayOfWeek.setText(keyName);

        HoursAdapter booksAdapter = new HoursAdapter(context, availableHours.get(keyName));
        viewHolder.hoursAvailable.setAdapter(booksAdapter);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayOfWeek;
        GridView hoursAvailable;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            dayOfWeek = itemView.findViewById(R.id.day_of_week);
            hoursAvailable = itemView.findViewById(R.id.hours_available);

        }
    }
}
