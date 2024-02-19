package com.example.rxjavaapp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class Database {

    public static Single<List<String>> getStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("text1");
        strings.add("text2");
        strings.add("text3");
        strings.add("word1");
        strings.add("word2");
        strings.add("word3");
        strings.add("example");
        return Single.just(strings);
    }
}
