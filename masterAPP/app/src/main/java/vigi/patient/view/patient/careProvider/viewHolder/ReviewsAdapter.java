package vigi.patient.view.patient.careProvider.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.entities.Patient;
import vigi.patient.model.services.Appointment;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

public class ReviewsAdapter extends EmptyRecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private final String TAG = getClass().getName();
    private ArrayList<Appointment> reviews;
    private CareProvider careProvider;
    private ArrayList<Patient> patients;
    private Patient patient;

    public ReviewsAdapter(ArrayList<Appointment> reviews, ArrayList<Patient> patients, CareProvider careProvider) {
        this.reviews = reviews;
        this.patients = patients;
        this.careProvider = careProvider;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cprovider_profile_reviews, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        patient = patients.stream()
                .filter(patient -> patient.getId().equals(reviews.get(i).getPatientId())).findFirst().orElse(null);

        viewHolder.date.setText("Treated by "+ careProvider.getName() +" on the " + reviews.get(i).getDate());
        viewHolder.patient.setText(patient.getName());
        Picasso.get().load(patients.get(i).getImage()).into(viewHolder.image);
        viewHolder.comment.setText(reviews.get(i).getReview());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView patient;
        TextView date;
        TextView comment;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            patient = itemView.findViewById(R.id.patient);
            date = itemView.findViewById(R.id.date);
            comment = itemView.findViewById(R.id.comment);

        }
    }

}

