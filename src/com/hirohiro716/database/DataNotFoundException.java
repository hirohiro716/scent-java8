package com.hirohiro716.database;

/**
 * データが存在しない場合に発生する例外クラス
 * @author hiro
 *
 */
public class DataNotFoundException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1394572582803781847L;

    private final static String MESSAGE = "データが存在しません。";

    /**
     * 初期詳細メッセージを持つ新規例外を構築する. 原因は初期化されない。Throwable.initCause(java.lang.Throwable)を呼び出すことによってあとでこれを初期化することができる。
     */
    public DataNotFoundException() {
        super(MESSAGE);
    }

    /**
     * 指定された詳細メッセージ/原因/抑制の有効化または無効化/書込み可能スタック・トレースの有効化または無効化に基づいて新しい例外を構築する。
     *
     * @param message 詳細メッセージ。
     *
     * @param cause 原因(null値が許可されており原因が存在しないか不明であることを示す)
     * @param enableSuppression 抑制を有効化するかそれとも無効化するかwritableStackTrace
     * @param writableStackTrace スタック・トレースを書込み可能にするかどうか
     */
    public DataNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 指定された詳細メッセージおよび原因を使用して新規例外を構築する。causeと関連付けられた詳細メッセージがこの例外の詳細メッセージに自動的に統合されることはない。
     *
     * @param message 詳細メッセージ(あとでThrowable.getMessage()メソッドで取得できるように保存される)
     * @param cause 原因(あとでThrowable.getCause()メソッドで取得できるように保存される)(null値が許可されており原因が存在しないか不明であることを示す)
     */
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 指定された詳細メッセージを持つ新規例外を構築する. 原因は初期化されない。Throwable.initCause(java.lang.Throwable)を呼び出すことによってあとでこれを初期化することができる。
     *
     * @param message 詳細メッセージ。詳細メッセージはあとでThrowable.getMessage()メソッドで取得できるように保存されます。
     */
    public DataNotFoundException(String message) {
        super(message);
    }

    /**
     * 初期詳細メッセージを持つ新規例外を構築する。
     *
     * @param cause 原因(null値が許可されており原因が存在しないか不明であることを示す)
     */
    public DataNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
