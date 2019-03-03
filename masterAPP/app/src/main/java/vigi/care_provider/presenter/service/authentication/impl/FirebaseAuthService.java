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
    public void logout() {

    }
}
