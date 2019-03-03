package vigi.care_provider.view.vigi.activity;

import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;

public interface VigiGenerateNewPasswordActivity extends VigiActivity {

    void performGenerateNewPassword(AuthenticationService authService, String user);
}
