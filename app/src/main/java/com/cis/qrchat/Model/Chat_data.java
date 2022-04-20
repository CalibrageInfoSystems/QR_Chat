package com.cis.qrchat.Model;

public class Chat_data {
    String name;
    String image;
    String descrption;
    String time;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public Chat_data(String name, String image, String descrption, String time) {
        this.name = name;
        this.image = image;
        this.descrption = descrption;
        this.time = time;
    }





}
