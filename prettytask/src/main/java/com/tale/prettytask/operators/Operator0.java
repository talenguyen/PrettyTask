package com.tale.prettytask.operators;

import com.tale.prettytask.functions.Function0;

/**
 * Created by TALE on 11/27/2014.
 */
public class Operator0<Result> implements Function0<Result> {

    private final Function0<Result> function;

    public Operator0(Function0<Result> function) {
        this.function = function;
    }

    @Override public Result call() {
        return function.call();
    }
}
