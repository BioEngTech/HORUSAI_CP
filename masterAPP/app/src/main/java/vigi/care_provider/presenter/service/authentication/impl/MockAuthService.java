package vigi.care_provider.presenter.service.authentication.impl;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;

public class MockAuthService implements AuthenticationService {

    @Override
    public void init() {
        //mock answer
    }

    @Override
    public void login(String user, String password) {
        //mock answer
    }

    @Override
    public void addLoginCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        //mock answer
    }

    @Override
    public void generateNewPassword(String user) {
        //mock answer
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(Activity activity, OnCompleteListener<Void> listener) {
        //mock answer
    }

    @Override
    public void register(String user, String password) {
        //mock answer
    }

    @Override
    public void addRegisterCompleteListener(Activity activity, OnCompleteListener<AuthResult> listener) {
        //mock answer
    }

    @Override
    public void logout() {
        //mock answer
    }
}
