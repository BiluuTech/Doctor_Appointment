package app.biluutech.medicineprescription.Activities.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import app.biluutech.medicineprescription.ItemModel;
import app.biluutech.medicineprescription.MainActivity;
import app.biluutech.medicineprescription.R;

public class AllergiesActivity extends AppCompatActivity {

    private ListView listView;

    private SearchView searchView;

    private String diseasesName[] = {"headache", "malaria", "dengue", "d5", "d4", "d5", "d6", "d7", "d8", "d9", "d10"};
    private String diseasesPres[] = {"aspirin, ibuprofen", "atovaquone and proguanil", "acetaminophen ", "d3", "d4", "", "d6", "d7", "d8", "d9", "d10"};
    private String diseasesDesc[] = {"Headache is pain in any region of the head. Headaches may occur on one or both sides of the head",
            "Malaria causes symptoms that typically include fever, tiredness, vomiting, and headaches.",
            "Dengue fever is a mosquito-borne tropical disease caused by the dengue virus. Symptoms typically begin three to fourteen days after infection."
            , "desc 1", "desc 1", "desc 1", "desc 1", "desc 1", "desc 1", "desc 1", "desc 1"};

    private List<ItemModel> listItems = new ArrayList<>();
    private CustomAdapter customAdapter;

    private Toolbar toolbar;


    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    navView.getMenu().getItem(0).setChecked(true);
                    Intent intentHome = new Intent(AllergiesActivity.this, PatientHomeActivity.class);
                    startActivity(intentHome);
                    finish();


                case R.id.navigation_profile:
                    navView.getMenu().getItem(1).setChecked(true);

                    Intent intentProfile = new Intent(AllergiesActivity.this, PatientProfileActivity.class);
                    startActivity(intentProfile);
                    finish();

                    return true;
                case R.id.navigation_search:
                    navView.getMenu().getItem(2).setChecked(true);

                    Intent intentSearch = new Intent(AllergiesActivity.this, PatientSearchActivity.class);
                    startActivity(intentSearch);
                    finish();

                    return true;
                case R.id.navigation_allergies:
                    navView.getMenu().getItem(3).setChecked(true);

                    Intent intentDisease = new Intent(AllergiesActivity.this, AllergiesActivity.class);
                    startActivity(intentDisease);
                    finish();

                    return true;

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    navView.getMenu().getItem(4).setChecked(true);

                    Intent intentMain = new Intent(AllergiesActivity.this, MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        navView = findViewById(R.id.nav_view);

        navView.getMenu().getItem(3).setChecked(true);
        navView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        listView = findViewById(R.id.listView);

        for (int i = 0; i < diseasesName.length; i++) {

            ItemModel itemModel = new ItemModel(diseasesName[i],diseasesDesc[i],diseasesPres[i]);

            listItems.add(itemModel);

        }
        customAdapter = new CustomAdapter(listItems, this);
        listView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);

        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                customAdapter.getFilter().filter(newText);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search_view) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter implements Filterable {

        private List<ItemModel> itemModelList;
        private List<ItemModel> itemModelListFiltered;
        private Context context;

        public CustomAdapter(List<ItemModel> itemModelList, Context context) {
            this.itemModelList = itemModelList;
            this.itemModelListFiltered = itemModelList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return itemModelListFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.row_item, null);

            TextView itemName = view.findViewById(R.id.disease_name_TV);
            TextView itemDesc = view.findViewById(R.id.disease_causes_TV);
            TextView itemPres = view.findViewById(R.id.disease_prescription_TV);

            itemName.setText(itemModelListFiltered.get(position).getName());
            itemDesc.setText(itemModelListFiltered.get(position).getDesc());
            itemPres.setText(itemModelListFiltered.get(position).getPres());

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();

                    if (constraint == null || constraint.length() == 0) {

                        filterResults.count = itemModelList.size();
                        filterResults.values = itemModelList;

                    } else {
                        String searchStr = constraint.toString().toLowerCase();

                        List<ItemModel> resultData = new ArrayList<>();

                        for (ItemModel itemModel : itemModelList) {
                            if (itemModel.getName().contains(searchStr)) {
                                resultData.add(itemModel);
                            }
                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    itemModelListFiltered = (List<ItemModel>) results.values;
                    notifyDataSetChanged();

                }
            };

            return filter;
        }
    }

}
