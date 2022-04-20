package com.cis.qrchat.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Searchobject {

    @SerializedName("searchValue")
    @Expose
    private String searchValue;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

}