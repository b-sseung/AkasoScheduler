package com.sseung.akasoschedule;

import android.os.Parcel;
import android.os.Parcelable;

public class Upload_Item {

    int number;
    int uploadYY, uploadMM, uploadDD;
    int year, month, day;
    String name, image, state;

    public Upload_Item(int number, int uploadYY, int uploadMM, int uploadDD, int year, int month, int day, String name, String image, String state){
        this.number = number;
        this.uploadYY = uploadYY;
        this.uploadMM = uploadMM;
        this.uploadDD = uploadDD;
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
        this.image = image;
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getUploadYY() {
        return uploadYY;
    }

    public void setUploadYY(int uploadYY) {
        this.uploadYY = uploadYY;
    }

    public int getUploadMM() {
        return uploadMM;
    }

    public void setUploadMM(int uploadMM) {
        this.uploadMM = uploadMM;
    }

    public int getUploadDD() {
        return uploadDD;
    }

    public void setUploadDD(int uploadDD) {
        this.uploadDD = uploadDD;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
