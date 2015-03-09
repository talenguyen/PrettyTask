package com.tale.prettytask.task;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TasksTest extends TestCase {

    @Mock Observer observer;

    public void setUp() throws Exception {
        super.setUp();
        observer = Mockito.mock(Observer.class);
    }

    public void tearDown() throws Exception {
        observer = null;
    }

    public void testRegisterBeforeExecute() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.register("testRegister", observer);

        // Not call yet.
        verify(observer, never()).onBeforeResult();
        // Call after execute.
        tasks.onBeforeResult();
        verify(observer, Mockito.only()).onBeforeResult();
    }

    public void testRegisterWhenExecuting() throws Exception {
        final Tasks tasks = new Tasks();
        // Register before.
        final String key = "testRegister";
        tasks.register(key, observer);
        // Not call yet.
        verify(observer, never()).onBeforeResult();
        // Execute the task.
        tasks.onBeforeResult();
        // Unregister
        tasks.unRegister(key);
        // Call after execute.
        verify(observer, Mockito.only()).onBeforeResult();

    }

    public void testUnRegister() throws Exception {

    }

    public void testOnBeforeResultBeforeRegister() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.onBeforeResult();
        // Not call yet.
        verify(observer, never()).onBeforeResult();
    }

    public void testOnBeforeResultAfterRegister() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.register("testOnBeforeResultAfterRegister", observer);
        // Not call yet.
        verify(observer, never()).onBeforeResult();
        // Execute
        tasks.onBeforeResult();
        // Verify observer
        verify(observer, only()).onBeforeResult();
        tasks.onBeforeResult();
        verify(observer, times(2)).onBeforeResult();
    }

    public void testOnResultBeforeRegister() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.onResult("data");
        // Not call yet.
        verify(observer, never()).onResult(any());
    }

    public void testOnResultAfterRegister() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.register("testOnResultAfterRegister", observer);
        // Not call yet.
        final String mockResult = "data";
        verify(observer, never()).onResult(any());
        // Execute
        tasks.onResult(mockResult);
        // Verify observer
        verify(observer, only()).onResult(eq(mockResult));
        tasks.onResult(mockResult);
        verify(observer, times(2)).onResult(eq(mockResult));
    }

    public void testOnErrorBeforeRegister() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.onError(new NullPointerException("null"));
        // Not call yet.
        verify(observer, never()).onResult(any());
    }

    public void testOnErrorAfterRegister() throws Exception {
        final Tasks tasks = new Tasks();
        tasks.register("testOnErrorAfterRegister", observer);
        // Not call yet.
        final Throwable mockResult = new IllegalArgumentException("data");
        verify(observer, never()).onError(any(Throwable.class));
        // Execute
        tasks.onError(mockResult);
        // Verify observer
        verify(observer, only()).onError(eq(mockResult));
        tasks.onError(mockResult);
        verify(observer, times(2)).onError(eq(mockResult));
    }
}