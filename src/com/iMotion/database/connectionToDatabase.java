package com.iMotion.database;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by lenovo on 14-2-10.
 */
public class connectionToDatabase implements CTDinterface   {
    Socket connect;
    OutputStream output;
    InputStream input;
    Connection conn;
    Statement sm;
    String tableName="userinfo";
    public connectionToDatabase()  {
 /*       connect=new Socket();
    try {
         output=connect.getOutputStream();
         input=connect.getInputStream();
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
         conn =getconnection();
        try{
           sm=conn.createStatement();



        }catch(SQLException e){
            System.out.print(e);;
        }
    } catch (InstantiationException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();

*/

    }



    @Override
    public void register(User user1) throws ClassNotFoundException {

        String sql="insert into "+tableName+"(name,password,sex,registedate,lastTime) values('"+user1.name+"','"+user1.password+"','"+user1.sex+"','"+user1.registerDate
                +"','"+user1.lastTime +"');";

        if(!checkname(user1.name))       return;//if the name already existed
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);

            //     PreparedStatement ps=conn.prepareStatement(sql);
            //   ps.executeUpdate(sql);
            // con.commit();


            Statement stmt=con.createStatement();
            //  ps.executeUpdate(sql);
            stmt.executeUpdate(sql);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        createuserStatusDB(user1.name);




    }

    private void createuserStatusDB(String name ) {
        int id=getstatusDB(name);
        String DBname="userStatusDB"+id;

        System.out.println(getstatusDB(name));
        String sql="create table "+DBname+" (num int auto_increment,name varchar(30),emotion varchar(50),text varchar(200),place varchar(200),location_x double,location_y double,time datetime ,primary key(num));";
        System.out.println(sql);
        try {  Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            Statement sm = con.createStatement();

            con.setAutoCommit(false);
            java.sql.PreparedStatement ps=con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(String name, String password) {
        String sql="select * from "+tableName+" where name='"+name+"' and "+"password='"+password+"';";



        ResultSet a=null;





        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            Statement ps=con.createStatement();
            //  ps.executeUpdate(sql);
            a=ps.executeQuery(sql);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




        try {
            if(a.next() )  {
                User user = new User();
                user.id=a.getLong(1);
                user.name=a.getString(2);
                user.password=a.getString(3);
                user.sex=a.getString(4);
                user.registerDate=a.getDate(5);
                user.lastTime =a.getDate(6);
                // user.Emotionlist= (ArrayList) a.getObject(7);

                return user;


            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updatetime(name);
        return null;




    }

    @Override
    public ArrayList<Status> getStatuslist(String name) {
        System.out.print( getstatusDB(name));
        int id=getstatusDB(name);
        String DBname="userStatusDB"+id;

        //String sql="select * from "+DBname+";";

        String sql="select * from "+DBname+" where num between "+0 +" and "+10000+";";
        System.out.println(sql);

        ResultSet a=null;

        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            //   Statement ps=conn.getStatement(sql);
            Statement ps=con.createStatement();
            //  ps.executeUpdate(sql);
            a=ps.executeQuery(sql);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Status> p=new ArrayList<Status>();
        try {
            while(a.next()){
                Status e=new Status();
                e.number =a.getInt(1);
                e.username=a.getString(2);
                e.emotion=a.getString(3);
                e.text=a.getString(4);
                e.place=a.getString(5);
                e.locationX =a.getDouble(6);
                e.locationY =a.getDouble(7);
                e.createdDate=a.getDate(8);
                e.createdTime=a.getTime(8);

                p.add(e);




            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("nullPointer 238");
        }


        return p;
    }



    private int getstatusDB(String name){

        int id;

        String sql="select "+"id"+" from "+tableName+" where name= '"+name+"';";

        System.out.println(sql);

        ResultSet a=null;


        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            Statement ps=con.createStatement();
            //  ps.executeUpdate(sql);
            a=ps.executeQuery(sql);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




        try {
            if(a.next() )  {
                return id=a.getInt(1);
                // String databasename="userStatusDB"+id;
                //  databasename ;// System.out.println(databasename);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }












        return 0;


    }
    @Override
    public boolean checkname(String name)  {
        //链接到远端数据库 检查名字




        ResultSet a = null;
        String sql="select * from "+tableName+" where name='"+name+"';";
        System.out.println(sql);
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            //   Statement ps=conn.getStatement(sql);
            Statement ps=con.createStatement();
            //  ps.executeUpdate(sql);
            a=ps.executeQuery(sql);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




        try {
            if(! a.next() )  return  true;// if the name cannot be find in the database , then the register is legal
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ArrayList addStatus(Status newStatus) {

        int id=getstatusDB(newStatus.username);
        String DBname="userStatusDB"+id;
        String sql="INSERT INTO "+DBname+" (`name`, `emotion`, `text`, `place`, `location_x`, `location_y`, `time`) VALUES ('"+newStatus.username+"','"+newStatus.emotion+"','"+newStatus.text+"','"+newStatus.place+"','"+newStatus.locationX +"','"+newStatus.locationY +"',now());";
        System.out.print(sql);
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);

            //     PreparedStatement ps=conn.prepareStatement(sql);
            //   ps.executeUpdate(sql);
            // con.commit();


            Statement stmt=con.createStatement();
            //  ps.executeUpdate(sql);
            stmt.executeUpdate(sql);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return getStatuslist(newStatus.username);
    }

    @Override
    public void updatetime(String name) {

        int id=getstatusDB(name);
        String DBname="userStatusDB"+id;
        String sql="UPDATE `userinfo` SET `lastTime`=now() WHERE name='"+name+"';";
        //  String sql="INSERT INTO "+DBname+" (`name`, `emotion`, `text`, `place`, `location_x`, `location_y`, `time`) VALUES ('"+newStatus.username+"','"+newStatus.emotion+"','"+newStatus.text+"','"+newStatus.place+"','"+newStatus.locationX+"','"+newStatus.locationY+"',now());";
        System.out.print(sql);
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);

            //     PreparedStatement ps=conn.prepareStatement(sql);
            //   ps.executeUpdate(sql);
            // con.commit();


            Statement stmt=con.createStatement();
            //  ps.executeUpdate(sql);
            stmt.executeUpdate(sql);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    public static Connection getconnection(){


        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        try {

            String databaseName = "imotion";
            String host = "imotionssl.mysql.rds.aliyuncs.com";
            String port = "3306";
            String username = "imotionspring";      //用户名(api key);PwOI149n9fNnjMUV5Zw0kQvN
            String password = "imotion2014";//密码(secret key)
            String driverName = "com.mysql.jdbc.Driver";
            String dbUrl = "jdbc:mysql://";
            String serverName = host + ":" + port + "/";
            String connName = dbUrl + serverName + databaseName;


            Class.forName(driverName);
            connection = DriverManager.getConnection(connName, username, password);
            return connection;

        }

        catch (Exception ex) {
            System.err.print(ex);
            System.out.println("null");
            ex.printStackTrace();

        }

        return null;
    }

    public  ArrayList addcomment(Status status,Status comment){
        int num=status.number;

        int id=getstatusDB(comment.username);
        int length;
        try{
            length=getcommentlist(status).size();

        }catch (Exception e){
            length =0;


        }
        String DBname="userStatusDB"+id;
        String sql="INSERT INTO "+DBname+" (`num`,`name`, `emotion`, `text`, `place`, `location_x`, `location_y`, `time`) VALUES ('"+(num*10000+length)+"','"+comment.username+"','"+comment.emotion+"','"+comment.text+"','"+comment.place+"','"+comment.locationX +"','"+comment.locationY +"',now());";
        System.out.print(sql);
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);

            //     PreparedStatement ps=conn.prepareStatement(sql);
            //   ps.executeUpdate(sql);
            // con.commit();


            Statement stmt=con.createStatement();
            //  ps.executeUpdate(sql);
            stmt.executeUpdate(sql);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }





        return  null;


    }

    public ArrayList getAllName(){
        ResultSet a=null;
        String sql="select name from userinfo;";
        System.out.println(sql);
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            //   Statement ps=conn.getStatement(sql);
            Statement ps=con.createStatement();
            //  ps.executeUpdate(sql);
            a=ps.executeQuery(sql);
            con.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ArrayList<String> p=new ArrayList();
        try{
            while(a.next()){
                p.add(a.getString(1));

            }
            return  p;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;


    }


    public  ArrayList getcommentlist(Status status){


        //   System.out.print( getstatusDB(name));
        int id=getstatusDB(status.username);
        String DBname="userStatusDB"+id;
        int i=status.number;
        //  String sql="select * from "+DBname+";";
        String sql="select * from "+DBname+" where num between "+i*10000 +" and "+(i+1)*10000+";";

        System.out.println(sql);

        ResultSet a=null;

        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con=getconnection();
            con.setAutoCommit(false);
            //   Statement ps=conn.getStatement(sql);
            Statement ps=con.createStatement();
            //  ps.executeUpdate(sql);
            a=ps.executeQuery(sql);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Status> p=new ArrayList();
        try {
            while(a.next()){
                Status e=new Status();
                e.number =a.getInt(1);
                e.username=a.getString(2);
                e.emotion=a.getString(3);
                e.text=a.getString(4);
                e.place=a.getString(5);
                e.locationX =a.getDouble(6);
                e.locationY =a.getDouble(7);
                e.createdDate=a.getDate(8);
                e.createdTime=a.getTime(8);

                p.add(e);




            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return p;


    }
}



