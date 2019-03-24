package vigi.patient.view.vigi.activity;

import vigi.patient.presenter.service.authentication.api.AuthenticationService;

public interface VigiLoginActivity extends VigiActivity {

    void performLogin(AuthenticationService authService, String email, String password);
}
