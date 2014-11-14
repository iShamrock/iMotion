package com.iMotion.database;
import java.util.ArrayList;

/**
 * Created by lenovo on 14-2-13.
 */
public class CTDBtest {
    public static void  main(String[] args){
        connectionToDatabase ctdb=new connectionToDatabase();


          System.out.println("checkname result of harry is " + ctdb.checkname("Harry"));

          // checkname()  检测该用户名是否已被占用


            //下面是注册一个用户的演示

        ctdb.updatetime("eric7");
        User eric = new User();
        eric.name="admin";          //填写基本的用户信息   用户名 密码 性别
        eric.password="admin";
        eric.sex="male";
        eric.registerDate= new java.sql.Date(new java.util.Date().getTime());
        eric.lastTime = new java.sql.Date(new java.util.Date().getTime());

        try {
            ctdb.register(eric);              //register（USER user）完成注册  调用该方法后， 用户数据就入库了
            System.out.println("register over");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("register fail");//在用户名重名的情况下 不会注册成功
        }







        User user=ctdb.login("admin","admin");  //调用login(name,password)登陆 返回一个User的实例 User中含有用户的基本信息
        if(user!=null){

            System.out.println(user.id);
        System.out.println(user.name);
        System.out.println(user.password);
        System.out.println(user.sex);
        System.out.println(user.lastTime);
        System.out.println(user.registerDate);

        }
        ctdb.getStatuslist("admin");






        Status happy= new Status("wowee!","happy","eric7",123.1233,12413.2321,"hollw");
        //创建一个状态  需要填写 （状态文字、心情、用户名、地理信息）

        ctdb.addStatus(happy); //调用方法 状态就进入数据库



        ArrayList g=ctdb.getStatuslist("eric7");//调用getStatuslist（用户名）就可以获得该用户的所有状态列表；
        //下面演示的是从获取的arraylist中逐个打印Status
        for(int i=0;i<g.size();i++){


           Status newstatus = (Status) g.get(i);

            System.out.println("start print newstatus");
            System.out.println( newstatus.number);
            System.out.println(newstatus.emotion);
            System.out.println( newstatus.text);
            System.out.print(newstatus.createdDate+"  ");
            System.out.println(newstatus.createdTime);

        }





        Status commt= new Status("hohoe!","happy","eric7",123.1233,12413.2321,"hollw");//创建一个评价 格式和Status是一样的

        ctdb.addcomment((Status) g.get(2),commt);//添加点评必须要传入2个status实例  第一个是被点评的实例 第二个是点评的内容
            //被点评的实例请从上面的getarraylist返回的函数中取出
            //  直接创建的Status会因为信息不完整报错！！另外Status的nummber由数据库创建。不能擅自改动。


       //下面演示的是打印某个Stauts 的点评 （打印Status本身差不多）
        ArrayList commtes=ctdb.getcommentlist((Status) g.get(2));
        for(int i=0;i<commtes.size();i++){


            Status newstatus = (Status) commtes.get(i);

            System.out.println("start print new status");
            System.out.println( newstatus.number);
            System.out.println(newstatus.emotion);
            System.out.println( newstatus.text);
            System.out.print(newstatus.createdDate+"  ");
            System.out.println(newstatus.createdTime);

        }

    }





}
