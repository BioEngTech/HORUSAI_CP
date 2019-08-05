package vigi.patient.view.patient.careProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.model.services.Review;
import vigi.patient.model.services.Treatment;
import vigi.patient.view.patient.careProvider.booking.BookingActivity;
import vigi.patient.view.patient.careProvider.viewHolder.ReviewsAdapter;
import vigi.patient.view.patient.careProvider.viewHolder.ServicesAdapter;
import vigi.patient.view.patient.cart.CartActivity;
import vigi.patient.view.patient.search.SearchActivity;
import vigi.patient.view.utils.drawable.CountDrawable;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;
import vigi.patient.view.utils.recyclerView.ItemOffsetDecoration;
import vigi.patient.view.vigi.activity.VigiActivity;

import static com.google.common.base.Preconditions.checkNotNull;
import static vigi.patient.view.utils.activity.ActivityUtils.jumpToActivity;

@SuppressWarnings("FieldCanBeLocal")
public class ProfileActivity extends AppCompatActivity implements VigiActivity {

    private String TAG = getClass().getName();
    private ArrayList<Review> reviewsList;
    private ArrayList<Treatment> servicesList;
    private CareProvider careProvider;
    private final static String CHOSEN_CAREPROVIDER = "chosenCareProvider";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cprovider_profile);

        Intent intent = getIntent();
        careProvider = (CareProvider) Objects.requireNonNull(intent.getExtras()).getSerializable(CHOSEN_CAREPROVIDER);

        setupUiComponents();
        getServices();
        setUpServicesRecyclerView();
        getReviews();
        setUpReviewsRecyclerView();
        setupClickListeners();

    }

    @Override
    public void setupUiComponents() {

        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.smoothScrollTo(0,0);
        customizeActionBar();
        customizeToolBar();
    }

    public void setupClickListeners() {
        TextView booking = findViewById(R.id.booking);
        booking.setOnClickListener(v -> jumpToActivity(this, BookingActivity.class, false));
    }
    private void setUpServicesRecyclerView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.recycler_view_services);
        recyclerView.setNestedScrollingEnabled(false);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ServicesAdapter adapter = new ServicesAdapter(servicesList);
        recyclerView.setAdapter(adapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setEmptyView(findViewById(R.id.empty_view_services)); // always put after the adapter was set
    }

    private void getServices() {
        servicesList = new ArrayList<>();
        Treatment example = new Treatment();
        example.setName(careProvider.getName());
        servicesList.add(example);
        servicesList.add(example);
    }

    private void setUpReviewsRecyclerView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.recycler_view_reviews);
        recyclerView.setNestedScrollingEnabled(false);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ReviewsAdapter adapter = new ReviewsAdapter(reviewsList);
        recyclerView.setAdapter(adapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setEmptyView(findViewById(R.id.empty_view_reviews)); // always put after the adapter was set
    }

    private void customizeActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setTitle(careProvider.getName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getReviews() {
        reviewsList = new ArrayList<>();
        reviewsList.add(new Review());
        reviewsList.add(new Review());
        reviewsList.add(new Review());
    }

    public void setCount(Context context, String count, Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        CountDrawable badge;

        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // TODO get amount of requests in the cart and update cart icon with that value
        setCount(this, "1", menu); // In case there was one request in the cart
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            case R.id.search:
                Intent careProviderSearchIntent = new Intent(this, SearchActivity.class);
                startActivity(careProviderSearchIntent);
                return true;
            case R.id.cart:
                Intent checkCartIntent = new Intent(this, CartActivity.class);
                startActivity(checkCartIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}