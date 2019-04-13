package vigi.patient.view.patient.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

private class TokenBroadcastReceiver extends BroadcastReceiver {

    private TokenBroadcastReceiver() { }

    @Override
    public void onReceive(Context context, Intent intent) {
        mProgressDialogController.finishProgress();
        if (intent == null) {
            return;
        }
        if (intent.hasExtra(TokenIntentService.STRIPE_ERROR_MESSAGE)) {
            // handle your error!
            return;
        }
        if (intent.hasExtra(TokenIntentService.STRIPE_CARD_TOKEN_ID) &&
                intent.hasExtra(TokenIntentService.STRIPE_CARD_LAST_FOUR)) {
            // handle your resulting token here
        }
    }
}
}
