package com.iMotion.bubble;

import com.iMotion.database.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by 逢双 on 14-2-10.
 */
public class BubbleManager{
    private ArrayList<Bubble> bubbleArray = new ArrayList<Bubble>();
    private ArrayList<Reply> discussArray = new ArrayList<Reply>();
    public ArrayList<Status> statusArray = new ArrayList<Status>();
    /*private Bubble[] bubbleArray = new Bubble[1000];//暂且使用数组
    private Reply[] discussArray = new Reply[1000];*/
    private int totalNumber;

    public BubbleManager(){

    }

    public void addBubble(Bubble bubble){
        bubbleArray.add(bubble);
        discussArray.add(new Reply());
    }

    /*save和load暂时废弃*/
    public void saveBubble(){
        //最终应当保存至服务器
        try {
//            String foldername = Environment.getExternalStorageDirectory().getPath() + "bubble.txt";
            FileWriter fw = new FileWriter(new File("/sdcard/bubble.txt"));
            for(int i = 0; i < totalNumber; i++){
                fw.write(bubbleArray.get(i).getMessage() + "\r\n" + bubbleArray.get(i).getLocation() + "\r\n"
                        + bubbleArray.get(i).getLatitude() + "\r\n" + bubbleArray.get(i).getLongitude() + "\r\n");
            }
            fw.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("bubble save failed");
        }
    }

    public void loadBubble(){
        //最终应当通过服务器获取
        try {
//            String foldername = Environment.getExternalStorageDirectory().getPath() + "bubble.txt";
            Scanner scanner = new Scanner(new File("/sdcard/bubble.txt"));
            String message;
            String location;
            double latitude;
            double longitude;
            while (scanner.hasNext()){
                message = scanner.nextLine();
                location = scanner.nextLine();
                latitude = Double.parseDouble(scanner.nextLine());
                longitude = Double.parseDouble(scanner.nextLine());
            //    addBubble(new Bubble(message, Location_Geo, latitude, longitude));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("bubble load failed");
        }
    }

    public ArrayList<Bubble> getBubbleArray() {
        return bubbleArray;
    }

    public int getTotalNumber() {
        return bubbleArray.size();
    }

    public ArrayList<Reply> getDiscussArray() {
        return discussArray;
    }
}
