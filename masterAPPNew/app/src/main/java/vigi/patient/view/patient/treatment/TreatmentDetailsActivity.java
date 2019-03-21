package vigi.patient.view.patient.treatment;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Objects;
import vigi.patient.R;
import vigi.patient.view.patient.appointment.BookAppointmentsActivity;


@SuppressWarnings("FieldCanBeLocal")
public class TreatmentDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = Activity.class.getName();
    private Toolbar myToolbar;
    private ImageView imageTreatment;
    private TextView category;
    private TextView duration;
    private TextView description;
    private TextView benefits;
    private FloatingActionButton bookingBtn;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.patient_treatment_details);

        // Get views present on the layout
        myToolbar = findViewById(R.id.toolbar);
        imageTreatment = findViewById(R.id.image);
        category = findViewById(R.id.category);
        duration = findViewById(R.id.duration);
        description = findViewById(R.id.description);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        benefits = findViewById(R.id.benefits);
        bookingBtn = findViewById(R.id.booking_btn);

        // Fetch selected treatment form the database using id previously selected,
        // and display information according to the selected treatment, example below
        Intent intent = getIntent();
        String treatmentId = Objects.requireNonNull(intent.getExtras()).getString("treatmentId");
        setTreatmentDetails(treatmentId);

        // Customize action bar / toolbar
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up booking btn
        bookingBtn.setOnClickListener(this);

    }


    private void setTreatmentDetails(String id){

        // TODO set up views according to the information of the treatment selected

        imageTreatment.setImageDrawable(getResources().getDrawable(R.drawable.image_daily));
        collapsingToolbar.setTitle("Daily tasks");
        duration.setText("1h30 average");
        category.setText("Daily assistance");
        description.setText("O interesse pelo texto como objeto de estudo gerou vários trabalhos importantes de teóricos da Linguística Textual," +
                " que percorreram fases diversas cujas características principais eram transpor os limites da frase descontextualizada da gramática tradicional" +
                " e ainda incluir os relevantes papéis do autor e do leitor na construção de textos.\n" + "Um texto pode ser escrito ou oral e, em sentido lato, pode ser também não verbal." +
                "\n" + "Todo texto tem que ter alguns aspectos formais, ou seja, tem que ter estrutura, elementos que estabelecem relação entre si. " +
                " Dentro dos aspectos formais temos a coesão e a coerência, que dão sentido e forma ao texto.");

        benefits.setText("O interesse pelo texto como objeto de estudo gerou vários trabalhos importantes de teóricos da Linguística Textual," +
                " que percorreram fases diversas cujas características principais eram transpor os limites da frase descontextualizada da gramática tradicional" +
                " e ainda incluir os relevantes papéis do autor e do leitor na construção de textos.\n" + "Um texto pode ser escrito ou oral e, em sentido lato, pode ser também não verbal." +
                "\n" + "Todo texto tem que ter alguns aspectos formais, ou seja, tem que ter estrutura, elementos que estabelecem relação entre si. " +
                " Dentro dos aspectos formais temos a coesão e a coerência, que dão sentido e forma ao texto.");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == bookingBtn.getId()){
            Intent bookingIntent = new Intent(this, BookAppointmentsActivity.class);
            startActivity(bookingIntent);
        }
    }

    // Action when back arrow is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return false;
    }

    // Action when back navigation button is pressed
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }
}
