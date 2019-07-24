package vigi.patient.view.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import vigi.patient.R;
import vigi.patient.model.services.Treatment;

import static com.google.common.base.Preconditions.checkNotNull;

public class VigiTreatmentDetailsDialog {

    private Context context;
    private TextView duration;
    private TextView description;
    private TextView benefits;
    private ImageView close_cross;

    public VigiTreatmentDetailsDialog(Context context) {
        this.context = context;
    }

    public void showDetails(Treatment treatment){

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_treatment_details);
        checkNotNull(dialog.getWindow());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        duration = dialog.findViewById(R.id.duration);
        description = dialog.findViewById(R.id.description);
        benefits = dialog.findViewById(R.id.benefits);

        duration.setText(treatment.getMinutesOfDuration().toString());
        description.setText(treatment.getDescription());
        benefits.setText(treatment.getBenefits());

        close_cross = dialog.findViewById(R.id.close_dialog);
        close_cross.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }
}

