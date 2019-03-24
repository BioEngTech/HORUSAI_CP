package vigi.patient.presenter.service.authentication.impl;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import vigi.patient.presenter.service.authentication.api.AuthenticationService;

public class MockAuthService implements AuthenticationService {

    @Override
    public void init() {
        //mock answer
    }

    @Override
    public boolean login(String user, String password) {
        return true;
    }

    @Override
    public void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        //mock answer
    }

    @Override
    public boolean generateNewPassword(String user) {
        //mock answer
        return true;
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(Activity activity, OnCompleteListener<Void> listener) {
        //mock answer
    }

    @Override
    public boolean register(String user, String password) {
        //mock answer
        return true;
    }

    @Override
    public void addRegisterCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        //mock answer
    }

    @Override
    public String getCurrentUserString() {
        return null;
    }

    @Override
    public boolean logout() {
        //mock answer
        return true;
    }
}
