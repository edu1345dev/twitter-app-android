package com.appbox.jose.twitterapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edu_1 on 29/09/2016.
 */
public class RecentSearch{

    @SerializedName("search_list")
    private ArrayList<String> searchList;

    public RecentSearch(){
        searchList = new ArrayList<>();
    }

    public ArrayList<String> getSearchList() {
        return searchList;
    }

    public void setSearchList(ArrayList<String> searchList) {
        this.searchList = searchList;
    }

    public String getGson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
