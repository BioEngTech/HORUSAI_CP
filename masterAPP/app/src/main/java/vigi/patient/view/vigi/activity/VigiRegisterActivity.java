package vigi.patient.view.vigi.activity;

import vigi.patient.presenter.service.authentication.api.AuthenticationService;

public interface VigiRegisterActivity extends VigiActivity {

    void performRegister(AuthenticationService authService, String email, String password);
}
