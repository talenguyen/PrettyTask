package com.tale.prettytask;

/**
 * Created by TALE on 11/27/2014.
 */
public interface TaskHandler {

    /**
     * Pause running task. This will effect if task is started.
     */
    public void pause();

    /**
     * Resume the paused task. This just effect if task is paused.
     */
    public void resume();

    /**
     * Stop the task. This just effect if task is started or paused. After this called task's
     * become invalid.
     */
    public void stop();
}
