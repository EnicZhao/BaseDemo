package com.project.enic.basedemo.base_module.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者： Enic
 * 时间:  2019/9/20.
 * 介绍：线程调度，所谓线程调度即在进行耗时操作时，将耗时事件调度到子线程或IO线程，当耗时操作结束需要在
 *       主线程中处理事件，则调度到主线程中
 */

public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> switchThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
