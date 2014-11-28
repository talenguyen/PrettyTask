package com.tale.prettytask;

import android.os.Handler;
import android.os.Looper;

import com.tale.prettytask.tasks.Task;

import java.lang.ref.WeakReference;

class TaskRunnable<T> implements Runnable {
    private final WeakReference<OnResult<T>> mOnResultPref;
    private final Task<T> mTask;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private Thread mThread;

    TaskRunnable(Task<T> task, WeakReference<OnResult<T>> onResultPref) {
        this.mTask = task;
        this.mOnResultPref = onResultPref;
    }

    @Override public void run() {
        mThread = Thread.currentThread();
        try {
            T result = null;
            // Just handle error for our job. Not for all.
            try {
                result = mTask.call();
            } catch (Throwable e) {
                postError(e);
            }
            if (result != null) {
                postSuccess(result);
            }
        } finally {
            postCompleted();

            // Sets the current Thread to null, releasing its storage
            mThread = null;

            // Clears the Thread's interrupt flag
            Thread.interrupted();

        }
    }

    private void postSuccess(final T result) {
        if (mOnResultPref == null) {
            return;
        }

        final OnResult<T> onSuccess = mOnResultPref.get();
        if (onSuccess != null) {
            mMainHandler.post(
                    new Runnable() {
                        @Override public void run() {
                            onSuccess.onSucceed(result);
                        }
                    }
            );
        }
    }

    private void postCompleted() {
        if (mOnResultPref == null) {
            return;
        }

        final OnResult<T> onCompleted = mOnResultPref.get();
        if (onCompleted != null) {
            mMainHandler.post(
                    new Runnable() {
                        @Override public void run() {
                            onCompleted.onCompleted();
                        }
                    }
            );
        }
    }

    private void postError(final Throwable e) {
        if (mOnResultPref != null) {
            final OnResult<T> onError = mOnResultPref.get();
            if (onError != null) {
                mMainHandler.post(
                        new Runnable() {
                            @Override public void run() {
                                onError.onError(e);
                            }
                        }
                );
            }
        } else {
            throw new RuntimeException("onError is not implemented but error occurred: " + e.getMessage());
        }

    }

    public void cancel() {
        if (mThread != null) {
            mThread.interrupt();
        }
    }
}