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
     * logs in with user and password
     * @throws AuthenticationException if non successfull
     * @param user
     * @param password
     */
    void login(String user, String password) throws AuthenticationException;

    void addLoginCompleteListener(OnCompleteListener<AuthResult> listener);

    void generateNewPassword(String user) throws AuthenticationException;

    void addGenerateNewPasswordCompleteListener(OnCompleteListener<Void> listener);

    void register(String user, String password) throws AuthenticationException;

    void addRegisterCompleteListener(OnCompleteListener<AuthResult> listener);

    void logout() throws AuthenticationException;

    String getCurrentUserString();

}
