package vigi.patient.view.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import vigi.patient.R;
import vigi.patient.model.services.Treatment;
import vigi.patient.presenter.service.appointment.api.AppointmentService;
import vigi.patient.presenter.service.appointment.impl.FirebaseAppointmentService;

import static com.google.common.base.Preconditions.checkNotNull;

public class VigiAppointmentDetailsDialog {

    private Context context;
    private TextView qrcode;
    private TextView description;
    private TextView benefits;
    private Button cancel_button;
    private ImageView close_cross;
    private AppointmentService appointmentService;

    public VigiAppointmentDetailsDialog(Context context) {
        this.context = context;
    }

    public void showActiveDetails(String currentPatientId, String appointmentId, String paymentCode){

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_appointment);
        checkNotNull(dialog.getWindow());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        qrcode = dialog.findViewById(R.id.qrcode);
        qrcode.setText(paymentCode);

        cancel_button = dialog.findViewById(R.id.cancel_btn);
        cancel_button.setOnClickListener(view -> {
            appointmentService = new FirebaseAppointmentService();
            appointmentService.init();
            appointmentService.setStatusFirebaseAppointments(context,appointmentId,"cancel");

            dialog.dismiss();
        });

        close_cross = dialog.findViewById(R.id.close_btn);
        close_cross.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    public void showHistoryDetails(String currentPatientId, String appointmentId, String status){

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_appointment);
        checkNotNull(dialog.getWindow());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        qrcode = dialog.findViewById(R.id.qrcode);

        cancel_button = dialog.findViewById(R.id.cancel_btn);
        if (status.equals("reviewed")){
            cancel_button.setText("Revisit medical report");
            qrcode.setHint("Your Care Provider has shared medical report");
        }
        else if (status.equals("scanned")){
            cancel_button.setText("Review Appointment");
            qrcode.setHint("Write here your review");
        }

        cancel_button.setOnClickListener(view -> {

        });

        close_cross = dialog.findViewById(R.id.close_btn);
        close_cross.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

}

