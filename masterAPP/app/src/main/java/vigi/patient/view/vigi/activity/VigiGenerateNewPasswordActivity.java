package vigi.patient.view.vigi.activity;

import vigi.patient.presenter.service.authentication.api.AuthenticationService;

public interface VigiGenerateNewPasswordActivity extends VigiActivity {

    void performGenerateNewPassword(AuthenticationService authService, String user);
}
