package com.example.covid19tracker.Filters;

import com.example.covid19tracker.Model.Country;
import com.example.covid19tracker.Utility.ButtonStateHandlerOptimized;

import java.util.ArrayList;
import java.util.List;

public class SingleCaseFilter extends Filters {
    public SingleCaseFilter(int lowerBound, int upperBound, String singleFilter) {
        super(lowerBound, upperBound, singleFilter);
    }

    @Override
    public void setDefaultFilterOrder(List<Country> countryList,String order) {
        if(this.buttonStateHandlerOptimized  != null){
            if(order.equals("DESC")){
                buttonStateHandlerOptimized.getListSortedByDesc(countryList,getSingleFilter());
            }else{
                buttonStateHandlerOptimized.getListSortedByAsc(countryList,getSingleFilter());
            }
        }
    }

    public List<Country> getFilteredDataBasedOnFilters(List<Country> input) {
        List<Country> filterList = new ArrayList<>();
        String currentFilter = getSingleFilter();
        for (int i = 0; i < input.size(); i++) {
            Country currentCountry = input.get(i);
            if (currentCountry != null) {
                if (currentFilter.equals("TotalRecovery")) {
                    if (currentCountry.getTotalRecovered() >= getLowerBound() && currentCountry.getTotalRecovered() <= getUpperBound()) {
                        filterList.add(currentCountry);
                    } else {
                        continue;
                    }
                } else if (currentFilter.equals("TotalDeaths")) {
                    if (currentCountry.getTotalDeaths() >= getLowerBound() && currentCountry.getTotalDeaths() <= getUpperBound()) {
                        filterList.add(currentCountry);
                    } else {
                        continue;
                    }
                } else if (currentFilter.equals("TotalCases")) {
                    if (currentCountry.getTotalConfirmed() >= getLowerBound() && currentCountry.getTotalConfirmed() <= getUpperBound()) {
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
    public void setButtonStateHandlerOptimized(ButtonStateHandlerOptimized btn) {
        this.buttonStateHandlerOptimized = btn;
    }

    @Override
    public List<Country> applyFilter(List<Country> countryList) {
        List<Country> filteredData = getFilteredDataBasedOnFilters(countryList);
        return filteredData;
    }
}

