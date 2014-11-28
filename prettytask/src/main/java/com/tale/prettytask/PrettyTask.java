package com.tale.prettytask;

import com.tale.prettytask.tasks.Task;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/**
 * Created by TALE on 11/28/2014.
 */
public class PrettyTask<T> {

    private final Task<T> mTask;

    private Executor mExecutor;
    private TaskRunnable mTaskRunnable;
    private boolean isPaused;
    private WeakReference<OnResult<T>> mOnResultPref;

    private PrettyTask(Task<T> task) {
        this.mTask = task;
    }

    public static <T> PrettyTask<T> create(Task<T> task) {
        return new PrettyTask<T>(task);
    }

    public PrettyTask<T> onResult(OnResult<T> onResult) {
        if (onResult == null) {
            throw new IllegalArgumentException("onResult can not be null");
        }
        this.mOnResultPref = new WeakReference<OnResult<T>>(onResult);
        return this;
    }

    public PrettyTask<T> execute() {
        execute(Executors.THREAD_POOL_EXECUTOR);
        return this;
    }

    public PrettyTask<T> execute(Executor executor) {
        this.mExecutor = executor;
        start();
        return this;
    }

    public PrettyTask<T> executeSerial() {
        execute(Executors.SERIAL_EXECUTOR);
        return this;
    }

    void start() {
        if (mExecutor != null && mTask != null) {
            mTaskRunnable = new TaskRunnable<T>(mTask, mOnResultPref);
            mExecutor.execute(mTaskRunnable);
        }
    }

    public void pause() {
        if (mTaskRunnable != null) {
            mTaskRunnable.cancel();
            isPaused = true;
        }
    }

    public void resume() {
        if (isPaused) {
            start();
        }
    }

    public void stop() {
        if (mTaskRunnable != null) {
            mTaskRunnable.cancel();
        }

        isPaused = false;

        release();
    }

    private void release() {
        mExecutor = null;
        mTaskRunnable = null;
        java.lang.System.gc(); // Tells the system that garbage collection is necessary.
    }
}
