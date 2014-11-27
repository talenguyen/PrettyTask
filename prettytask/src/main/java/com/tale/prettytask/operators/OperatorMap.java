package com.tale.prettytask.operators;

import com.tale.prettytask.functions.Function0;
import com.tale.prettytask.functions.Function1;

/**
 * Created by TALE on 11/27/2014.
 */
public class OperatorMap<T, T1> implements Function0<T> {

    private final Function0<T1> function0;
    private final Function1<T, T1> function1;

    public OperatorMap(Function0 function0, Function1<T, T1> function1) {
        this.function0 = function0;
        this.function1 = function1;
    }

    @Override public T call() {
        return function1.call(function0.call());
    }
}
