package vigi.patient.view.patient.treatment.viewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.view.patient.treatment.booking.BookingActivity;
import vigi.patient.view.utils.dialog.VigiTreatmentDetailsDialog;

@SuppressWarnings("FieldCanBeLocal")
public class TreatmentsViewAdapter extends PagerAdapter implements View.OnClickListener {

    private String TAG = getClass().getName();
    private final static String CHOSEN_TREATMENT = "chosenTreatment";
    private List<Treatment> treatments;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageView imageView;
    private TextView title;
    private TextView knowMore;
    public static int currentPosition;

    public TreatmentsViewAdapter(List<Treatment> treatments, Context context) {
        this.treatments = treatments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return treatments.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.adapter_treatment_selection, container, false);

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        knowMore = view.findViewById(R.id.see_more);

        Picasso.get().load(treatments.get(position).getImage().toString()).into(imageView);
        title.setText(treatments.get(position).getName());
        setupKnowMore();

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
            new VigiTreatmentDetailsDialog(context).showDetails(treatments.get(currentPosition));
        } else if (view.getId() == imageView.getId()){ // Go to booking appointments

            Intent bookingIntent = new Intent(context, BookingActivity.class);
            bookingIntent.putExtra(CHOSEN_TREATMENT, treatments.get(currentPosition).getName());
            context.startActivity(bookingIntent);
        }
    }

    private void setupKnowMore() {
        SpannableString ss = new SpannableString(context.getString(R.string.know_more_text));
        ss.setSpan(new UnderlineSpan(), 0, context.getString(R.string.know_more_text).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        knowMore.setText(ss);
        knowMore.setMovementMethod(LinkMovementMethod.getInstance());
    }

}