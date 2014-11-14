/**
 * Created by lenovo on 14-2-10.
 */
package com.iMotion.database;
import java.util.ArrayList;
public class User {

    long id=0;
    public String name;
    String password;
    public String sex;
    public java.sql.Date  registerDate;
    public   java.sql.Date lastTime;
    public  ArrayList<Status>   Statuslist ;

    public User(String name, String password, String sex){
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.registerDate = new java.sql.Date(new java.util.Date().getTime());
        this.lastTime = new java.sql.Date(new java.util.Date().getTime());
    }

    public User(){

    }





}
