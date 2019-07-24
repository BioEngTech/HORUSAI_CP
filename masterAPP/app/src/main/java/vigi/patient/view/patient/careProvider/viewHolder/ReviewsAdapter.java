package vigi.patient.view.patient.careProvider.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.services.Review;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

public class ReviewsAdapter extends EmptyRecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private final String TAG = getClass().getName();
    private ArrayList<Review> reviews;

    public ReviewsAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cprovider_profile_reviews, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // viewHolder.category.setText(reviews.get(i).getPatientName()); TODO need method to get name in Review
        // viewHolder.date.setText(reviews.get(i).getTreatmentDate()); TODO need method to get date in Review
        // viewHolder.comment.setText(reviews.get(i).getComment());  TODO need method to get comment in Review
        // viewHolder.image.setImageDrawable(reviews.get(i).getPatientImage()); // TODO need method to get image in Review
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

