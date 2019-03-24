package vigi.patient.presenter.service.authentication.impl;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.concurrent.atomic.AtomicBoolean;

import vigi.patient.presenter.error.codes.FirebaseErrorCodes;
import vigi.patient.presenter.error.exceptions.AuthenticationException;
import vigi.patient.presenter.service.authentication.api.AuthenticationService;
import vigi.patient.view.authentication.login.LoginActivity;
import vigi.patient.view.utils.dialog.VigiErrorDialog;

import static com.google.common.base.Preconditions.checkNotNull;

public class FirebaseAuthService implements AuthenticationService {

    private FirebaseAuth authInstance;
    private Task<AuthResult> loginResultTask;
    private Task<Void> generateNewPasswordTask;
    private Task<AuthResult> registerResultTask;

    @Override
    public void init() {
        authInstance = FirebaseAuth.getInstance();
    }

    @Override
    public boolean login(String user, String password) throws AuthenticationException {
        final String[] errorText = new String[1];

        try{
            loginResultTask = authInstance.signInWithEmailAndPassword(user, password);

            loginResultTask.addOnCompleteListener(task -> {
                try {
                    onCompleteLogin(task);
                } catch (AuthenticationException e) {
                    errorText[0] = e.getMessage();
                }
            }).wait();

            if (errorText[0].isEmpty()) {
                return true;
            } else {
                throw new AuthenticationException(errorText[0]);
            }
        } catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
    }

    @Override
    public void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        loginResultTask.addOnCompleteListener(activity, listener);

    }

    private boolean onCompleteLogin(Task<AuthResult> task) throws AuthenticationException {
        if (task.isSuccessful()) {
            return true;
        } else {
            String errorText = "";
            try {
                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                errorText = FirebaseErrorCodes.exceptionType(errorCode);
            } catch (ClassCastException e){
                errorText = "Internet connection is not available.";
            } finally {
                throw new AuthenticationException(errorText);
            }
        }
    }

    @Override
    public boolean generateNewPassword(String user) {
        try {
            generateNewPasswordTask = authInstance.sendPasswordResetEmail(user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(Activity activity, OnCompleteListener<Void> listener) {
        generateNewPasswordTask.addOnCompleteListener(activity, listener);
    }

    @Override
    public boolean register(String user, String password) {
        try {
            registerResultTask = authInstance.createUserWithEmailAndPassword(user, password);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void addRegisterCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        registerResultTask.addOnCompleteListener(activity, listener);
    }

    @Override
    public String getCurrentUserString() {
        checkNotNull(authInstance.getCurrentUser());
        return authInstance.getCurrentUser().toString();
    }

    @Override
    public boolean logout() {
        return true;
    }
}
