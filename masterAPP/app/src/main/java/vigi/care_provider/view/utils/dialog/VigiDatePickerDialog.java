package vigi.care_provider.view.utils.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;

import java.util.Calendar;

import static com.google.common.base.Preconditions.checkNotNull;

public class VigiDatePickerDialog {
    private Activity activity;
    private Calendar calendarInstance;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private VigiDatePickerDialog() {}

    public VigiDatePickerDialog(Activity activity, DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.activity = activity;
        this.dateSetListener = onDateSetListener;
    }

    public void showDialog() {
        setCalendarInstance();

        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener, calendarInstance.get(Calendar.YEAR), calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.DAY_OF_MONTH));

        long hundredTenYears = 1000 * 60 * 60 * 24 * 39600L;

        dialog.getDatePicker().setMinDate(calendarInstance.getTimeInMillis() - hundredTenYears);
        dialog.getDatePicker().setMaxDate(calendarInstance.getTimeInMillis());
        checkNotNull(dialog.getWindow());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setCalendarInstance() {
        calendarInstance = Calendar.getInstance();
    }
}
