package com.cis.qrchat.Model;

public class Regionitems {
    int Id;



    String name;
    String cityname;

    public Regionitems(int id, String name,  String cityname) {
        Id = id;
        this.name = name;
        this.cityname = cityname;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}
