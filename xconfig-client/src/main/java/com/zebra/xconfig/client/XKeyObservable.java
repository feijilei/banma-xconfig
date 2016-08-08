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
    private boolean isReady;

    XKeyObservable(){
        isReady = false;
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

    //todo 反向通知是否要在新的线程处理 目前不开新线程，如果使用者想要做某些比较重的操作，建议自己开新线程处理，zk能够保证频繁更新时候的最终一致性，开新线程需要自己处理一致性问题
    void change(String key,String value){
        if(!isReady){
            return;
        }

        List<XKeyObserver> observers = this.cacheMap.get(key);
        if (observers == null){
            return;
        }

        for(XKeyObserver observer : observers){
            observer.change(value);
        }
    }

    void ready(){
        this.isReady = true;
    }
}
