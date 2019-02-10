package vigi.patient.user;

import android.view.View;

public interface ItemClickListener {
    void onClick(View view, int position);

    void send(String url);
}