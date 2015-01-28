package com.tale.prettytask.sample;

import android.test.AndroidTestCase;

import com.tale.prettytask.OnResult;
import com.tale.prettytask.Task;
import com.tale.prettytask.functions.Func0;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Created by giang on 1/21/15.
 */
public class TaskTest extends AndroidTestCase {

    @Mock OnResult<Object> mockOnResult;
    @Mock Func0<Object> mockFunc;
    TestableTask<Object> testableTask;

    @Override public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockFunc.call()).thenThrow(new NullPointerException("Null"));
        testableTask = new TestableTask<>(mockFunc, mockOnResult);
    }

    public void testOnCompletedGetCalled() throws Exception {
        final Object result = testableTask.getResult();
        assertTrue(result instanceof NullPointerException);
        testableTask.postResult(result);
        Mockito.verify(mockOnResult).onCompleted();
    }

    public static class TestableTask<T> extends Task<T> {

        public TestableTask(Func0<T> action, OnResult<T> onResult) {
            super(action, onResult);
        }

        public Object getResult() {
            return doInBackground(null);
        }

        public void postResult(Object o) {
            onPostExecute(o);
        }
    }
}
