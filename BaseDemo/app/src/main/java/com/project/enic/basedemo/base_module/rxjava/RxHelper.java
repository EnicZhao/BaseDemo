package com.project.enic.basedemo.base_module.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Enic on 2019/9/20.
 * RxJava 帮助类，做计时器使用
 */
public final class RxHelper {

    private RxHelper() {
        throw new AssertionError();
    }

    /**
     * 倒计时
     * @param time
     * @return
     */
    public static Observable<Integer> countdown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;

        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1)
                .compose(RxSchedulers.<Integer>switchThread());
    }


    public static Observable<Long> countdownByMillisecond(long time) {
        return Observable.interval(0, time, TimeUnit.MILLISECONDS)
                .take(2)
                .compose(RxSchedulers.<Long>switchThread());
    }

}
