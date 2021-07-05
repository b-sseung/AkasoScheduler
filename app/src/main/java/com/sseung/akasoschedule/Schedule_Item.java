package com.sseung.akasoschedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Schedule_Item implements Parcelable {

    String division;
    String detail_division;
    String name;
    String time;
    String image;
    String uri;
    String sale;
    String source;

    int year, month, day;

    public Schedule_Item(){
        // Default constructor required for calls to DataSnapshot.getValue(Schedule_Item.class)
    }
    public Schedule_Item(int year, int month, int day, String division, String detail_division, String name, String time, String image, String uri, String sale, String source){
        this.year = year;  //년
        this.month = month;  //월
        this.day = day;  //일
        this.division = division;  //구분 ex) 미디어, 방송..
        this.detail_division = detail_division;  //상세 구분 ex) 사진, 잡지, 드라마, 예능, 라디오 등등
        this.name = name;  //이름
        this.time = time;  //시간
        this.image = image;  //이미지
        this.uri = uri;  //uri
        this.sale = sale;  //판매처
        this.source = source;  //출처
    }

    protected Schedule_Item(Parcel in) {
        division = in.readString();
        detail_division = in.readString();
        name = in.readString();
        time = in.readString();
        uri = in.readString();
        sale = in.readString();
        source = in.readString();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        image = in.readString();
    }

    public static final Creator<Schedule_Item> CREATOR = new Creator<Schedule_Item>() {
        @Override
        public Schedule_Item createFromParcel(Parcel in) {
            return new Schedule_Item(in);
        }

        @Override
        public Schedule_Item[] newArray(int size) {
            return new Schedule_Item[size];
        }
    };

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

    public void setDivision(String division){
        this.division = division;
    }

    public String getDivision(){
        return this.division;
    }

    public void setDetail(String detail_division){
        this.detail_division = detail_division;
    }

    public String getDetail(){
        return this.detail_division;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return this.time;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return this.image;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

    public String getUri(){
        return this.uri;
    }

    public void setSale(String sale){
        this.sale = sale;
    }

    public String getSale(){
        return this.sale;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.year);
        dest.writeString(this.detail_division);
        dest.writeString(this.name);
        dest.writeString(this.time);
        dest.writeString(this.image);
        dest.writeString(this.uri);
        dest.writeString(this.sale);
        dest.writeString(this.source);
    }
}
