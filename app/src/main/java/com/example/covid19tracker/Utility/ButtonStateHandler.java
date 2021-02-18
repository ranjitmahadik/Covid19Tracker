package com.example.covid19tracker.Utility;

import com.example.covid19tracker.Model.Country;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ButtonStateHandler {
    public boolean isAsc() {
        return isAsc;
    }
    private boolean isAsc = false;
    private static String currentUser = "";
    private static ButtonStateHandler btnHandler;

    public static ButtonStateHandler getInstance(boolean state,String currentUserInp){
        if(!currentUser.equals(currentUserInp)){
            btnHandler = null;
            btnHandler = new ButtonStateHandler(state,currentUserInp);
        }
        return btnHandler;
    }

    private ButtonStateHandler(boolean isAsc, String currentUserInp) {
        this.isAsc = isAsc;
        currentUser = currentUserInp;
    }

    public List<Country> sortByCountryAsc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                return o1.getCountry().compareTo(o2.getCountry());
            }
        });
        isAsc = true;
        return countryList;
    }
    public List<Country> sortByCountryDesc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                return o2.getCountry().compareTo(o1.getCountry());
            }
        });
        isAsc = false;
        return countryList;
    }

    public List<Country> sortByTotalCasesDesc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalConfirmed() < o2.getTotalConfirmed()){
                    return 1;
                }
                return -1;
            }
        });
        isAsc = false;
        return countryList;
    }

    public List<Country> sortByTotalCasesAsc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalConfirmed() < o2.getTotalConfirmed()){
                    return -1;
                }
                return 1;
            }
        });
        isAsc = true;
        return countryList;
    }

    public List<Country> sortByTotalRecoveredAsc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalRecovered() < o2.getTotalRecovered()){
                    return -1;
                }
                return 1;
            }
        });
        isAsc = true;
        return countryList;
    }

    public List<Country> sortByTotalRecoveredDesc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalRecovered() < o2.getTotalRecovered()){
                    return 1;
                }
                return -1;
            }
        });
        isAsc = false;
        return countryList;
    }

    public List<Country> sortByTotalDeathsDesc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalDeaths() < o2.getTotalDeaths()){
                    return 1;
                }
                return -1;
            }
        });
        isAsc = false;
        return countryList;
    }

    public List<Country> sortByTotalDeathsAsc(List<Country> countryList){
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalDeaths() < o2.getTotalDeaths()){
                    return -1;
                }
                return 1;
            }
        });
        isAsc = true;
        return countryList;
    }

}
