package vigi.patient.view.patient.treatment.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vigi.patient.R;


import vigi.patient.model.services.Treatment;
import vigi.patient.view.patient.appointment.BookAppointmentsActivity;
import vigi.patient.view.patient.treatment.TreatmentDetailsActivity;

@SuppressWarnings("FieldCanBeLocal")
public class TreatmentsViewAdapter extends PagerAdapter implements View.OnClickListener {

    private List<Treatment> treatments;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageView imageView;
    private TextView title;
    private TextView knowMore;
    private int position = 0;
    private String category;

    private final static String CHOSEN_TREATMENT = "chosenTreatment";


    public TreatmentsViewAdapter(String category, List<Treatment> treatments, Context context) {
        this.treatments = treatments;
        this.category = category;

        this.context = context;
    }

    @Override
    public int getCount() {
        //TODO: Must be refactored -> infinite = consuming resources
        return Integer.MAX_VALUE; // Make it an infinite view pager
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);

        //set loop
        if (this.position >= treatments.size() - 1) {
            this.position = 0;
        } else {
            ++this.position;
        }

        View view = layoutInflater.inflate(R.layout.patient_treatment_view, container, false);

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        knowMore = view.findViewById(R.id.see_more);

        //imageView.setImageDrawable(treatments.get(position).getImage());
        Picasso.get().load(treatments.get(this.position).getImage().toString()).into(imageView);
        title.setText(treatments.get(this.position).getName());

        imageView.setOnClickListener(this);
        knowMore.setOnClickListener(this);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == knowMore.getId()){ // Go to treatment details
            Intent treatmentDetailsIntent = new Intent(context, TreatmentDetailsActivity.class);
            treatmentDetailsIntent.putExtra(CHOSEN_TREATMENT, treatments.get(position));
            context.startActivity(treatmentDetailsIntent);

        } else if (view.getId() == imageView.getId()){ // Go to booking appointments
            Intent bookingIntent = new Intent(context, BookAppointmentsActivity.class);
            bookingIntent.putExtra(CHOSEN_TREATMENT, treatments.get(position));
            context.startActivity(bookingIntent);
        }
    }
}