package vigi.care_provider.view.patient.treatment;

import android.view.View;

public interface TreatmentClickListener {
    void onClick(View view, int position);

    void send(String url);
}