package vigi.patient.view.patient.cart.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import vigi.patient.R;
import vigi.patient.model.services.Appointment;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;
import vigi.patient.view.vigi.activity.VigiRecyclerViewNotification;

public class CartAdapter extends EmptyRecyclerView.Adapter<CartAdapter.ViewHolder>{

    private final String TAG = getClass().getName();

    private ArrayList<Appointment> requests;
    private VigiRecyclerViewNotification vigiRecyclerViewNotification;

    public CartAdapter(ArrayList<Appointment> requests, VigiRecyclerViewNotification vigiRecyclerViewNotification) {
        this.requests = requests;
        this.vigiRecyclerViewNotification = vigiRecyclerViewNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_toolbar_cart,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        /*
            viewHolder.treatment.setText(requests.get(i).getTreatmentName()); TODO need method to get treatment name in class Appointment (Ex: getTreamentName())
            viewHolder.price.setText(String.format("@string/currency%s", requests.get(i).getPrice().toString()));
            viewHolder.cpName.setText(requests.get(i).getCareProviderName()); TODO need method to get care provider name in class Appointment (Ex: getCareProviderName())
            viewHolder.cpField.setText(requests.get(i).getCareProviderField()); TODO need method to get care provider field in class Appointment (Ex: getCareProviderField())
            viewHolder.image.setImageDrawable(requests.get(i).getTreatmentImage()); TODO need method to get treatment image in class Appointment (Ex: getTreatmentImage())
        */
        viewHolder.removeBtn.setOnClickListener(view -> {
          // TODO remove request from cart and update recycler view that is being shown to the user
        });
    }

    @Override
    public int getItemCount() {
        if (requests.isEmpty()) { vigiRecyclerViewNotification.onRecyclerViewNotification(View.GONE); }
        else { vigiRecyclerViewNotification.onRecyclerViewNotification(View.VISIBLE); }
        return requests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView treatment;
        TextView price;
        TextView cpName;
        TextView cpField;
        ImageView removeBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            treatment = itemView.findViewById(R.id.patient_name);
            price = itemView.findViewById(R.id.price);
            cpName = itemView.findViewById(R.id.cp_name);
            cpField = itemView.findViewById(R.id.cp_field);
            removeBtn = itemView.findViewById(R.id.removeIcon);
        }
    }

}
