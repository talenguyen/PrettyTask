package com.tale.prettytask.functions;

import java.util.concurrent.Callable;

public interface Func0<R> extends Function, Callable<R> {
    @Override
    public R call() throws Exception;
}