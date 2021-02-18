package com.example.covid19tracker.Filters;


import com.example.covid19tracker.Model.Country;
import com.example.covid19tracker.Utility.ButtonStateHandlerOptimized;

import java.util.ArrayList;
import java.util.List;

public abstract class Filters {
    public Filters(){}
    public Filters(int lowerBound, int upperBound, List<String> filter) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.filter = filter;
    }
    public Filters(int lowerBound, int upperBound, String singleFilter) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.singleFilter = singleFilter;
    }
    public int getLowerBound() {
        return lowerBound;
    }
    public int getUpperBound() {
        return upperBound;
    }

    protected ButtonStateHandlerOptimized buttonStateHandlerOptimized;
    private int lowerBound;
    private int upperBound;
    private String singleFilter;
    private List<String> filter = new ArrayList<>();

    void setFilters(String currFilter){
        filter.add(currFilter);
    }
    public List<String> getFilter() {
        return filter;
    }
    public String getSingleFilter() {
        return singleFilter;
    }

    public abstract void setDefaultFilterOrder(List<Country> countryList,String order);
    public abstract void setButtonStateHandlerOptimized(ButtonStateHandlerOptimized buttonStateHandlerOptimized);
    public abstract List<Country> applyFilter(List<Country> countryList);
}
