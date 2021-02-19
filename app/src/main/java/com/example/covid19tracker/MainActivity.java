package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19tracker.Adapters.CovidRecycleAdapter;
import com.example.covid19tracker.Api.ApiClient;
import com.example.covid19tracker.Api.ApiInterface;
import com.example.covid19tracker.Filters.MutliCaseFilters;
import com.example.covid19tracker.Filters.SingleCaseFilter;
import com.example.covid19tracker.Fragments.CountryListFragment;
import com.example.covid19tracker.Fragments.FilterFragment;
import com.example.covid19tracker.Model.Country;
import com.example.covid19tracker.Model.CovidData;
import com.example.covid19tracker.Model.Global;
import com.example.covid19tracker.Utility.ButtonStateHandler;
import com.example.covid19tracker.Utility.ButtonStateHandlerOptimized;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FilterFragment.FilterFragmentListener {
    private int LOCATION_REQUEST_CODE = 10001;
    private static final String TAG = "MainActivity";

    //searchTagHiding
    private MenuItem menuItem;


    private TextView confirmed, recovered, deceased;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private List<Country> countryList = new ArrayList<>();
    private List<Country> backUpCountryList = new ArrayList<>(countryList);

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String countryName = null;

    //Api setup
    private ApiInterface apiService = ApiClient.getService();

    private TextView countryTextView, totalCasesTextView, totalRecoveredTextView, totalDeathTextView;
    private ImageView descCountry, ascTotalCases, ascTotalRecovered, ascTotalDeath;

    //List Fragment
    private CountryListFragment countryListFragment;
    //Filter Fragment
    private FilterFragment filterFragment;

    //Button State Handler
    private ButtonStateHandlerOptimized btnStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1e1e30")));


        confirmed = (TextView) findViewById(R.id.confirm_id);
        recovered = (TextView) findViewById(R.id.recovered_id);
        deceased = (TextView) findViewById(R.id.deceased_id);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingBtnId);


        //State Handler Changes
        countryTextView = (TextView) findViewById(R.id.sort_by_country_id);
        descCountry = (ImageView) findViewById(R.id.desc_sort_img_id_country);
        totalCasesTextView = (TextView) findViewById(R.id.sort_by_total_cases_id);
        ascTotalCases = (ImageView) findViewById(R.id.sort_by_total_cases_id_img);
        ascTotalRecovered = (ImageView) findViewById(R.id.sort_by_total_recovered_id_img);
        totalRecoveredTextView = (TextView) findViewById(R.id.sort_by_total_recovered_cases_id);
        totalDeathTextView = (TextView) findViewById(R.id.sort_by_total_deaths_id);
        ascTotalDeath = (ImageView) findViewById(R.id.sort_by_total_deaths_id_img);


        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Showing Fragments
        countryListFragment = new CountryListFragment(countryList);
        filterFragment = new FilterFragment(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, countryListFragment, "CountryList")
                .commit();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //*//
                countryList = new ArrayList<>(backUpCountryList);
                Log.d(TAG, "onClick: Clicked " + countryList.size());
                Log.d(TAG, "onClick: Clicked ");
                countryListFragment.setProperData(backUpCountryList);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, filterFragment, "FilterList")
                        .addToBackStack("FilterList")
                        .commit();
                setTitleBasedOnCurrentFragment("Filters", false);
            }
        });

        totalDeathTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterIndicators();
                btnStateHandler = ButtonStateHandlerOptimized.getInstance(false, "TotalDeaths");
                if (btnStateHandler.isAsc()) {
                    countryList = btnStateHandler.getListSortedByDesc(countryList, "TotalDeaths");
                    ascTotalDeath.setVisibility(View.VISIBLE);
                } else {
                    countryList = btnStateHandler.getListSortedByAsc(countryList, "TotalDeaths");
                    ascTotalDeath.setVisibility(View.VISIBLE);
                }
                countryListFragment.setData(countryList);
                ascTotalDeath.setRotation(ascTotalDeath.getRotation() + 180);
            }
        });


        totalRecoveredTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterIndicators();
                btnStateHandler = ButtonStateHandlerOptimized.getInstance(false, "TotalRecovery");
                if (btnStateHandler.isAsc()) {
                    countryList = btnStateHandler.getListSortedByDesc(countryList, "Recovery");
                    ascTotalRecovered.setVisibility(View.VISIBLE);
                } else {
                    countryList = btnStateHandler.getListSortedByAsc(countryList, "Recovery");
                    ascTotalRecovered.setVisibility(View.VISIBLE);
                }
                countryListFragment.setData(countryList);
                ascTotalRecovered.setRotation(ascTotalRecovered.getRotation() + 180);
            }
        });

        totalCasesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterIndicators();
                btnStateHandler = ButtonStateHandlerOptimized.getInstance(false, "TotalCases");
                if (btnStateHandler.isAsc()) {
                    countryList = btnStateHandler.getListSortedByDesc(countryList, "TotalCases");
                    ascTotalCases.setVisibility(View.VISIBLE);
                } else {
                    countryList = btnStateHandler.getListSortedByAsc(countryList, "TotalCases");
                    ascTotalCases.setVisibility(View.VISIBLE);
                }
                countryListFragment.setData(countryList);
                ascTotalCases.setRotation(ascTotalCases.getRotation() + 180);
            }
        });


        countryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilterIndicators();
                btnStateHandler = ButtonStateHandlerOptimized.getInstance(false, "Country");
                if (btnStateHandler.isAsc()) {
                    countryList = btnStateHandler.getListSortedByDesc(countryList, "Country");
                    descCountry.setVisibility(View.VISIBLE);
                } else {
                    countryList = btnStateHandler.getListSortedByAsc(countryList, "Country");
                    descCountry.setVisibility(View.VISIBLE);
                }
                countryListFragment.setData(countryList);
                descCountry.setRotation(descCountry.getRotation() + 180);
            }
        });


        apiCall();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Page Refreshed.", Toast.LENGTH_SHORT).show();
                apiCall();
                handler.postDelayed(this, 120000);
            }
        }, 120000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_layout, menu);
        menuItem = menu.findItem(R.id.search_menu);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                resetFilterIndicators();
//                setFilterIndicatorsBasedOnFilters(new ArrayList<String>(Collections.singleton("Country")));
                Log.d(TAG, "onQueryTextChange: " + newText);
                countryListFragment.searchViewSetter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    void apiCall() {

        Call<CovidData> call = apiService.getCovidData();

        call.enqueue(new Callback<CovidData>() {
            @Override
            public void onResponse(Call<CovidData> call, Response<CovidData> response) {
                CovidData covidData = response.body();
                MainActivity that = MainActivity.this;
                if (covidData != null) {
                    Global global = covidData.getGlobal();
                    List<Country> countries = covidData.getCountries();
                    if (global != null) {
                        String confirmedCases = String.valueOf((global.getTotalConfirmed() == null) ? "NA" : global.getTotalConfirmed());
                        String recoveredCases = String.valueOf((global.getTotalRecovered() == null) ? "NA" : global.getTotalRecovered());
                        String deathCases = String.valueOf((global.getTotalDeaths() == null) ? "NA" : global.getTotalDeaths());
                        if (!confirmedCases.equals("NA")) confirmed.setText(confirmedCases);
                        if (!recoveredCases.equals("NA")) recovered.setText(recoveredCases);
                        if (!deathCases.equals("NA")) deceased.setText(deathCases);
                    }
                    if (countries != null && !countries.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        that.countryList = countries;
                        that.backUpCountryList = new ArrayList<>(countryList);
                        resetFilterIndicators();
                        setFilterIndicatorsBasedOnFilters(Collections.singletonList("TotalCases"));
                        Log.i("#Main", "Countries : " + that.countryList.size());
                        countryListFragment.setCountryList(countries);
                        countryListFragment.notifyAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<CovidData> call, Throwable t) {
                Log.i("#Retro onFail", "t : " + t.toString());
            }
        });
    }

    private void getGeoLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLongitude(), location.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            countryName = addresses.get(0).getCountryName();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG, "onSuccess: location null");
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });


//        Testing
        if (countryName == null) {
            this.countryName = "India";
            Toast.makeText(this, "Country set to " + countryName, Toast.LENGTH_SHORT).show();
        }


        if (countryName != null) {
            countryListFragment.setCountryName(this.countryName);
        }
    }

    private void resetFilterIndicators() {
        if (descCountry.getVisibility() != View.GONE) {
            descCountry.setVisibility(View.GONE);
        }
        if (ascTotalCases.getVisibility() != View.GONE) {
            ascTotalCases.setVisibility(View.GONE);
        }
        if (ascTotalRecovered.getVisibility() != View.GONE) {
            ascTotalRecovered.setVisibility(View.GONE);
        }
        if (ascTotalDeath.getVisibility() != View.GONE) {
            ascTotalDeath.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getGeoLocation();
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "Give me Permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGeoLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitleBasedOnCurrentFragment("Covid19Tracker", true);
    }

    void updateCountryListFragmentAdapterWithFilteredData(List<Country> argCountryList) {
        countryListFragment.setData(argCountryList);
        countryListFragment.notifyAdapter();
    }

    @Override
    public void onClickFilter(SingleCaseFilter singleCaseFilter) {

        List<Country> filterCountryList = singleCaseFilter.applyFilter(countryList);
        singleCaseFilter.setButtonStateHandlerOptimized(ButtonStateHandlerOptimized.getInstance(false, singleCaseFilter.getSingleFilter()));
        singleCaseFilter.setDefaultFilterOrder(filterCountryList, "DESC");
        Toast.makeText(this, "After Filtering Size : " + filterCountryList.size(), Toast.LENGTH_SHORT).show();


        countryList = filterCountryList;

        updateCountryListFragmentAdapterWithFilteredData(filterCountryList);
        List<String> list = new ArrayList<>();
        list.add(singleCaseFilter.getSingleFilter());
        setFilterIndicatorsBasedOnFilters(list);
        setTitleBasedOnCurrentFragment("Covid19Tracker", true);

        getSupportFragmentManager().popBackStackImmediate();    //to retrive previous frame
    }


    private void setImageView(String s) {
        switch (s) {
            case "TotalCases":
                ascTotalCases.setVisibility(View.VISIBLE);
                return;
            case "TotalRecovery":
                ascTotalRecovered.setVisibility(View.VISIBLE);
                return;
            case "TotalDeaths":
                ascTotalDeath.setVisibility(View.VISIBLE);
                return;
            case "Country":
                descCountry.setVisibility(View.VISIBLE);
                return;
            default:
                ascTotalDeath.setVisibility(View.VISIBLE);
                ascTotalRecovered.setVisibility(View.VISIBLE);
                ascTotalCases.setVisibility(View.VISIBLE);
                return;
        }
    }


    @Override
    public void onClickClose() {
        setTitleBasedOnCurrentFragment("Covid19Tracker", true);
        getSupportFragmentManager().popBackStackImmediate();    //to retrive previous frame
    }

    @Override
    public void onClickReset() {
        countryList = new ArrayList<>(backUpCountryList);
        countryListFragment.notifyAdapter();
    }

    private void setFilterIndicatorsBasedOnFilters(List<String> filters) {
        resetFilterIndicators();        // removing existing filters
        for (String s : filters) {
            setImageView(s);
        }
    }

    @Override
    public void onClickFilter(MutliCaseFilters mutliCaseFilters) {

        List<Country> filterList = mutliCaseFilters.applyFilter(countryList);
        mutliCaseFilters.addButtonStateHandlerOptimized();
        mutliCaseFilters.setAppropriateFilters();
        mutliCaseFilters.setDefaultFilterOrder(filterList, "DESC");

        countryList = filterList;
        Toast.makeText(this, "After Filtering Size : " + filterList.size(), Toast.LENGTH_SHORT).show();

        updateCountryListFragmentAdapterWithFilteredData(countryList);
        setFilterIndicatorsBasedOnFilters(mutliCaseFilters.getFilter());
        setTitleBasedOnCurrentFragment("Covid19Tracker", true);
        getSupportFragmentManager().popBackStackImmediate();    //to retrive previous frame

    }

    private void setTitleBasedOnCurrentFragment(String title, boolean isFloatingActionButtonVisible) {
        if (isFloatingActionButtonVisible) {
            if (menuItem != null) menuItem.setVisible(true);
            floatingActionButton.setVisibility(View.VISIBLE);
            setTitle(title);
        } else {
            if (menuItem != null) menuItem.setVisible(false);
            Log.i("This"," shoudn't be there!");
            floatingActionButton.setVisibility(View.GONE);
            setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.main_container);
        Fragment fragment2 = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment2 != null && fragment2 instanceof CountryListFragment) {
            setTitleBasedOnCurrentFragment("Covid19Tracker", true);
        }
        if (fragment1 != null && fragment1 instanceof FilterFragment) {
            setTitleBasedOnCurrentFragment("Filters", false);
        }
    }
}