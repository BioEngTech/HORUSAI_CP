package vigi.patient.utils;

import android.app.Application;

public class FontFamilyInitiaton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypeFaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/sanfrancisco_light.ttf");
    }
}
