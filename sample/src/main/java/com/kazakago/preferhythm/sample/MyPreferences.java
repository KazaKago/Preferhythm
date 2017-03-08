package com.kazakago.preferhythm.sample;

import com.kazakago.preferhythm.PrefKey;
import com.kazakago.preferhythm.PrefName;

import java.util.Set;

/**
 * Created by tamura_k on 2017/03/08.
 */
@PrefName
public class MyPreferences {

    @PrefKey
    public String foo;
    @PrefKey
    public int bar;
    @PrefKey
    public Integer bar2;
    @PrefKey
    public Long longTest;
    @PrefKey
    public long longTest2;
    @PrefKey
    public Set<String> hugahuga;
    @PrefKey
    public Set<Long> aaaaaaaaaaaaa;

}