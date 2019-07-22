package com.zhongchuang.canting;

import android.util.Log;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    List<Map<String ,Object>> outputMapList;
    @Test
    public void addition_isCorrect() {
        Long aLong = Long.valueOf("05");
        Log.e("log",aLong+"");

    }
private Map<String,Long> mp=new HashMap<>();
    private String content="";
 public void setValuePercent(List<Map<String,Object>> outputMapList){
        for(Map<String,Object> data:outputMapList){
            for(Map.Entry<String,Object> entry:data.entrySet()){
                Long valueData = mp.get(entry.getKey());
                if(!entry.getKey().equals("cusValue")){
                    if(valueData==null){
                        mp.put(entry.getKey(), (Long) entry.getValue());
                    }else {
                        if(entry.getKey().equals("value23")){
                            content=content+"+"+(Long)entry.getValue();
                        }
                        mp.put(entry.getKey(),valueData+(Long) entry.getValue());
                    }
                }
            }
        }
     DecimalFormat df=new DecimalFormat("0.00");
     for(Map<String,Object> data:outputMapList){
         for(Map.Entry<String,Object> entry:data.entrySet()){
             Long totalData = mp.get(entry.getKey());
             if(!entry.getKey().equals("cusValue")){
                 data.put(entry.getKey(),totalData==0?0:df.format(((Long)entry.getValue()/(totalData*1.0))*100));
             }
         }
         data.put("content",content);
         data.put("total",mp.get("value23")==null?0:mp.get("value23"));
     }

 }



}