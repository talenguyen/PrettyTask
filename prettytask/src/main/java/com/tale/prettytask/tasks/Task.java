package com.tale.prettytask.tasks;

/**
 * Created by TALE on 11/28/2014.
 */
public abstract class Task<Result> {

    public boolean isCancel() {
        return Thread.interrupted();
    }

    public abstract Result call();

}
