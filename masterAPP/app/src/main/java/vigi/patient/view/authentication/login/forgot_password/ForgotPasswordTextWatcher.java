package vigi.patient.view.authentication.login.forgot_password;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;

import vigi.patient.R;

public class ForgotPasswordTextWatcher implements TextWatcher {
    private ForgotPasswordActivity activity;

    ForgotPasswordTextWatcher(ForgotPasswordActivity activity) {
        this.activity = activity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (activity.hasEmailError()) {
            activity.getEmailInput().setError(null);
            activity.setEmailError(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        //TODO: Isn't the text accessible from the editable?
        if (activity.getEmailText().getText().length() != 0){
            activity.getContinueBtn().setEnabled(true);
            activity.getContinueBtn().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorMain));
        } else {
            activity.getContinueBtn().setEnabled(false);
            activity.getContinueBtn().setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorGrayLight));
        }
    }
}
