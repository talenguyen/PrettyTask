package com.tale.prettytask.functions;

public interface Func1<T1, R> extends Function {
    public R call(T1 t1) throws Exception;
}