package vigi.patient.view.utils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Arrays;

public final class ActivityUtils {
    public static final int CAMERA = 1888;
    public static final int GALLERY = 1889;

    /**
     * Creates an intent to another activity
     * and starts it
     *
     * @param from Activity where the app is at the moment
     * @param to Class of Activity intended to open
     * @param toFinish if the current activity must be finished
     */
    public static void jumpToActivity(Activity from, Class<? extends Activity> to, boolean toFinish) {
        Intent intentTo = new Intent(from, to);
        from.startActivity(intentTo);
        if (toFinish) from.finish();
    }

    public static void jumpToActivity(Activity from, Class<? extends Activity> to, boolean toFinish, Bundle options) {
        Intent intentTo = new Intent(from, to);
        from.startActivity(intentTo, options);
        if (toFinish) from.finish();
    }

    /**
     *
     * @param from
     * @param to
     * @param flags
     */
    public static void jumpToActivity(Activity from, Class<? extends Activity> to, boolean toFinish, int flags) {
        Intent intentTo = new Intent(from, to);
        intentTo.addFlags(flags);
        from.startActivity(intentTo);
        if (toFinish) from.finish();
    }

    /**
     *
     * @param from
     */
    public static void jumpToGalleryActivity(Activity from) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        from.startActivityForResult(galleryIntent, GALLERY);
    }

    /**
     *
     * @param from
     */
    public static void jumpToPhoneCameraActivity(Activity from) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        from.startActivityForResult(intent, CAMERA);
    }

    /**
     * Hides the keyboard from a particular view
     * as long as other input views are unfocused
     *
     * @param view view to hide the keyboard from
     * @param unfocusedViews views that should not have focus on
     */
    public static void hideKeyboardFrom(View view, View... unfocusedViews) {
        long viewsWithFocus = Arrays.stream(unfocusedViews).filter(View::hasFocus).count();
        if (!view.hasFocus() && viewsWithFocus == 0) {
            InputMethodManager input = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
