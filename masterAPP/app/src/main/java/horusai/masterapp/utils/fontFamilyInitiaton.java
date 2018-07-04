package horusai.masterapp.utils;

import android.app.Application;

public class fontFamilyInitiaton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        typeFaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/sanfrancisco_light.ttf");
    }
}
