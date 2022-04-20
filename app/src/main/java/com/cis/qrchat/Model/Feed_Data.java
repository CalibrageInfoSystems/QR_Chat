package com.cis.qrchat.Model;

public class Feed_Data {

    int mainImg,feedImg;
    String accName,desc,timer,likecount,commentcount;

    public Feed_Data(int mainImg, int feedImg, String accName, String desc, String timer, String likecount, String commentcount) {
        this.mainImg = mainImg;
        this.feedImg = feedImg;
        this.accName = accName;
        this.desc = desc;
        this.timer = timer;
        this.likecount = likecount;
        this.commentcount = commentcount;
    }

    public int getMainImg() {
        return mainImg;
    }

    public void setMainImg(int mainImg) {
        this.mainImg = mainImg;
    }

    public int getFeedImg() {
        return feedImg;
    }

    public void setFeedImg(int feedImg) {
        this.feedImg = feedImg;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }
}
