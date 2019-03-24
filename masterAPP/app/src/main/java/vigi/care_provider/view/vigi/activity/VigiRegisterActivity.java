package vigi.care_provider.view.vigi.activity;

import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;

public interface VigiRegisterActivity extends VigiActivity {

    void performRegister(AuthenticationService authService, String email, String password);
}
