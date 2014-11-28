package com.tale.prettytask;

/**
 * Created by TALE on 11/28/2014.
 */
public interface OnResult<T> {

    void onSucceed(T result);

    void onError(Throwable throwable);

    void onCompleted();

}
