package com.kazakago.quickpref.sample;

import com.kazakago.quickpref.PrefKey;
import com.kazakago.quickpref.PrefName;

import javax.annotation.Nonnull;

/**
 * Created by tamura_k on 2017/03/08.
 */
@PrefName
public class MyPreferences {

    @Nonnull
    @PrefKey
    public String foo;
    @PrefKey
    public int bar;
    @PrefKey
    public Integer bar2;

}