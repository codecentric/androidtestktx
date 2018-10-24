package com.daimler.mbpro.android.core.signalr;

import android.support.annotation.NonNull;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPool with {@link BackgroundExecutor#NUMBER_OF_CORES} core pool size and
 * {@link BackgroundExecutor#NUMBER_OF_CORES} * 2 maximum pool size with one minute of keepAlive time,
 * before the runnable is discarded.
 */
public class BackgroundExecutor {
  private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
  private LinkedBlockingDeque<Runnable> taskQueue = new LinkedBlockingDeque<>();

  private ExecutorService executorService =
      new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES * 2, 1, TimeUnit.MINUTES, taskQueue,
                             new BackgroundThreadFactory()
      );

  public void execute(Runnable runnable) {
    executorService.execute(runnable);
  }

  /**
   * ThreadFactory which creates threads with SignalR naming.
   */
  static class BackgroundThreadFactory implements ThreadFactory {
    @Override public Thread newThread(@NonNull Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setName(String.format("SignalR-%s", thread.getName()));
      Log.d(getClass().getSimpleName(), String.format("Created thread: %s", thread.getName()));
      return thread;
    }
  }
}
