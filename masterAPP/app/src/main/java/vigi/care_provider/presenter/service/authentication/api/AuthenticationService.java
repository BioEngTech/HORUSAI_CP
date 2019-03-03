package vigi.care_provider.presenter.service.authentication.api;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Service responsible for operations related
 * with Authentication (not to confuse with Authorization)
 */
public interface AuthenticationService {

    void init();

    void login(String user, String password);

    void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener);

    void logout();
}
