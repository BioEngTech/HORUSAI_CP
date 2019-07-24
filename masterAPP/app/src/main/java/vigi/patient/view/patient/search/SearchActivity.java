package vigi.patient.view.patient.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.ArrayList;
import vigi.patient.R;
import vigi.patient.model.entities.CareProvider;
import vigi.patient.view.patient.search.viewHolder.SearchAdapter;
import vigi.patient.view.utils.recyclerView.EmptyRecyclerView;
import vigi.patient.view.utils.recyclerView.ItemOffsetDecoration;
import static com.google.common.base.Preconditions.checkNotNull;

public class SearchActivity extends AppCompatActivity {
    private SearchAdapter adapter;
    private ArrayList<CareProvider> careProvidersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_search);

        getCareProvidersInAcceptedRange();
        customizeActionBar();
        customizeToolBar();
        setUpRecyclerView();
    }

    private void customizeActionBar() {
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void customizeToolBar() {
        checkNotNull(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getCareProvidersInAcceptedRange() {
        careProvidersList = new ArrayList<>();
        CareProvider example1 = new CareProvider();
        example1.setName("Michael");
        careProvidersList.add(example1);
        CareProvider example2 = new CareProvider();
        example2.setName("John");
        careProvidersList.add(example2);
        careProvidersList.add(example2);
        careProvidersList.add(example2);
        careProvidersList.add(example2);
        careProvidersList.add(example2);
        careProvidersList.add(example2);
        careProvidersList.add(example2);
    }

    private void setUpRecyclerView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.recycler_view);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.recycler_view_vertical_margin);
        recyclerView.addItemDecoration(itemDecoration);
        EmptyRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(careProvidersList);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search care provider...");
        searchItem.expandActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}