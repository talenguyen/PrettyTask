package com.tale.prettytask;

import com.tale.prettytask.functions.Action0;
import com.tale.prettytask.functions.Action1;
import com.tale.prettytask.functions.Function0;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by talenguyen on 20/11/2014.
 */
public class Async<T> {
    private static final String LOG_TAG = "Async";

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, LOG_TAG + " #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory
    );

    /**
     * An {@link Executor} that executes tasks one at a time in serial
     * order.  This serialization is global to a particular process.
     */
    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(
                    new Runnable() {
                        public void run() {
                            try {
                                r.run();
                            } finally {
                                scheduleNext();
                            }
                        }
                    }
            );
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }

    private final Function0<T> mFunction;

    private Async(Function0<T> function) {
        this.mFunction = function;
    }

    /**
     * Execute task on THREAD_POOL_EXECUTOR by default.
     *
     * @param onSuccess   callback.
     * @param onError     callback.
     * @param onCompleted callback which be called after onSuccess or onError. NOTE: this will not
     *                    be called when task is paused or stopped.
     * @return
     */
    public TaskHandler execute(
            Action1<T> onSuccess,
            Action1<Throwable> onError,
            final Action0 onCompleted
    ) {
        return execute(SERIAL_EXECUTOR, onSuccess, onError, onCompleted);
    }

    /**
     * Execute task on SERIAL_EXECUTOR.
     *
     * @param onSuccess   callback.
     * @param onError     callback.
     * @param onCompleted callback which be called after onSuccess or onError. NOTE: this will not
     *                    be called when task is paused or stopped.
     * @return
     */
    public TaskHandler executeIO(
            Action1<T> onSuccess,
            Action1<Throwable> onError,
            final Action0 onCompleted
    ) {
        return execute(SERIAL_EXECUTOR, onSuccess, onError, onCompleted);
    }

    /**
     * Call to execute task on specific Executor.
     *
     * @param executor    The executor to run.
     * @param onSuccess   callback.
     * @param onError     callback.
     * @param onCompleted callback which be called after onSuccess or onError. NOTE: this will not
     *                    be called when task is paused or stopped.
     * @return {@link com.tale.prettytask.TaskHandler} object. Use full to handle task follow
     * life-cycle of Activity & Fragment
     */
    public TaskHandler execute(
            Executor executor,
            Action1<T> onSuccess,
            Action1<Throwable> onError,
            final Action0 onCompleted
    ) {
        if (onSuccess == null) {
            throw new IllegalArgumentException("onSuccess can not be null");
        }
        final TaskHandlerIml taskHandlerIml = new TaskHandlerIml(
                executor,
                mFunction,
                onSuccess,
                onError,
                onCompleted
        );
        taskHandlerIml.start();
        return taskHandlerIml;
    }

}
