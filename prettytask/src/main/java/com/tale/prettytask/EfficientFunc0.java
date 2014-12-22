package com.tale.prettytask;

import android.annotation.TargetApi;
import android.os.Build;

import com.tale.prettytask.Task;
import com.tale.prettytask.functions.Func0;

/**
 * Implementation of {@link com.tale.prettytask.functions.Func0} and supported isCanceled() method to check
 * if task is canceled then can stop do in background task ASAP.
 */
public class EfficientFunc0<T> implements Func0<T> {
    private Task<T> task;

    @Override public T call() {
        return null;
    }

    /**
     * Returns true if this task was cancelled before it completed normally
     * @return true if task was cancelled before it completed
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE) public boolean isCanceled() {
        if (task != null) {
            return task.isCancelled();
        } else {
            return false;
        }
    }

    void setTask(Task<T> task) {
        this.task = task;
    }

}
