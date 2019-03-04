package vigi.care_provider.presenter.service.authentication.impl;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;

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
    public void login(String user, String password) {
        loginResultTask = authInstance.signInWithEmailAndPassword(user, password);
    }

    @Override
    public void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        loginResultTask.addOnCompleteListener(activity, listener);
    }

    @Override
    public void generateNewPassword(String user) {
        generateNewPasswordTask = authInstance.sendPasswordResetEmail(user);
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(Activity activity, OnCompleteListener<Void> listener) {
        generateNewPasswordTask.addOnCompleteListener(activity, listener);
    }

    @Override
    public void register(String user, String password) {
        registerResultTask = authInstance.createUserWithEmailAndPassword(user, password);
    }

    @Override
    public void addRegisterCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        registerResultTask.addOnCompleteListener(activity, listener);
    }

    @Override
    public void logout() {

    }
}
