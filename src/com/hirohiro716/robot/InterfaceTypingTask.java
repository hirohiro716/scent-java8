package com.hirohiro716.robot;

import java.util.Collection;

/**
 * 日本語用のキー入力機能のインターフェース。
 *
 * @author hiro
 * @param <T> KeyCodeの型
 */
public interface InterfaceTypingTask<T> {
    
    /**
     * 定義情報のタスクの区切り文字。
     *
     */
    public static final String DEFINITION_STRING_TASK_DELIMITER = " ";

    /**
     * 定義情報のタイプと値の区切り文字。
     *
     */
    public static final String DEFINITION_STRING_TYPE_AND_VALUE_DELIMITER = ":";

    /**
     * 定義情報の複数値の区切り文字。
     *
     */
    public static final String DEFINITION_STRING_VALUES_DELIMITER = ",";
    
    /**
     * 現在の定義情報を文字列として出力する。
     *
     * @return 定義情報
     */
    public abstract String makeTaskDefinitionString();
    
    /**
     * 定義情報を文字列から取り込む。
     *
     * @param taskDefinitionString 定義情報
     */
    public abstract void importFromTaskDefinitionString(String taskDefinitionString);
    
    /**
     * セットされているタスク数を取得する。
     *
     * @return タスク数
     */
    public abstract int getNumberOfTasks();
    
    /**
     * キーを入力するタスクを追加する。
     *
     * @param keyCodes
     */
    public abstract void addKeyTypeTask(KeyCode... keyCodes);

    /**
     * 待機するタスクを追加する。
     *
     * @param milliseconds
     */
    public abstract void addSleepTask(long milliseconds);
    
    /**
     * Task独自のKeyCodeから実際にタイピングに使用するKeyCodeを探す。
     *
     * @param keyCode Task独自のkeyCode
     * @return タイピングに使用するKeyCode
     */
    public abstract T findTypingKeyCode(KeyCode keyCode);
    
    /**
     * タイピングに使用するKeyCodeからTask独自のKeyCodeを探す。
     *
     * @param keyCode タイピングに使用するKeyCode
     * @return Task独自のKeyCode
     */
    public abstract KeyCode findTaskKeyCode(T keyCode);
    
    /**
     * キーの入力を実行する。
     *
     * @param keyCodes
     */
    public abstract void keyType(Collection<T> keyCodes);
    
    /**
     * タスクを実行する。
     *
     */
    public abstract void execute();
    
    /**
     * KeyCodeの列挙型。
     *
     * @author hiro
     */
    public enum KeyCode {
        /**
         * 不明
         */
        UNDEFINED,
        /**
         * A
         */
        A,
        /**
         * B
         */
        B,
        /**
         * C
         */
        C,
        /**
         * D
         */
        D,
        /**
         * E
         */
        E,
        /**
         * F
         */
        F,
        /**
         * G
         */
        G,
        /**
         * H
         */
        H,
        /**
         * I
         */
        I,
        /**
         * J
         */
        J,
        /**
         * K
         */
        K,
        /**
         * L
         */
        L,
        /**
         * M
         */
        M,
        /**
         * N
         */
        N,
        /**
         * O
         */
        O,
        /**
         * P
         */
        P,
        /**
         * Q
         */
        Q,
        /**
         * R
         */
        R,
        /**
         * S
         */
        S,
        /**
         * T
         */
        T,
        /**
         * U
         */
        U,
        /**
         * V
         */
        V,
        /**
         * W
         */
        W,
        /**
         * X
         */
        X,
        /**
         * Y
         */
        Y,
        /**
         * Z
         */
        Z,
        /**
         * SPACE
         */
        SPACE,
        /**
         * ENTER
         */
        ENTER,
        /**
         * ESCAPE
         */
        ESCAPE,
        /**
         * DELETE
         */
        DELETE,
        /**
         * BACKSPACE
         */
        BACKSPACE,
        /**
         * SHIFT
         */
        SHIFT,
        /**
         * CONTROL
         */
        CONTROL,
        /**
         * ALT
         */
        ALT,
        /**
         * TAB
         */
        TAB,
        /**
         * HOME
         */
        HOME,
        /**
         * END
         */
        END,
        ;
        
        /**
         * 文字列からKeyCodeの取得を試みる。
         *
         * @param string 文字列 
         * @return KeyCode
         */
        public static KeyCode find(String string) {
            for (KeyCode keyCode: KeyCode.values()) {
                if (string != null) {
                    if (keyCode.toString().toLowerCase().equals(string.toLowerCase()) || keyCode.toString().toUpperCase().equals(string.toUpperCase())) {
                        return keyCode;
                    }
                }
            }
            return KeyCode.UNDEFINED;
        }    }

}
