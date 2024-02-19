package com.example.rxjavaapp;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class Repository {

    public static Single<List<String>> getStringsByQuery(String query) {
        return Database.getStrings()
                .map(strings -> strings.stream()
                        .filter(string -> string.contains(query))
                        .collect(Collectors.toList())
                );
    }
}
