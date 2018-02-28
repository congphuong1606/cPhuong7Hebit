package com.discovertodo.phone.android.util;

import android.content.Context;

import com.discovertodo.phone.android.model.TagClassHtml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Ominext on 2/8/2018.
 */

public class JsonUtils {
    public static String getJson(Context activity) {
        String json = null;
        try {
            InputStream inputStream = activity.getAssets().open("ebook2018/config.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray getListJsonOject(String jsonConfig, String tag) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonConfig);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray lists;
            if (jsonObject != null) {
                lists = jsonObject.getJSONArray(tag);
                return lists;
            } else return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<TagClassHtml> getListObject(JSONArray lists) {
        if(lists!=null){
            ArrayList<TagClassHtml> list = new ArrayList<>();
            for (int j = 0; j < lists.length(); j++) {
                JSONObject tagClassHtml = null;
                try {
                    tagClassHtml = lists.getJSONObject(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (tagClassHtml != null) {
                        list.add(new TagClassHtml(tagClassHtml.getString("nameClass"), tagClassHtml.getInt("position")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }else return null;

    }
}
