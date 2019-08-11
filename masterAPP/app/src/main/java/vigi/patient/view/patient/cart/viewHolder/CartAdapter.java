package vigi.patient.view.patient.cart.viewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.entities.Agenda;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Appointment;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.appointment.api.AppointmentService;
import vigi.patient.presenter.service.appointment.impl.FirebaseAppointmentService;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;
import vigi.patient.view.vigi.activity.VigiRecyclerViewNotification;

public class CartAdapter extends EmptyRecyclerView.Adapter<CartAdapter.ViewHolder>{

    private final String TAG = getClass().getName();

    private List<Appointment> appointmentsList;
    private List<CareProvider> careProvidersList;
    private List<Treatment> treatmentsList;
    private VigiRecyclerViewNotification vigiRecyclerViewNotification;
    private CareProvider careProvider;
    private Treatment treatment;
    private AppointmentService appointmentService;
    private Context context;
    private Appointment appointmentToBeRemoved;
    private List<String> appointmentsIds;

    public CartAdapter(Context context, List<String> appointmentsIds, List<Appointment> appointmentsList, List<CareProvider> careProvidersList, List<Treatment> treatmentsList, VigiRecyclerViewNotification vigiRecyclerViewNotification) {
        this.context = context;
        this.appointmentsList = appointmentsList;
        this.appointmentsIds = appointmentsIds;
        this.vigiRecyclerViewNotification = vigiRecyclerViewNotification;
        this.careProvidersList = careProvidersList;
        this.treatmentsList = treatmentsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_toolbar_cart,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        careProvider = careProvidersList.stream()
                .filter(careProvider -> careProvider.getId().equals(appointmentsList.get(i).getCareProviderId())).findFirst().orElse(null);
        treatment = treatmentsList.stream()
                .filter(treatment -> treatment.getId().equals(appointmentsList.get(i).getTreatmentId())).findFirst().orElse(null);

        viewHolder.treatment.setText(treatment.getName());
        viewHolder.price.setText(appointmentsList.get(i).getPrice()+"€");
        viewHolder.cpName.setText(careProvider.getName());
        viewHolder.cpField.setText(careProvider.getJob());

        String[] splitedDate = appointmentsList.get(i).getDate().split(" ");
        viewHolder.date.setText(splitedDate[0]);
        viewHolder.hour.setText(splitedDate[1]);
        Picasso.get().load(careProvider.getImage().toString()).into(viewHolder.image);

        viewHolder.removeBtn.setOnClickListener(view -> {
            appointmentService = new FirebaseAppointmentService();
            appointmentService.init();
            appointmentService.removeFirebaseAppointments(context,appointmentsIds.get(i));
        });
    }

    @Override
    public int getItemCount() {
        if (appointmentsList.isEmpty()) { vigiRecyclerViewNotification.onRecyclerViewNotification(View.GONE); }
        else { vigiRecyclerViewNotification.onRecyclerViewNotification(View.VISIBLE); }
        return appointmentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView treatment;
        TextView price;
        TextView cpName;
        TextView cpField;
        TextView date;
        TextView hour;
        ImageView removeBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            treatment = itemView.findViewById(R.id.patient_name);
            price = itemView.findViewById(R.id.price);
            cpName = itemView.findViewById(R.id.cp_name);
            cpField = itemView.findViewById(R.id.cp_field);
            removeBtn = itemView.findViewById(R.id.removeIcon);
            date = itemView.findViewById(R.id.date);
            hour = itemView.findViewById(R.id.hour);
        }
    }

}
