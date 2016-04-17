package com.example.wangzhengkui.preferencefile.preference.cache;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/14.
 */
public class QueuedWork {
    // The set of Runnables that will finish or wait on any async
   // activities started by the application.
    private static final ConcurrentLinkedQueue<Runnable> sPendingWorkFinishers = new ConcurrentLinkedQueue<Runnable>();
    private static ExecutorService sSingleThreadExecutor = null; // lazy, guarded by class

    public static ExecutorService singleThreadExecutor() {
        synchronized (QueuedWork.class) {
            if (sSingleThreadExecutor == null) {
                // TODO: can we give this single thread a thread name?
                sSingleThreadExecutor = Executors.newSingleThreadExecutor();
            }
            return sSingleThreadExecutor;
        }
    }

    public static void add(Runnable finisher) {
        sPendingWorkFinishers.add(finisher);
    }

    public static void remove(Runnable finisher) {
        sPendingWorkFinishers.remove(finisher);
    }
}
