package vigi.patient;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import horusai.masterapp.initiation.ConfigureTypeUser;
import horusai.masterapp.initiation.Home;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * Created by joaosousa on 13/10/18.
 */

@RunWith(AndroidJUnit4.class)
public class IntegrationUiTest {

    @Rule
    public ActivityTestRule<Home> mActivityRule = new ActivityTestRule<>(Home.class);

    @BeforeClass
    public static void initOnce() {
        Intents.init();
    }

    @Test
    public void login_success(){
        Espresso.onView((withId(R.id.initiationHome_RegisterBtn)))
                .perform(ViewActions.click());

        intended(hasComponent(ConfigureTypeUser.class.getName()));
    }

}


