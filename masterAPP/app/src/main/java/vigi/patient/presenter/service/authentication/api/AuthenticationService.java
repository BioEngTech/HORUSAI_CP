package vigi.patient.presenter.service.authentication.api;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

/**
 * Service responsible for operations related
 * with Authentication (not to confuse with Authorization)
 */
public interface AuthenticationService {

    void init();

    void login(String user, String password);

    void addLoginCompleteListener(OnCompleteListener<AuthResult> listener);

    void generateNewPassword(String user);

    void addGenerateNewPasswordCompleteListener(OnCompleteListener<Void> listener);

    void register(String user, String password);

    void addRegisterCompleteListener(OnCompleteListener<AuthResult> listener);

    void logout();

    String getCurrentUserString();

}
