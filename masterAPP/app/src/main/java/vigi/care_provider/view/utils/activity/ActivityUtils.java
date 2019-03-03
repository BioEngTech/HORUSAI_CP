package vigi.care_provider.view.utils.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Arrays;

import vigi.care_provider.view.authentication.home.HomeAuthActivity;
import vigi.care_provider.view.init.WelcomeScreenActivity;

public final class ActivityUtils {

    /**
     * Creates an intent to another activity
     * and starts it
     *
     * @param from Activity where the app is at the moment
     * @param to Class of Activity intended to open
     */
    public static void jumpToActivity(Activity from, Class<? extends Activity> to) {
        Intent intentTo = new Intent(from, to);
        from.startActivity(intentTo);
    }

    public static void jumpToActivity(Activity from, Class<? extends Activity> to, Bundle options) {
        Intent intentTo = new Intent(from, to);
        from.startActivity(intentTo, options);
    }

    /**
     *
     * @param from
     * @param to
     * @param flags
     */
    public static void jumpToActivity(Activity from, Class<? extends Activity> to, int flags) {
        Intent intentTo = new Intent(from, to);
        intentTo.addFlags(flags);
        from.startActivity(intentTo);
    }

    /**
     * Hides the keyboard from a particular view
     * as long as other input views are unfocused
     *
     * @param view view to hide the keyboard from
     * @param unfocusedViews views that should not have focus on
     */
    public static void hideKeyboardFrom(View view, View... unfocusedViews) {
        long viewsWithoutFocus = Arrays.stream(unfocusedViews).filter(View::hasFocus).count();
        if (!view.hasFocus() && viewsWithoutFocus == 0) {
            InputMethodManager input = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
