package com.example.covid19tracker.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covid19tracker.Filters.Filters;
import com.example.covid19tracker.Filters.MutliCaseFilters;
import com.example.covid19tracker.Filters.SingleCaseFilter;
import com.example.covid19tracker.MainActivity;
import com.example.covid19tracker.R;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {
    private static final String TAG = "FilterFragment";

    // Filters
    private SingleCaseFilter singleCaseFilter;
    private MutliCaseFilters mutliCaseFilters;
    private List<Filters> singleLiltersList = new ArrayList<>();

    private EditText totalCasesGreaterThan,getTotalCasesLessthan,totalDeathCasesGreaterThan,totalDeathcasesLessthan,totalRecoveryGreaterthan,totalRecoveryLessthan;
    private Button filterBtn,closeBtn,resetBtn;
    private boolean isTotalDeathSelected ,isTotalCasesSelected,allFiltersSelected,isRecoveryFilterSelected;
    private List<String> filters = new ArrayList<>();
    // Listener
    private FilterFragmentListener listener;


    public FilterFragment(MainActivity activity) {
        this.listener = (FilterFragmentListener) activity;
    }

    public interface FilterFragmentListener {
        void onClickFilter(SingleCaseFilter singleCaseFilter);
        void onClickFilter(MutliCaseFilters mutliCaseFilters);
        void onClickClose();
        void onClickReset();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Filters");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView = inflater.inflate(R.layout.fragment_filter, container, false);
        totalCasesGreaterThan = currentView.findViewById(R.id.total_cases_greaterthan_id);
        getTotalCasesLessthan = currentView.findViewById(R.id.total_cases_lessthan_id);

        totalDeathCasesGreaterThan = currentView.findViewById(R.id.total_death_cases_greaterthan_id);
        totalDeathcasesLessthan = currentView.findViewById(R.id.total_death_cases_lessthan_id);


        totalRecoveryGreaterthan = currentView.findViewById(R.id.total_recovery_cases_greaterthan_id);
        totalRecoveryLessthan = currentView.findViewById(R.id.total_recovery_cases_lessthan_id);


        filterBtn = currentView.findViewById(R.id.filter_btn);
        resetBtn = currentView.findViewById(R.id.reset_id);
        closeBtn = currentView.findViewById(R.id.filter_close_btn);




        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickClose();
            }
        });


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalCasesLowerBound = Integer.MIN_VALUE;
                int totalCasesUpperBound = Integer.MAX_VALUE;
                int totalDeathCasesLowerBound = Integer.MIN_VALUE;
                int totalDeathCasesUpperBound = Integer.MAX_VALUE;
                int totalRecoveryLowerBound = Integer.MIN_VALUE;
                int totalRecoveryUpperBound = Integer.MAX_VALUE;


                String lowerBound = totalCasesGreaterThan.getText().toString();
                String upperBound = getTotalCasesLessthan.getText().toString();

                String deathLowerBound = totalDeathCasesGreaterThan.getText().toString();
                String deathUpperBound = totalDeathcasesLessthan.getText().toString();

                String recoveryLowerBound = totalRecoveryGreaterthan.getText().toString();
                String recoveryUpperBound = totalRecoveryLessthan.getText().toString();



                if(!lowerBound.isEmpty()){
                    filters.add("TotalCases");
                    isTotalCasesSelected = true;
                    totalCasesLowerBound = Integer.valueOf(lowerBound) ;
                }
                if(!upperBound.isEmpty()){
                    filters.add("TotalCases");
                    isTotalCasesSelected = true;
                    totalCasesUpperBound = Integer.valueOf(upperBound);
                }

                if(!deathLowerBound.isEmpty()){
                    filters.add("TotalDeaths");
                    isTotalDeathSelected = true;
                    totalDeathCasesLowerBound = Integer.valueOf(deathLowerBound);
                }
                if(!deathUpperBound.isEmpty()){
                    filters.add("TotalDeaths");
                    isTotalDeathSelected = true;
                    totalDeathCasesUpperBound = Integer.valueOf(deathUpperBound);
                }

                if(!recoveryLowerBound.isEmpty()){
                    filters.add("TotalRecovery");
                    isRecoveryFilterSelected = true;
                    totalRecoveryLowerBound = Integer.valueOf(recoveryLowerBound);
                }
                if(!recoveryUpperBound.isEmpty()){
                    filters.add("TotalRecovery");
                    isRecoveryFilterSelected = true;
                    totalRecoveryUpperBound = Integer.valueOf(recoveryUpperBound);
                }
                if(isTotalDeathSelected && isTotalCasesSelected && isRecoveryFilterSelected){
                    singleLiltersList.add(new SingleCaseFilter(totalCasesLowerBound,totalCasesUpperBound,"TotalCases"));
                    singleLiltersList.add(new SingleCaseFilter(totalDeathCasesLowerBound,totalDeathCasesUpperBound,"TotalDeaths"));
                    singleLiltersList.add(new SingleCaseFilter(totalRecoveryLowerBound,totalRecoveryUpperBound,"TotalRecovery"));
                    mutliCaseFilters = new MutliCaseFilters(singleLiltersList);
                    listener.onClickFilter(mutliCaseFilters);

                }else if(isTotalCasesSelected && isTotalDeathSelected){
                    singleLiltersList.add(new SingleCaseFilter(totalCasesLowerBound,totalCasesUpperBound,"TotalCases"));
                    singleLiltersList.add(new SingleCaseFilter(totalDeathCasesLowerBound,totalDeathCasesUpperBound,"TotalDeaths"));
                    mutliCaseFilters = new MutliCaseFilters(singleLiltersList);
                    listener.onClickFilter(mutliCaseFilters);

                }else if(isTotalCasesSelected && isRecoveryFilterSelected){
                    singleLiltersList.add(new SingleCaseFilter(totalCasesLowerBound,totalCasesUpperBound,"TotalCases"));
                    singleLiltersList.add(new SingleCaseFilter(totalRecoveryLowerBound,totalRecoveryUpperBound,"TotalRecovery"));
                    mutliCaseFilters = new MutliCaseFilters(singleLiltersList);
                    listener.onClickFilter(mutliCaseFilters);

                } else if(isTotalDeathSelected && isRecoveryFilterSelected){
                    singleLiltersList.add(new SingleCaseFilter(totalDeathCasesLowerBound,totalDeathCasesUpperBound,"TotalDeaths"));
                    singleLiltersList.add(new SingleCaseFilter(totalRecoveryLowerBound,totalRecoveryUpperBound,"TotalRecovery"));
                    mutliCaseFilters = new MutliCaseFilters(singleLiltersList);
                    listener.onClickFilter(mutliCaseFilters);

                } else if(isTotalDeathSelected){
                    singleCaseFilter = new SingleCaseFilter(totalDeathCasesLowerBound,totalDeathCasesUpperBound,filters.get(0));
                    listener.onClickFilter(singleCaseFilter);

                } else if(isRecoveryFilterSelected){
                    singleCaseFilter = new SingleCaseFilter(totalRecoveryLowerBound,totalRecoveryUpperBound,filters.get(0));
                    listener.onClickFilter(singleCaseFilter);

                }else {
                    filters.add("TotalCases");
                    singleCaseFilter = new SingleCaseFilter(totalCasesLowerBound,totalCasesUpperBound,filters.get(0));
                    listener.onClickFilter(singleCaseFilter);
                }

                resetTextFlags();
            }
        });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickReset();


                resetTextFlags();

                singleCaseFilter = null;
                mutliCaseFilters = null;

                resetAllTextFields();
            }
        });

        return currentView;
    }


    private void resetAllTextFields(){
        getTotalCasesLessthan.setText("");
        totalCasesGreaterThan.setText("");
        totalDeathcasesLessthan.setText("");
        totalDeathCasesGreaterThan.setText("");
        totalRecoveryGreaterthan.setText("");
        totalRecoveryLessthan.setText("");
    }

    private void resetTextFlags(){
        isRecoveryFilterSelected = false;
        isTotalDeathSelected = false;
        isTotalCasesSelected = false;
        allFiltersSelected = false;
        filters.clear();
        singleLiltersList.clear();
    }

}