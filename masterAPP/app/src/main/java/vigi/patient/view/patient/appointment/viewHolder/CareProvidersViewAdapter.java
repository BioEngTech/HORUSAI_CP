package vigi.patient.view.patient.appointment.viewHolder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;


public class CareProvidersViewAdapter extends RecyclerView.Adapter<CareProvidersViewAdapter.ViewHolder>{

    private static String TAG = Activity.class.getName();

    private ArrayList<CareProvider> careProviders;

    public CareProvidersViewAdapter(ArrayList<CareProvider> careProvidersList) {
        careProviders = careProvidersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_careproviders_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name.setText(careProviders.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return careProviders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView field;
        TextView duration;
        TextView rating;
        TextView price;
        CircleImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            field = itemView.findViewById(R.id.field);
            duration = itemView.findViewById(R.id.duration);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);

        }
    }
}
