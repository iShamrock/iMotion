package com.iMotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iMotion.bubble.Bubble;
import com.iMotion.bubble.BubbleManager;
import com.iMotion.bubble.Reply;
import com.iMotion.database.Status;
import com.iMotion.location.Location_Geo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 逢双 on 14-2-3.
 */
public class Discussion extends Activity {

    BubbleManager bubbleManager = MapView.bubbleManager;
    private ListView listView;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Bubble bubble;
    private Reply replyArray;
    private EditText replyMessage;
    private SimpleAdapter adapter;
    Intent intent = new Intent();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discuss);

        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        bubble = bubbleManager.getBubbleArray().get(position);
        replyArray = bubbleManager.getDiscussArray().get(position);

        String msg = bubble.getMessage();
        TextView t_msg = (TextView) findViewById(R.id.bubbleContent_discuss);
        t_msg.setText(msg);

        String location = bubble.getLocation();
        TextView t_location = (TextView) findViewById(R.id.loc_discuss);
        t_location.setText(location);

        String time = bubble.getTime();
        TextView t_time = (TextView) findViewById(R.id.time_discuss);
        t_time.setText(time);

        replyMessage = (EditText) findViewById(R.id.addDiscuss_discuss);

        listView = (ListView) findViewById(R.id.discussContent_discuss);
        adapter = new SimpleAdapter(this, getTripListData(),
                R.layout.listview_discussion, new String[]{"msg", "loc", "time"},
                new int[]{R.id.msg_reply, R.id.loc_reply, R.id.time_reply});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replyMessage.setText("回复 " + replyArray.getReplyUser().get(position) + ": ");
            }
        });

        Button replyButton = (Button) findViewById(R.id.discussion_comment_button);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyArray.addReply("UserXXX", replyMessage.getText().toString(), Location_Geo.getLocation());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("msg", replyMessage.getText().toString());
                map.put("loc", Location_Geo.getLocation());
                map.put("time", new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis())));
                list.add(map);
                adapter.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double latitude = (double) Location_Geo.getCurrentGeoPoint().getLatitudeE6() / 1e6;
                        double longtitude = (double) Location_Geo.getCurrentGeoPoint().getLongitudeE6() / 1e6;
                        Connect.addReply(bubbleManager.statusArray.get(position), new Status(replyMessage.getText().toString(),
                                "lalalala", Login.USER, latitude, longtitude, Location_Geo.getLocation()));
                    }
                }).start();
                replyMessage.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public List<Map<String, Object>> getTripListData() {

        Map<String, Object> map;
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < replyArray.getReplyNumber(); i++) {
            map = new HashMap<String, Object>();
            map.put("msg", replyArray.getReplyMessage().get(i));
            map.put("loc", replyArray.getReplyLocation().get(i));
            map.put("time", replyArray.getReplyTime().get(i));
            list.add(map);
        }
        return list;
    }
}
