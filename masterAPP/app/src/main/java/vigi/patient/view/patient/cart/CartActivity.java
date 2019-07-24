package vigi.patient.view.patient.cart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import vigi.patient.R;
import vigi.patient.model.services.Appointment;
import vigi.patient.view.patient.cart.viewHolder.CartAdapter;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("FieldCanBeLocal")
public class CartActivity extends AppCompatActivity {

    private String TAG = getClass().getName();
    private ArrayList<Appointment> requestsList;
    private TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_cart);

        customizeActionBar();
        customizeToolBar();
        getRequestsInTheCart();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.recycler_view);
        RelativeLayout detailsLayout = findViewById(R.id.confirmation_layout);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CartAdapter adapter = new CartAdapter(requestsList, detailsLayout::setVisibility);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty_view)); // Can only be called after setting adapter
    }

    private void customizeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Treatments in the cart");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getRequestsInTheCart(){
        // TODO get all requests in the cart in order to show that list in the recycler view
        requestsList = new ArrayList<>();
        requestsList.add(new Appointment()); // Ex: if we add only one request in the cart
        // TODO get sum of all requests and update total price
        totalPrice = findViewById(R.id.total_price);
        totalPrice.setText(String.format("%s %s",150,getString(R.string.currency))); // Ex: if sum was 150
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

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
}