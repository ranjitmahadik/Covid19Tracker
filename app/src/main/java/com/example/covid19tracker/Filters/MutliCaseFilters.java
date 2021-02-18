package com.example.covid19tracker.Filters;

import android.util.Log;

import com.example.covid19tracker.Model.Country;
import com.example.covid19tracker.Utility.ButtonStateHandlerOptimized;

import java.util.ArrayList;
import java.util.List;

public class MutliCaseFilters extends Filters{
    List<Filters> singleCaseFilterList;

    public MutliCaseFilters(List<Filters> singleCaseFilterList) {
        this.singleCaseFilterList = singleCaseFilterList;
    }

    public void setAppropriateFilters(){
        for(int i=0;i<singleCaseFilterList.size();i++){
            String currFilter = singleCaseFilterList.get(i).getSingleFilter();
            setFilters(currFilter);
        }
    }

    public void addButtonStateHandlerOptimized() {
        this.buttonStateHandlerOptimized = ButtonStateHandlerOptimized.getInstance(false,singleCaseFilterList.get(singleCaseFilterList.size()-1).getSingleFilter());
    }

    @Override
    public void setDefaultFilterOrder(List<Country> countryList,String order) {
        if(this.buttonStateHandlerOptimized  != null){
            int idx = singleCaseFilterList.size();
            String sortingParam = singleCaseFilterList.get(idx-1).getSingleFilter();
            if(order.equals("DESC")){
                buttonStateHandlerOptimized.getListSortedByDesc(countryList,sortingParam);
            }else{
                buttonStateHandlerOptimized.getListSortedByAsc(countryList,sortingParam);
            }
        }
    }

    public List<Country> getFilteredDataBasedOnFilters(List<Country> input,Filters singleCaseFilter) {
        List<Country> filterList = new ArrayList<>();
        String currentFilter = singleCaseFilter.getSingleFilter();
        for (int i = 0; i < input.size(); i++) {
            Country currentCountry = input.get(i);
            if (currentCountry != null) {
                if (currentFilter.equals("TotalRecovery")) {
                    if (currentCountry.getTotalRecovered() >= singleCaseFilter.getLowerBound() && currentCountry.getTotalRecovered() <= singleCaseFilter.getUpperBound()) {
                        filterList.add(currentCountry);
                    } else {
                        continue;
                    }
                } else if (currentFilter.equals("TotalDeaths")) {
                    if (currentCountry.getTotalDeaths() >= singleCaseFilter.getLowerBound() && currentCountry.getTotalDeaths() <= singleCaseFilter.getUpperBound()) {
                        filterList.add(currentCountry);
                    } else {
                        continue;
                    }
                } else if (currentFilter.equals("TotalCases")) {
                    if (currentCountry.getTotalConfirmed() >= singleCaseFilter.getLowerBound() && currentCountry.getTotalConfirmed() <= singleCaseFilter.getUpperBound()) {
                        filterList.add(currentCountry);
                    } else {
                        continue;
                    }
                }
            }
        }
        return filterList;
    }

    @Override
    public void setButtonStateHandlerOptimized(ButtonStateHandlerOptimized buttonStateHandlerOptimized) { }

    @Override
    public List<Country> applyFilter(List<Country> countryList){
        for(int i=0;i<singleCaseFilterList.size();i++){
            countryList = getFilteredDataBasedOnFilters(countryList, singleCaseFilterList.get(i));
        }
        return countryList;
    }

}
