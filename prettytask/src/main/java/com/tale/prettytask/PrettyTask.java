package com.tale.prettytask;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.tale.prettytask.functions.Func0;
import com.tale.prettytask.functions.Func1;

/**
 * A wrapper of AsyncTask. Help to build async task more pretty
 */
public class PrettyTask<T> {

    private Func0<T> func;
    private OnResult<T> result;

    private PrettyTask(Func0<T> func) {
        this.func = func;
    }

    public static <T> PrettyTask<T> create(Func0<T> func) {
        return new PrettyTask<T>(func);
    }

    public PrettyTask<T> onResult(OnResult<T> result) {
        this.result = result;
        return this;
    }

    public <R> PrettyTask<R> map(final Func1<T, R> func1) {
        return new PrettyTask(new Func0<R>() {
            @Override public R call() throws Exception {
                return func1.call(func.call());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE) public Task<T> execute() {
        final Task<T> task = new Task<T>(func, result);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
        return task;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) public Task<T> executeSerial() {
        final Task<T> task = new Task<T>(func, result);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        return task;
    }

}
