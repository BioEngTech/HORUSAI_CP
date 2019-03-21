package vigi.care_provider.view.utils.fonts;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;


public class FontFamilyInitiation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        overrideFont(getApplicationContext(), "SERIF", "fonts/sanfrancisco_light.ttf");
    }

    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {

            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);

        } catch (Exception e) {
            Log.d("ERROR-FONT", e.toString());
        }
    }
}
