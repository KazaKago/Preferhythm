package com.kazakago.quickpref.sample;

import com.kazakago.quickpref.LogField;
import com.kazakago.quickpref.Loggable;

/**
 * Created by tamura_k on 2017/03/08.
 */
@Loggable
public class MyModel {
    @LogField
    public String foo;
    @LogField
    public int bar;

    public MyModel(String foo, int bar) {
        this.foo = foo;
        this.bar = bar;
    }
}
