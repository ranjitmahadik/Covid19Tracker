package com.example.covid19tracker.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19tracker.Model.Country;
import com.example.covid19tracker.Model.Global;
import com.example.covid19tracker.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;





public class CovidRecycleAdapter extends RecyclerView.Adapter<CovidRecycleAdapter.CovidViewHolder> {
    private List<Country> countryList;
    private Activity activity;

    public void setCountryName(String countryName) {
        this.countryName = countryName;
        setUserToTop();
    }

    private String countryName = null;

    public CovidRecycleAdapter(List<Country> countryList, Activity activity) {
        this.countryList = countryList;
        this.activity = activity;
    }

    private void setInitialData(List<Country> tempCountryList){
        this.countryList = tempCountryList;
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getTotalConfirmed() > o2.getTotalConfirmed()){
                    return  -1;
                }
                return 1;
            }
        });
        setUserToTop();
        notifyDataSetChanged();
    }

    class CovidViewHolder extends RecyclerView.ViewHolder {
        public TextView country, deaths, recovery, totalCases;

        public CovidViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country_name_id);
            deaths = itemView.findViewById(R.id.total_death_id);
            recovery = itemView.findViewById(R.id.total_recovery_id);
            totalCases = itemView.findViewById(R.id.total_case_id);
        }
    }

    @NonNull
    @Override
    public CovidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.country_list_item, parent, false);
        return new CovidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidViewHolder holder, int position) {

        Country currentCountry = countryList.get(position);

        if(currentCountry == null)  return;

        String countryName = currentCountry.getCountry();

        countryName = ((countryName == null) ? "NA" : countryName);
        String deaths = String.valueOf((currentCountry.getTotalDeaths() == null) ? "-1" : currentCountry.getTotalDeaths());
        String recovery = String.valueOf((currentCountry.getTotalRecovered() == null) ? "-1" : currentCountry.getTotalRecovered());
        String totalCases = String.valueOf((currentCountry.getTotalConfirmed() == null) ? "-1" : currentCountry.getTotalConfirmed());

        holder.country.setText(countryName);
        holder.totalCases.setText(totalCases);
        holder.recovery.setText(recovery);
        holder.deaths.setText(deaths);

        if(countryName != null && currentCountry.isUserCountry() != null && currentCountry.isUserCountry()){
            holder.country.setText("*"+countryName);
            holder.country.setTextColor(Color.parseColor("#db5581"));
            holder.totalCases.setTextColor(Color.parseColor("#db5581"));
            holder.recovery.setTextColor(Color.parseColor("#db5581"));
            holder.deaths.setTextColor(Color.parseColor("#db5581"));
        }else{
            holder.country.setTextColor(Color.parseColor("#6c757d"));
            holder.totalCases.setTextColor(Color.parseColor("#6c757d"));
            holder.recovery.setTextColor(Color.parseColor("#6c757d"));
            holder.deaths.setTextColor(Color.parseColor("#6c757d"));
        }
    }

    public void setCountryList(List<Country> countryList) {
        setInitialData(countryList);
    }

    public void setData(List<Country> countryList){
        this.countryList = countryList;
//        setUserToTop();
        notifyDataSetChanged();
    }

    private void setUserToTop(){
        int idx = getUserCountry();
        if(idx != -1){
            Country userPresentCountry = this.countryList.get(idx);
            this.countryList.remove(idx);
            countryList.add(0,userPresentCountry);
        }
    }
    private int getUserCountry(){
        int idx = -1;
        if(this.countryName != null){
            for(int i=0;i<this.countryList.size();i++){
                Country currentCountry = this.countryList.get(i);
                if(currentCountry.getCountry() != null && currentCountry.getCountry().equals(this.countryName)){
                    idx = i;
                    Log.i("#Country:"," is : "+currentCountry.getCountry());
                    currentCountry.setUserCountry(true);
                    break;
                }
            }
        }
        return idx;
    }


    @Override
    public int getItemCount() {
        return countryList.size();
    }

}
