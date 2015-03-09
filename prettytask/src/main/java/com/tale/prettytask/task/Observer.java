package com.tale.prettytask.task;

/**
 * Created by tale on 3/9/15.
 */
public interface Observer<Result> {
    /**
     * Called before task is completed.
     * <ul>
     * <li>After this Observer is registered and task is <b>started</b>.</li>
     * <li>After this Observer is registered and task is <b>running</b>.</li>
     * </ul>
     * <i>NOTE:</i> This will be called only one time.
     */
    void onBeforeResult();

    /**
     * Called when task is execute successful.
     *
     * @param result The result.
     */
    void onResult(Result result);

    /**
     * Called when task is execute error.
     *
     * @param throwable The exception throw.
     */
    void onError(Throwable throwable);

}
