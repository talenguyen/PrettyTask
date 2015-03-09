package com.tale.prettytask.task;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tale on 3/9/15.
 */
public class Tasks implements Observer<Object> {
    private final Map<String, Observer> observerMap = new LinkedHashMap<String, Observer>();
    private final Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
    private String activeKey;
    private boolean isRunning;

    public void register(String key, Observer observer) {
        if (key == null || observer == null) {
            throw new NullPointerException("key or observer must not be null");
        }

        if (isRunning && key.equals(activeKey)) {
            observer.onBeforeResult();
        }
        observerMap.put(key, observer);
        activeKey = key;
    }

    public void unRegister(String key) {
        if (key == null) {
            return;
        }
        observerMap.remove(key);
    }

    @Override public void onBeforeResult() {
        if (activeKey == null) {
            return;
        }
        final Observer observer = observerMap.get(activeKey);
        observer.onBeforeResult();
        isRunning = true;
    }

    @Override public void onResult(Object o) {
        if (activeKey == null) {
            return;
        }
        final Observer observer = observerMap.get(activeKey);
        if (observer == null) {
            resultMap.put(activeKey, o);
        } else {
            observer.onResult(o);
        }
    }

    @Override public void onError(Throwable throwable) {
        if (activeKey == null) {
            return;
        }
        final Observer observer = observerMap.get(activeKey);
        if (observer != null) {
            observer.onError(throwable);
        }
    }
}
