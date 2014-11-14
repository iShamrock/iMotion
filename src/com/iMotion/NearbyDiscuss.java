package com.iMotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.iMotion.bubble.BubbleManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 逢双 on 14-2-10.
 */
public class NearbyDiscuss extends Activity {

    BubbleManager bubbleManager = MapView.bubbleManager;
    private ListView listView;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearbydiscuss);

        listView = (ListView) findViewById(R.id.listView_NearbyDiscuss);
        SimpleAdapter adapter = new SimpleAdapter(this, getTripListData(),
                R.layout.listview_nearbydiscuss, new String[]{"img", "msg", "Location_Geo", "user", "time"},
                new int[]{R.id.img_nbd, R.id.msg_nbd, R.id.loc_nbd, R.id.user_nbd, R.id.time_nbd});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("position", position);
                intent.setClass(NearbyDiscuss.this, Discussion.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public List<Map<String, Object>> getTripListData() {

        Map<String, Object> map;
        for (int i = 0; i < bubbleManager.getTotalNumber(); i++) {

            map = new HashMap<String, Object>();
            map.put("img", R.drawable.ic_launcher);
            map.put("msg", bubbleManager.getBubbleArray().get(i).getMessage());
            map.put("Location_Geo", bubbleManager.getBubbleArray().get(i).getLocation());
            map.put("user", bubbleManager.getBubbleArray().get(i).getUser());
            map.put("time", changeTimeString(bubbleManager.getBubbleArray().get(i).getTime()));
            list.add(map);
        }
        return list;
    }

    private String changeTimeString(String primitiveTime) {
        String tokens[] = primitiveTime.split("-|:", 6);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(tokens[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(tokens[1]));
        calendar.set(Calendar.DATE, Integer.parseInt(tokens[2]));
        calendar.set(Calendar.HOUR, Integer.parseInt(tokens[3]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(tokens[4]));
        calendar.set(Calendar.SECOND, Integer.parseInt(tokens[5]));
        long interval = System.currentTimeMillis() - calendar.getTimeInMillis();
        if (interval / 1000 % 60 <= 5) {
            return "moments ago";
        }
        if (interval / 1000 / 60 % 60 < 1) {
            return interval / 1000 / 60 + "minutes ago";
        }
        if (interval / 1000 / 60 %60 < 12) {
            return interval / 1000 / 60 / 60 + "hours ago";
        }
        if (interval / 1000 / 60 / 60 % 24 < 1) {
            return " Earlier today";
        }
        if (interval / 1000 / 60 / 60 % 24 < 7) {
            return interval / 1000 / 60 / 60 / 24 + "days ago";
        }
        if (interval / 1000 / 60 / 60 / 24 < 365) {
            return tokens[1] + "-" + tokens[2];
        }
        return tokens[0] + "-" + tokens[1] + "-" + tokens[2];
    }
}
