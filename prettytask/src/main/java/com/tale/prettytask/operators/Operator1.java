package com.tale.prettytask.operators;

import com.tale.prettytask.functions.Function1;

/**
 * Created by TALE on 11/27/2014.
 */
public class Operator1<Result, Param> implements Function1<Result, Param> {

    private final Function1<Result, Param> function;

    public Operator1(Function1<Result, Param> function) {
        this.function = function;
    }

    @Override public Result call(Param param) {
        return function.call(param);
    }
}
