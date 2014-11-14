package com.iMotion.database;
import java.util.ArrayList;

/**
 * Created by lenovo on 14-2-10.
 */
public interface CTDinterface  {
public void  register(User user1) throws ClassNotFoundException;
public User login(String name,String password);
public ArrayList  getStatuslist(String name);
public boolean checkname(String name) ;
public ArrayList addStatus(Status newStatus);
public void updatetime(String name);







}
