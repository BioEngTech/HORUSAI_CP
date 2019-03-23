package vigi.care_provider.presenter.service.authentication.api;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

/**
 * Service responsible for operations related
 * with Authentication (not to confuse with Authorization)
 */
public interface AuthenticationService {

    void init();

    boolean login(String user, String password);

    void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener);

    boolean generateNewPassword(String user);

    void addGenerateNewPasswordCompleteListener(Activity activity, OnCompleteListener<Void> listener);

    boolean register(String user, String password);

    void addRegisterCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener);

    String getCurrentUserString();

    boolean logout();
}
