package vigi.patient.presenter.service.authentication.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Collections;

import vigi.patient.presenter.error.codes.FirebaseErrorCodes;
import vigi.patient.presenter.service.authentication.api.AuthenticationService;
import vigi.patient.view.authentication.login.LoginActivity;
import vigi.patient.view.patient.home.HomePatientActivity;
import vigi.patient.view.utils.dialog.VigiErrorDialog;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToActivity;

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
    public boolean login(String user, String password, Activity... activities) {
        try{
            loginResultTask = authInstance.signInWithEmailAndPassword(user, password);
            if (activities!=null){
                addLoginCompleteListener(activities[0],new LoginCompleteListener());
            }

            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        loginResultTask.addOnCompleteListener(activity, listener);

    }

    private class LoginCompleteListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {
                jumpToActivity(LoginActivity.this, HomePatientActivity.class, true,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                String errorText = "";
                try {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    errorText = FirebaseErrorCodes.exceptionType(errorCode);
                } catch (ClassCastException e){
                    errorText = "Internet connection is not available.";
                } finally {
                    background.performClick();
                    stopSpinningLoader(spin, loginBtn);
                    new VigiErrorDialog(LoginActivity.this).showDialog(errorText);
                }
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
