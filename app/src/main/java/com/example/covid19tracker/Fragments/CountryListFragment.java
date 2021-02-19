package com.example.covid19tracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid19tracker.Adapters.CovidRecycleAdapter;
import com.example.covid19tracker.MainActivity;
import com.example.covid19tracker.Model.Country;
import com.example.covid19tracker.R;

import java.util.ArrayList;
import java.util.List;

public class CountryListFragment extends Fragment {


    private RecyclerView recyclerView;
    private CovidRecycleAdapter covidRecycleAdapter;
    private List<Country> countryList;
    private String countryName = "";


    public CountryListFragment() {
        // Required empty public constructor
    }

    public CountryListFragment(List<Country> countryList){
        this.countryList = new ArrayList<>(countryList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getActivity().getActionBar().setTitle();

        View currentView =  inflater.inflate(R.layout.fragment_country_list, container, false);
        recyclerView = (RecyclerView) currentView.findViewById(R.id.recycle_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        covidRecycleAdapter = new CovidRecycleAdapter(countryList, getActivity());
        recyclerView.setAdapter(covidRecycleAdapter);
        return currentView;
    }

    public void setData(List<Country> countryList1){
        if(countryList1 != null){
            this.countryList = countryList1;
            covidRecycleAdapter.setData(countryList);
        }
    }

    public void setCountryName(String countryName1){
        if(countryName1 != null){
            this.countryName =countryName1;
            covidRecycleAdapter.setCountryName(this.countryName);
        }
    }

    public void setProperData(List<Country> countryList){
        covidRecycleAdapter.setProperData(countryList);
    }

    public void searchViewSetter(String queryString){
        covidRecycleAdapter.getFilter().filter(queryString);
    }

    public void setCountryList(List<Country> countryList1){
        if(countryList != null){
            this.countryList = countryList1;
            covidRecycleAdapter.setCountryList(this.countryList);
        }
    }
    public void notifyAdapter(){
        covidRecycleAdapter.notifyDataSetChanged();
    }
}