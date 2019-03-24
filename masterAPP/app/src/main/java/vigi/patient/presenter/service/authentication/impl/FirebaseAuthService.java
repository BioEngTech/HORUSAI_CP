package vigi.patient.presenter.service.authentication.impl;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import vigi.patient.presenter.error.exceptions.AuthenticationException;
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
    public void login(String user, String password) throws AuthenticationException {
        try{
            loginResultTask = authInstance.signInWithEmailAndPassword(user, password);
        } catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
    }

    @Override
    public void addLoginCompleteListener(OnCompleteListener<AuthResult> listener) {
        loginResultTask.addOnCompleteListener(listener);
    }

    @Override
    public void generateNewPassword(String user) throws AuthenticationException{
        try {
            generateNewPasswordTask = authInstance.sendPasswordResetEmail(user);
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(OnCompleteListener<Void> listener) {
        generateNewPasswordTask.addOnCompleteListener(listener);
    }

    @Override
    public void register(String user, String password) throws AuthenticationException{
        try {
            registerResultTask = authInstance.createUserWithEmailAndPassword(user, password);
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    @Override
    public void addRegisterCompleteListener(OnCompleteListener<AuthResult> listener) {
        registerResultTask.addOnCompleteListener(listener);
    }

    @Override
    public String getCurrentUserString() {
        checkNotNull(authInstance.getCurrentUser());
        return authInstance.getCurrentUser().toString();
    }

    @Override
    public void logout() throws AuthenticationException {
    }
}
