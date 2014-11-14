package com.iMotion.bubble;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 逢双 on 14-2-12.
 */
public class Reply {
    private ArrayList<String> replyUser= new ArrayList<String>();
    private ArrayList<String> replyMessage= new ArrayList<String>();
    private ArrayList<String> replyLocation= new ArrayList<String>();
    private ArrayList<String> replyTime= new ArrayList<String>();

    public Reply(){
   //     addReply("lfs", "这是条测试用的回复=_=", "某地");
    }

    public void addReply(String user, String msg, String location){
        replyUser.add(user);
        replyMessage.add(msg);
        replyLocation.add(location);
        replyTime.add(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis())));
    }

    public ArrayList<String> getReplyMessage() {
        return replyMessage;
    }

    public int getReplyNumber() {
        return replyMessage.size();
    }

    public ArrayList<String> getReplyUser() {
        return replyUser;
    }

    public ArrayList<String> getReplyLocation() {
        return replyLocation;
    }

    public ArrayList<String> getReplyTime() {
        return replyTime;
    }
}
