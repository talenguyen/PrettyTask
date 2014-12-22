package com.tale.prettytask;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.tale.prettytask.functions.Func0;

/**
 * Created by tale on 12/20/14.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE) public class Task<T> extends AsyncTask<Object, Void, Object> {

    private final Func0<T> action;
    private OnResult<T> onResult;

    public Task(Func0<T> action, OnResult<T> onResult) {
        this.onResult = onResult;
        this.action = action;
    }

    @Override protected Object doInBackground(Object... params) {
        try {
            if (action instanceof EfficientFunc0) {
                final EfficientFunc0 efficientFunc0 = (EfficientFunc0) action;
                efficientFunc0.setTask(this);
            }
            return action.call();
        } catch (Exception e) {
            return e;
        }
    }

    @Override protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (isCancelled() || onResult == null) {
            return;
        }

        if (o != null && o instanceof Throwable) {
            onResult.onError((Throwable) o);
        } else {
            onResult.onSucceed(o == null ? null : (T) o);
        }

        onResult.onCompleted();
    }

    @Override protected void onCancelled() {
        super.onCancelled();
        onResult = null;
    }

    /**
     * Attempts to cancel execution of this task. This attempt will fail if the task has already
     * completed, already been cancelled, or could not be cancelled for some other reason. If successful,
     * and this task has not started when cancel is called, this task should never run. If the task
     * has already started, then the mayInterruptIfRunning parameter determines whether the thread
     * executing this task should be interrupted in an attempt to stop the task.
     *
     * @param mayInterruptIfRunning true if the thread executing this task should be interrupted;
     *                              otherwise, in-progress tasks are allowed to complete.
     * @see android.os.AsyncTask#cancel(boolean)
     */
    public void safeCancel(boolean mayInterruptIfRunning) {
        onResult = null;
        cancel(mayInterruptIfRunning);
    }
}
