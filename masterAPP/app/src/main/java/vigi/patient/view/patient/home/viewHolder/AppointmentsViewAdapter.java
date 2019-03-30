package vigi.patient.view.patient.home.viewHolder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.model.services.Appointment;
import vigi.patient.R;


public class AppointmentsViewAdapter extends FirebaseRecyclerAdapter<Appointment,AppointmentsViewAdapter.ViewHolder>{

    private static String TAG = Activity.class.getName();

    private ArrayList<Appointment> appointments;
    private String patientKey;

    //TODO delete
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefPatient;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AppointmentsViewAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options, String patientKey) {
        super(options);

        this.mDatabase = FirebaseDatabase.getInstance();
        this.mRefPatient = mDatabase.getReference().child("Patient");
        this.patientKey = patientKey;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_appointment_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Appointment model) {

        mRefPatient.child(this.patientKey).child("appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot appointment:dataSnapshot.getChildren()){

                    if (getRef(position).getKey().toString().equals(appointment.getKey().toString())){
                        Appointment appointmentInstance = new Appointment();
                        appointmentInstance.setDate(model.getDate().toString());
                        appointmentInstance.setTreatment(model.getTreatment().toString());
                        appointmentInstance.setCareProviderId(model.getCareProviderId().toString());
                        appointmentInstance.setRating(model.getRating().toString());
                        appointmentInstance.setDuration(model.getDuration().toString());

                        holder.setAppointment(appointmentInstance);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
            treatment = itemView.findViewById(R.id.treatment);
            careProviderName = itemView.findViewById(R.id.cp_name);
            rating = itemView.findViewById(R.id.rating);
            duration = itemView.findViewById(R.id.duration);
            day = itemView.findViewById(R.id.day);
            image = itemView.findViewById(R.id.image);

        }

        public void setAppointment(Appointment appointment) {
            time.setText(String.valueOf(appointment.getDate()).split(" ")[1]);
            treatment.setText(appointment.getTreatment());
            careProviderName.setText(appointment.getCareProviderId());
            rating.setText(appointment.getRating());
            duration.setText(appointment.getDuration());
            day.setText(String.valueOf(appointment.getDate()).split(" ")[0].substring(0,5));
        }

    }
}
