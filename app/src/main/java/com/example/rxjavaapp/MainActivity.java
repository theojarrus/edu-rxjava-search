package com.example.rxjavaapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> strings = new ArrayList<>();
        strings.add("12");
        strings.add("32");

        // Маппинг списка внутри Observable
        Log.wtf("RXJAVA", "disposableMap -----");
        Disposable disposableMap = Observable.fromArray(strings)
                .map(strings1 -> // это маппинг Observable
                        strings1 // список
                                .stream() // Stream API
                                .map(s -> Integer.parseInt(s)) // метод map из Stream API (преобразуем каждый элемент)
                                .collect(Collectors.toList())) // собираем все элементы из Stream в список
                .subscribe(integers ->
                                Log.wtf("RXJAVA", "onNext: " + integers),
                        throwable ->
                                Log.wtf("RXJAVA", "onError: " + throwable),
                        () -> {
                            Log.wtf("RXJAVA", "onComplete");
                        });

        compositeDisposable.add(disposableMap);

        ObservableSource one = Observable.fromArray(1, 2, 3).delay(2000, TimeUnit.MILLISECONDS);
        ObservableSource two = Observable.fromArray("a", "b").delay(1000, TimeUnit.MILLISECONDS);

        // Обьединение двух потоков данных через combineLates (можно заменить на zip)
        Log.wtf("RXJAVA", "disposableCombine -----");
        Disposable disposableCombine = Observable.combineLatest(one, two, (BiFunction<Integer, String, String>) (integer, s) -> integer.toString() + s)
                .subscribe(object -> {
                    Log.wtf("RXJAVA", "onNext: " + object.toString());
                }, (Consumer<Throwable>) throwable -> {
                    Log.wtf("RXJAVA", "onError: " + throwable);
                }, () -> {
                    Log.wtf("RXJAVA", "onComplete");
                });

        compositeDisposable.add(disposableCombine);

        // Обьединение двух потоков данных через merge (можно заменить на concat)
        Log.wtf("RXJAVA", "disposableMerge -----");
        Disposable disposableMerge = Observable.merge(one, two).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) {
                Log.wtf("RXJAVA", "onNext: " + o.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.wtf("RXJAVA", "onError: " + throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Throwable {
                Log.wtf("RXJAVA", "onComplete");
            }
        });

        compositeDisposable.add(disposableMerge);

        // Обьединение двух потоков данных через flatMap (один поток зависит от другого потока) (можно заменить на concatMap - порядок и switchMap - переключение)
        Log.wtf("RXJAVA", "disposableFlatMap -----");
        Disposable disposableFlatMap = Observable.fromArray(1, 3, 4, 5)
                .flatMap(integer -> {
                    return Observable.just(integer * 10)
                            .delay(2000, TimeUnit.MILLISECONDS);
                })
                .subscribe(integer -> {
                    Log.wtf("RXJAVA", "onNext: " + integer);
                }, throwable -> {
                    Log.wtf("RXJAVA", "onError: " + throwable);
                }, () -> {
                    Log.wtf("RXJAVA", "onComplete");
                });

        compositeDisposable.add(disposableFlatMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
