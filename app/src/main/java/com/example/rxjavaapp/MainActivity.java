package com.example.rxjavaapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rxjavaapp.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        Disposable searchDisposable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
            binding.search.addTextChangedListener(new SearchTextWatcher(text -> {
                emitter.onNext(text.trim());
            }));
        })
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .switchMapSingle((Function<String, SingleSource<?>>) text -> Repository.getStringsByQuery(text))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> binding.results.setText(o.toString()));

        compositeDisposable.add(searchDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
