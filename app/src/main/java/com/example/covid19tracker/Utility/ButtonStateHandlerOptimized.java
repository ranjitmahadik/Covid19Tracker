package com.example.covid19tracker.Utility;

import com.example.covid19tracker.Model.Country;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ButtonStateHandlerOptimized {
    public boolean isAsc() {
        return isAsc;
    }

    private boolean isAsc = false;
    private static String currentUser = "";
    private static ButtonStateHandlerOptimized btnHandler;

    public static ButtonStateHandlerOptimized getInstance(boolean state, String currentUserInp) {
        if (!currentUser.equals(currentUserInp)) {
            btnHandler = new ButtonStateHandlerOptimized(state, currentUserInp);
        }
        return btnHandler;
    }

    private ButtonStateHandlerOptimized(boolean isAsc, String currentUserInp) {
        this.isAsc = isAsc;
        currentUser = currentUserInp;
    }


    public List<Country> getListSortedByDesc(List<Country> countryList, String param) {
        isAsc = false;
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                switch (param) {
                    case "Country":
                        return o2.getCountry().compareTo(o1.getCountry());
                    case "TotalCases":
                        if (o1.getTotalConfirmed() < o2.getTotalConfirmed()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    case "TotalDeaths":
                        if (o1.getTotalDeaths() < o2.getTotalDeaths()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    default:
                        if (o1.getTotalRecovered() < o2.getTotalRecovered()) {
                            return 1;
                        } else {
                            return -1;
                        }
                }
            }
        });
        return countryList;
    }






    public List<Country> getListSortedByAsc(List<Country> countryList, String param) {
        isAsc = true;
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                switch (param) {
                    case "Country":
                        return o1.getCountry().compareTo(o2.getCountry());
                    case "TotalCases":
                        if (o1.getTotalConfirmed() < o2.getTotalConfirmed()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    case "TotalDeaths":
                        if (o1.getTotalDeaths() < o2.getTotalDeaths()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    default:
                        if (o1.getTotalRecovered() < o2.getTotalRecovered()) {
                            return -1;
                        } else {
                            return 1;
                        }
                }
            }
        });
        return countryList;
    }

}
