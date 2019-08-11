package vigi.patient.view.patient.careProvider.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

public class ServicesAdapter extends EmptyRecyclerView.Adapter<ServicesAdapter.ViewHolder>{

    private final String TAG = getClass().getName();
    private ArrayList<Treatment> treatments;
    private List<String> categories;

    public ServicesAdapter(ArrayList<Treatment> treatments) {
        this.treatments = treatments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cprovider_profile_services,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.treatment.setText(treatments.get(i).getName());
        viewHolder.category.setText(treatments.get(i).getCategory().categoryString());
        Picasso.get().load(treatments.get(i).getImage().toString()).into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return treatments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView treatment;
        CircleImageView image;
        TextView category;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            treatment = itemView.findViewById(R.id.treatment);
            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.category);

        }
    }

}

