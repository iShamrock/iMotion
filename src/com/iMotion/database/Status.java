package com.iMotion.database;
import java.util.Date;

/**
 * Created by lenovo on 14-2-10.
 */
public class Status {
    public    int number;
    public    String text;
    public    String emotion;
    public    Date createdDate;
    public    java.sql.Time createdTime;
    public    String username;
    public    Double locationX = new Double(15);
    public    Double locationY = new Double(15);
    public    String place;
    //  URL[] image[];
    public Status ( String text,String emotion,
             String username, Double locationX,  Double locationY, String place){

        this.text=text;
        this.emotion=emotion;
        this.username=username;
        this.locationX =locationX;
        this.locationY =locationY;
        this.place=place;
        //     this.createdTime=System.currentTimeMillis()/1000;


    }

    Status (){

    }



}
