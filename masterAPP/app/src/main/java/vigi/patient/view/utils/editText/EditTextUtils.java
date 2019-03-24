package vigi.patient.view.utils.editText;

import android.widget.EditText;

import java.util.Optional;

public final class EditTextUtils {

    public static String getTrimmedText(EditText editText) {
        return Optional.ofNullable(editText.getText())
                .map(CharSequence::toString)
                .map(String::trim).get();
    }
}
