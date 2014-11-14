package com.iMotion;

import com.iMotion.bubble.Bubble;
import com.iMotion.bubble.Emotion;
import com.iMotion.bubble.Reply;
import com.iMotion.database.Status;
import com.iMotion.database.User;
import com.iMotion.database.connectionToDatabase;

import java.util.ArrayList;

/**
 * Created by 逢双 on 14-2-18.
 */
public class Connect {

    private com.baidu.mapapi.map.MapView mMapView = null;
    private MapView.MyOverlay mOverlay = null;
    MapView activity;
    static connectionToDatabase connectionToDatabase = new connectionToDatabase();

    public Connect(com.baidu.mapapi.map.MapView mMapView, MapView.MyOverlay mOverlay, MapView activity){
        this.mMapView = mMapView;
        this.mOverlay = mOverlay;
        this.activity = activity;
    }
    public static boolean register(User user){
        try {
            connectionToDatabase.register(user);
            System.out.println("register over");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("register fail");
        }
        return false;
    }

    public static boolean login(String user, String password){
        User user1;
        try {
            user1 = connectionToDatabase.login(user, password);
            System.out.println("login over");
            return (user1 != null);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("login fail");
        }
        return false;
    }

    public static boolean addBubble(Status status){
        connectionToDatabase.addStatus(status);
        return false;
    }

    public static void addReply(Status status1, Status status2){
        connectionToDatabase.addcomment(status1, status2);
    }

    public void getBubble(){
        ArrayList<Bubble> bubble = MapView.bubbleManager.getBubbleArray();
        ArrayList<Reply> replies = MapView.bubbleManager.getDiscussArray();
        ArrayList<Status> comments;
        ArrayList<String> allName = connectionToDatabase.getAllName();
        int total = 0;
        for (int k = allName.size() - 1; k >= 0; k--) {
            ArrayList<Status> p = connectionToDatabase.getStatuslist(allName.get(k));
            for(int i = 0; i < p.size(); i++){
                bubble.add(new Bubble(p.get(i).text, new Emotion(p.get(i).emotion), p.get(i).place, p.get(i).locationX,
                        p.get(i).locationY, p.get(i).createdDate.toString() +"-"+ p.get(i).createdTime.toString()));
                MapView.bubbleManager.statusArray.add(p.get(i));
                replies.add(new Reply());
                comments = connectionToDatabase.getcommentlist(p.get(i));
                for (int j = 0; j < comments.size(); j++) {
                    replies.get(total).addReply(comments.get(j).username, comments.get(j).text, comments.get(j).place);
                }
                total++;
            }



            for (int i = 0; i < MapView.bubbleManager.getTotalNumber(); i++) {
                mOverlay.addItem(MapView.bubbleManager.getBubbleArray().get(i).getOverlayItem());
            }
            //执行地图刷新使生效
            mMapView.refresh();



        }
    }

    public static ArrayList<Status> refreshStatus(){
        return connectionToDatabase.getStatuslist(Login.USER);
    }

    public static void getReply(int index){
        ArrayList<Reply> replies = MapView.bubbleManager.getDiscussArray();
        ArrayList<Status> p = connectionToDatabase.getStatuslist(Login.USER);
        ArrayList<Status> comments;
        comments = connectionToDatabase.getcommentlist(p.get(index));
        for (int j = 0; j < comments.size(); j++) {
            replies.get(index).addReply(comments.get(j).username, comments.get(j).text, comments.get(j).place);
        }
    }
}
