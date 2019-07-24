package vigi.patient.view.patient.home.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.model.services.Appointment;
import vigi.patient.R;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;


public class AppointmentsAdapter extends EmptyRecyclerView.Adapter<AppointmentsAdapter.ViewHolder>{

    private String TAG = getClass().getName();
    private ArrayList<Appointment> appointments;

    public AppointmentsAdapter(ArrayList<Appointment> appointmentsList) {
        appointments = appointmentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_patient_appointments,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.time.setText(String.valueOf(appointments.get(i).getDate()));
        viewHolder.treatment.setText(appointments.get(i).getReport());
        viewHolder.careProviderName.setText(appointments.get(i).getCareProviderId().toString());
        viewHolder.rating.setText(appointments.get(i).getRating().toString());
        viewHolder.duration.setText(appointments.get(i).getMinutesOfDuration().toString());
        viewHolder.day.setText(appointments.get(i).getDate().toString());
        //TODO: CareProvider object
        //viewHolder.image.setImageDrawable(appointments.get(i).getCareProviderImage());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView treatment;
        TextView careProviderName;
        TextView rating;
        TextView duration;
        TextView day;
        CircleImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            treatment = itemView.findViewById(R.id.patient_name);
            careProviderName = itemView.findViewById(R.id.cp_name);
            rating = itemView.findViewById(R.id.rating);
            duration = itemView.findViewById(R.id.duration);
            day = itemView.findViewById(R.id.day);
            image = itemView.findViewById(R.id.image);

        }
    }
}
