package vigi.patient.presenter.service.authentication.api;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import vigi.patient.presenter.error.exceptions.AuthenticationException;

/**
 * Service responsible for operations related
 * with Authentication (not to confuse with Authorization)
 */
public interface AuthenticationService {

    void init();

    /**
     * Login method
     * Return true if login is successfull
     * @throws AuthenticationException otherwise
     * @param user
     * @param password
     */
    boolean login(String user, String password) throws AuthenticationException;

    void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener);

    boolean generateNewPassword(String user);

    void addGenerateNewPasswordCompleteListener(Activity activity, OnCompleteListener<Void> listener);

    boolean register(String user, String password);

    void addRegisterCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener);

    String getCurrentUserString();

    boolean logout();
}
