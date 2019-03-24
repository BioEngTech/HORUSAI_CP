package vigi.care_provider.view.vigi.activity;

import vigi.care_provider.presenter.service.authentication.api.AuthenticationService;

public interface VigiLoginActivity extends VigiActivity {

    void performLogin(AuthenticationService authService, String email, String password);
}
