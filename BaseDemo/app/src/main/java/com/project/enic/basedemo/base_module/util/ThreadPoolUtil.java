package com.project.enic.basedemo.base_module.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：beautiful_fly on 2018/8/28 0028.
 * 邮箱：764339729@qq.com
 * 版本：v1.0
 */

public class ThreadPoolUtil {

    private static ExecutorService mThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
        private AtomicInteger index = new AtomicInteger();
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("this thread name is " + index.getAndIncrement());
            return thread;
        }
    });

    public static void excuteTask(Runnable runnable){
        mThreadPool.execute(runnable);
    }

}
