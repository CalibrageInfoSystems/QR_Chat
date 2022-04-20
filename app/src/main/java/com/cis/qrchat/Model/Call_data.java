package com.cis.qrchat.Model;

public class Call_data {
    String name;
    String image;
    String descrption;
    String time;
    int callimage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCallimage() {
        return callimage;
    }

    public void setCallimage(int callimage) {
        this.callimage = callimage;
    }

    public Call_data(String name, String image, String descrption, String time,int callimage) {
        this.name = name;
        this.image = image;
        this.descrption = descrption;
        this.time = time;
        this.callimage = callimage;
    }
}
