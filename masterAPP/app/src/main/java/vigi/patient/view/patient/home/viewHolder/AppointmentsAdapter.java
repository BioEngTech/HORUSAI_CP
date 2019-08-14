package vigi.patient.view.patient.home.viewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Appointment;
import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.view.utils.dialog.VigiAppointmentDetailsDialog;
import vigi.patient.view.utils.dialog.VigiTreatmentDetailsDialog;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;


public class AppointmentsAdapter extends EmptyRecyclerView.Adapter<AppointmentsAdapter.ViewHolder>{

    private String TAG = getClass().getName();
    private List<String> appointmentsIds;
    private List<Appointment> appointmentsList;
    private List<CareProvider> careProvidersList;
    private List<Treatment> treatmentsList;
    private Context context;
    private CareProvider careProvider;
    private Treatment treatment;

    public AppointmentsAdapter(Context context, List<String> appointmentsIds, List<Appointment> appointmentsList, List<CareProvider> careProvidersList, List<Treatment> treatmentsList) {
        this.context = context;
        this.appointmentsList = appointmentsList;
        this.appointmentsIds = appointmentsIds;
        this.careProvidersList = careProvidersList;
        this.treatmentsList = treatmentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_patient_appointments,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        careProvider = careProvidersList.stream()
                .filter(careProvider -> careProvider.getId().equals(appointmentsList.get(i).getCareProviderId())).findFirst().orElse(null);
        treatment = treatmentsList.stream()
                .filter(treatment -> treatment.getId().equals(appointmentsList.get(i).getTreatmentId())).findFirst().orElse(null);

        if (appointmentsList.get(i).getStatus().equals("active")) {
            viewHolder.time.setText(appointmentsList.get(i).getDate().split(" ")[1]);

        }
        viewHolder.treatment.setText(treatment.getName());
        viewHolder.careProviderName.setText(careProvider.getName());
        viewHolder.rating.setText(String.valueOf(careProvider.getRating()));
        viewHolder.duration.setText(String.valueOf(appointmentsList.get(i).getMinutesOfDuration()));
        viewHolder.day.setText(appointmentsList.get(i).getDate().split(" ")[0]);
        Picasso.get().load(careProvider.getImage().toString()).into(viewHolder.image);

        if (!appointmentsList.get(i).getStatus().equals("active")) {
            viewHolder.treatment.setTextColor(R.color.colorGrayLight);
            viewHolder.careProviderName.setTextColor(R.color.colorGrayLight);
            viewHolder.rating.setTextColor(R.color.colorGrayLight);
            viewHolder.duration.setTextColor(R.color.colorGrayLight);
            viewHolder.day.setTextColor(R.color.colorGrayLight);

        }
        viewHolder.cell.setOnClickListener(view -> {

                if (appointmentsList.get(i).getStatus().equals("active")) {

                    new VigiAppointmentDetailsDialog(context).showActiveDetails(appointmentsList.get(i).getPatientId(), appointmentsIds.get(i), appointmentsList.get(i).getPaymentCode());
                }
                else if(appointmentsList.get(i).getStatus().equals("scanned")){

                    new VigiAppointmentDetailsDialog(context).showHistoryDetails(appointmentsList.get(i).getPatientId(), appointmentsIds.get(i), "scanned");

                }
                else if(appointmentsList.get(i).getStatus().equals("reviewed")){

                    new VigiAppointmentDetailsDialog(context).showHistoryDetails(appointmentsList.get(i).getPatientId(), appointmentsIds.get(i), "reviewed");

                }

            }
        );

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        TextView treatment;
        TextView careProviderName;
        TextView rating;
        TextView duration;
        TextView day;
        CircleImageView image;
        RelativeLayout cell;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            treatment = itemView.findViewById(R.id.patient_name);
            careProviderName = itemView.findViewById(R.id.cp_name);
            rating = itemView.findViewById(R.id.rating);
            duration = itemView.findViewById(R.id.duration);
            day = itemView.findViewById(R.id.day);
            image = itemView.findViewById(R.id.image);
            cell = itemView.findViewById(R.id.cell);

        }
    }
}
