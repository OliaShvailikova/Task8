package com.example.home.myapplicati;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ReadJSON {
    private Context context;
    private JSONArray jsonArray;
    public ReadJSON(Context context){
        this.context=context;
    }

    JSONArray readJson(){
        InputStream inputStream=context.getResources().openRawResource(R.raw.category);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        try {
            ctr=inputStream.read();
            while (ctr!=-1){
                byteArrayOutputStream.write(ctr);
                ctr=inputStream.read();
            }
            inputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject=new JSONObject(byteArrayOutputStream.toString());
            jsonArray=jsonObject.getJSONArray("Category");
            return jsonArray;

        } catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }

}
