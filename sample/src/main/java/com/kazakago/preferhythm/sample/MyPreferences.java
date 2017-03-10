package com.kazakago.preferhythm.sample;

import com.kazakago.preferhythm.PrefClass;
import com.kazakago.preferhythm.PrefField;
import com.kazakago.preferhythm.PrefKeyName;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Created by tamura_k on 2017/03/08.
 */
@PrefClass
public class MyPreferences {

    @Nonnull
    @PrefField
    @PrefKeyName("foooooooooooooo")
    public String foo = "hogehoge";
    @PrefField
    public int bar;
    @PrefField
    public Integer bar2;
    @PrefField
    public Long longTest;
    @PrefField
    public long longTest2;
    @PrefField
    public Set<String> hugahuga;
    @PrefField
    public Set<Long> aaaaaaaaaaaaa;

}