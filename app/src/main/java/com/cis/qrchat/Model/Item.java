package com.cis.qrchat.Model;

public class Item {
    private String name;
    private String Id;
    private Boolean value;

    public Item(String id,String name, Boolean value) {
        this.name = name;
        Id = id;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
