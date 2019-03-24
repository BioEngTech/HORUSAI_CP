package vigi.patient.presenter.service.authentication.impl;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import vigi.patient.presenter.service.authentication.api.AuthenticationService;

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
    public boolean login(String user, String password) {
        try {
            loginResultTask = authInstance.signInWithEmailAndPassword(user, password);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        loginResultTask.addOnCompleteListener(activity, listener);
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
