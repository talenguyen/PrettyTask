package com.tale.prettytask;

import android.os.Handler;
import android.os.Looper;

import com.sun.istack.internal.NotNull;
import com.tale.prettytask.functions.Action0;
import com.tale.prettytask.functions.Action1;
import com.tale.prettytask.functions.Function0;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

/**
 * Created by TALE on 11/27/2014.
 */
class TaskHandlerIml<T> implements TaskHandler {
    private WeakReference<Action1<T>> mOnSuccess;
    private WeakReference<Action1<Throwable>> mOnError;
    private WeakReference<Action0> mOnCompleted;

    private Executor mExecutor;
    private Function0<T> mFunction;

    private boolean isPaused;
    private Thread mThread;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    TaskHandlerIml(Executor executor, Function0<T> function, @NotNull Action1<T> onSuccess, Action1<Throwable> onError, Action0 onCompleted) {
        this.mExecutor = executor;
        this.mFunction = function;
        this.mOnSuccess = new WeakReference<Action1<T>>(onSuccess);
        if (onError != null) {
            this.mOnError = new WeakReference<Action1<Throwable>>(onError);
        }

        if (onCompleted != null) {
            this.mOnCompleted = new WeakReference<Action0>(onCompleted);
        }
    }

    void start() {
        if (mExecutor != null && mFunction != null) {
            mExecutor.execute(
                    new Runnable() {
                        @Override public void run() {
                            try {
                                mThread = Thread.currentThread();
                                T result = null;
                                // Just handle error for our job. Not for all.
                                try {
                                    result = mFunction.call();
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
                    }
            );
        }
    }

    private void postSuccess(final T result) {
        if (mOnSuccess == null) {
            return;
        }

        final Action1<T> onSuccess = mOnSuccess.get();
        if (onSuccess != null) {
            mMainHandler.post(
                    new Runnable() {
                        @Override public void run() {
                            onSuccess.call(result);
                        }
                    }
            );
        }
    }

    private void postCompleted() {
        if (mOnCompleted == null) {
            return;
        }

        final Action0 onCompleted = mOnCompleted.get();
        if (onCompleted != null) {
            mMainHandler.post(
                    new Runnable() {
                        @Override public void run() {
                            onCompleted.call();
                        }
                    }
            );
        }
    }

    private void postError(final Throwable e) {
        if (mOnError != null) {
            final Action1<Throwable> onError = mOnError.get();
            if (onError != null) {
                mMainHandler.post(
                        new Runnable() {
                            @Override public void run() {
                                onError.call(e);
                            }
                        }
                );
            }
        } else {
            throw new RuntimeException("onError is not implemented but error occurred: " + e.getMessage());
        }

    }

    @Override public void pause() {
        if (mThread != null) {
            mThread.interrupt();
            isPaused = true;
        }
    }

    @Override public void resume() {
        if (isPaused) {
            start();
        }
    }

    @Override public void stop() {
        if (mThread != null) {
            mThread.interrupt();
        }

        isPaused = false;
    }

    public void release() {
        mExecutor = null;
        mFunction = null;
        mOnSuccess = null;
        mOnError = null;
        mOnCompleted = null;
        java.lang.System.gc(); // Tells the system that garbage collection is necessary.
    }
}
