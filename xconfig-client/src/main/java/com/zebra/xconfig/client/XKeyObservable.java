package com.zebra.xconfig.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ying on 16/7/25.
 */
public class XKeyObservable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String,List<XKeyObserver>> cacheMap = new ConcurrentHashMap<String, List<XKeyObserver>>();

    XKeyObservable(){
    }

    public void addObserver(XKeyObserver observer){
        String key = observer.getKey();
        if(cacheMap.get(key) == null){
            cacheMap.put(key,new LinkedList<XKeyObserver>());
        }

        this.cacheMap.get(key).add(observer);
    }

    public void removeObserver(XKeyObserver observer){
        String key = observer.getKey();
        if(cacheMap.get(key) != null){
            cacheMap.get(key).remove(observer);
        }else{
        }
    }

    void change(String key,String value){
        List<XKeyObserver> observers = this.cacheMap.get(key);
        if (observers == null){
            return;
        }

        for(XKeyObserver observer : observers){
            observer.change(value);
        }
    }
}
