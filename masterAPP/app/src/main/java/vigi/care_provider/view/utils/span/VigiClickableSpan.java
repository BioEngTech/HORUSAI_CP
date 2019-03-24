package vigi.care_provider.view.utils.span;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import vigi.care_provider.R;

public class VigiClickableSpan extends ClickableSpan {
    private Context context;

    private VigiClickableSpan() {}

    public VigiClickableSpan(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(@NonNull View widget) {
        Toast.makeText(context, "Terms and Conditions are still missing.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(ContextCompat.getColor(context, R.color.colorWhite));
    }
}
