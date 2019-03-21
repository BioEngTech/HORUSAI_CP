package vigi.care_provider.view.patient.treatment.view_holder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import vigi.care_provider.R;
import vigi.care_provider.view.patient.treatment.TreatmentClickListener;

public class ViewHolderTreatment extends RecyclerView.ViewHolder implements View.OnClickListener{

    View v;
    TreatmentClickListener treatmentClickListener;

    public ViewHolderTreatment(View itemView) {
        super(itemView);
        v = itemView;
        itemView.setOnClickListener(this);
    }

    public void setTreatmentClickListener(TreatmentClickListener treatmentClickListener){
        this.treatmentClickListener = treatmentClickListener;
    }

    public void setService(Context c, String nametxt, String descriptiontxt, String url) {
        TextView type = v.findViewById(R.id.nameTxt);
        TextView description = v.findViewById(R.id.descriptionTxt);
        ImageView serviceimage = v.findViewById(R.id.serviceimage);

        Picasso.get().load(url).into(serviceimage);
        type.setText(nametxt);
        description.setText(descriptiontxt);
    }

    @Override
    public void onClick(View v) {
        treatmentClickListener.onClick(v,getAdapterPosition());
    }
}

