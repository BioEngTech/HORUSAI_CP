package vigi.patient.view.patient.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import vigi.patient.R;


import vigi.patient.model.services.Appointment;
import vigi.patient.view.patient.home.viewHolder.AppointmentsViewAdapter;
import vigi.patient.view.patient.treatment.SelectTreatmentActivity;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import static java.security.AccessController.getContext;

@SuppressWarnings("FieldCanBeLocal")
public class PaymentActivity extends AppCompatActivity{

    private static String TAG = Activity.class.getName();
    CardInputWidget mCardInputWidget;
    Card card;
    private static final String PUBLISHABLE_KEY = "pk_test_YmgOkUVspc6Naoa4EpvO4Jf800YgwDjC3F";
    private ProgressDialog progress;
    private Button purchase;
    private String errorMessage, clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_payment);

        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        //TODO FOCKING DELETE THE KEY FROM HERE. IM SERIOUS BOY!

        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            // Do not continue token creation.
            Toast.makeText(getApplicationContext(),"Invalid Card Data", Toast.LENGTH_LONG).show();
        }

        progress = new ProgressDialog(this);
        /*purchase = (Button) findViewById(R.id.purchase);
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });
    */

    }

    private void pay() throws APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        PaymentMethodCreateParams paymentMethodCreateParams =
                PaymentMethodCreateParams.create(
                        mCardInputWidget.getCard().toPaymentMethodParamsCard(),
                        null);
        boolean savePaymentMethod = true;
        PaymentIntentParams params = PaymentIntentParams
                .createConfirmPaymentIntentWithPaymentMethodCreateParams(
                        paymentMethodCreateParams,
                        clientSecret,
                        "https://vigi.herokuapp.com/",
                        savePaymentMethod);

        Stripe stripe = new Stripe(getApplicationContext(), PUBLISHABLE_KEY);
// Do not call on the UI thread or your app will crash
        PaymentIntent paymentIntent = stripe.confirmPaymentIntentSynchronous(params, PaymentConfiguration.getInstance().getPublishableKey());



    }
    private void buy(){
        boolean validation = card.validateCard();
        if(validation){
            startProgress("Validating Credit Card");
            new Stripe(getApplicationContext(), PUBLISHABLE_KEY).createToken(
                    card,
                    new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        @Override
                        public void onSuccess(Token token) {
                            // Send token to your own web service

                            finishProgress();
                            charge(token);
                        }
                    });
        } else if (!card.validateNumber()) {
            errorMessage = "The card number that you entered is invalid";
            Toast.makeText(getApplicationContext(),
                    errorMessage,
                    Toast.LENGTH_LONG
            ).show();
        } else if (!card.validateExpiryDate()) {
            errorMessage = "The expiration date that you entered is invalid";
            Toast.makeText(getApplicationContext(),
                    errorMessage,
                    Toast.LENGTH_LONG
            ).show();
        } else if (!card.validateCVC()) {
            errorMessage = "The CVC code that you entered is invalid";
            Toast.makeText(getApplicationContext(),
                    errorMessage,
                    Toast.LENGTH_LONG
            ).show();
        } else {
            errorMessage = "The card details that you entered are invalid";
            Toast.makeText(getApplicationContext(),
                    errorMessage,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void charge(Token cardToken){
        MyServer.chargeToken(cardToken);
    }

    private void startProgress(String title){
        progress.setTitle(title);
        progress.setMessage("Please Wait");
        progress.show();
    }
    private void finishProgress(){
        progress.dismiss();
    }




}

