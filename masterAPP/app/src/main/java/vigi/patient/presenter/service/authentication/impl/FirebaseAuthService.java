package vigi.patient.presenter.service.authentication.impl;

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
    public void login(String user, String password){
        loginResultTask = authInstance.signInWithEmailAndPassword(user, password);
    }

    @Override
    public void addLoginCompleteListener(OnCompleteListener<AuthResult> listener) {
        loginResultTask.addOnCompleteListener(listener);
    }

    @Override
    public void generateNewPassword(String user){
        generateNewPasswordTask = authInstance.sendPasswordResetEmail(user);
    }

    @Override
    public void addGenerateNewPasswordCompleteListener(OnCompleteListener<Void> listener) {
        generateNewPasswordTask.addOnCompleteListener(listener);
    }

    @Override
    public void register(String user, String password) {
        registerResultTask = authInstance.createUserWithEmailAndPassword(user, password);
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
    public void logout(){
        authInstance.signOut();
    }

}
