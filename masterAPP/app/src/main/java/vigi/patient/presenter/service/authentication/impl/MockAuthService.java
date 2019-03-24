package vigi.patient.presenter.service.authentication.impl;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import vigi.patient.presenter.error.exceptions.AuthenticationException;
import vigi.patient.presenter.service.authentication.api.AuthenticationService;

public class MockAuthService implements AuthenticationService {

    @Override
    public void init() {
        //mock answer
    }

    @Override
    public void login(String user, String password) throws AuthenticationException {
        //mock answer
    }

    @Override
    public void addLoginCompleteListener(OnCompleteListener<AuthResult> listener) {
        //mock answer
    }

    @Override
    public void generateNewPassword(String user) {
        //mock answer
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(OnCompleteListener<Void> listener) {
        //mock answer
    }

    @Override
    public void register(String user, String password) {
        //mock answer
    }

    @Override
    public void addRegisterCompleteListener(OnCompleteListener<AuthResult> listener) {

    }

    @Override
    public String getCurrentUserString() {
        return null;
    }

    @Override
    public void logout() {
        //mock answer
    }
}
