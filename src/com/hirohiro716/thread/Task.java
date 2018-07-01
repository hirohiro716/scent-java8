package com.hirohiro716.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 別スレッドで動かす補助をするクラス.
 * @author hiro
 * @param <T> 変数タイプ
 */
public class Task<T> {

    /**
     * コンストラクタ
     * @param callable
     */
    public Task(Callable<T> callable) {
        this.task = new FutureTask<>(callable);
        this.thread = new Thread(this.task);
    }

    private FutureTask<T> task;

    /**
     * 内部で保持しているFutureTaskインスタンスを取得する.
     * @return FutureTaskインスタンス
     */
    public FutureTask<T> getFutureTask() {
        return this.task;
    }

    private Thread thread;

    /**
     * 内部で保持しているThreadインスタンスを取得する.
     * @return Threadインスタンス
     */
    public Thread getThread() {
        return this.thread;
    }

    /**
     * タスクをスタートさせる.
     */
    public void start() {
        this.thread.start();
    }

    /**
     * タスクが終了したか確認する.
     * @return 終了していればTrue
     */
    public boolean isDone() {
        return this.task.isDone();
    }

    /**
     * タスクを処理した結果を取得する.<br>
     * 処理が終わっていない場合は終了まで待機する.
     * @return 結果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public T getResult() throws InterruptedException, ExecutionException {
        return this.task.get();
    }

}
