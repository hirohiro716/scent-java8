package com.hirohiro716.robot;

import java.util.LinkedHashMap;

/**
 * 日本語用のキー入力機能のインターフェース。
 *
 * @author hiro
 * @param <T> KeyCodeの型
 */
public interface InterfaceTypingRobotJapanese<T> {

    /**
     * IMEモード。
     *
     * @author hiro
     */
    public enum IMEMode {
        /**
         * OFF
         */
        OFF(0, "OFF"),
        /**
         * ひらがな
         */
        HIRAGANA(1, "ひらがな"),
        /**
         * 全角カタカナ
         */
        KATAKANA_WIDE(2, "カタカナ"),
        /**
         * 半角ｶﾀｶﾅ
         */
        KATAKANA_NARROW(3, "ｶﾀｶﾅ"),
        ;

        private IMEMode(int value, String description) {
            this.value = value;
            this.description = description;
        }

        private int value;

        /**
         * 定数値を取得する。
         *
         * @return 定数を表す値
         */
        public int getValue() {
            return this.value;
        }

        private String description;

        /**
         * 定数の説明を取得する。
         *
         * @return 説明
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * 定数値から定数を取得する。
         *
         * @param value
         * @return 定数
         */
        public static IMEMode find(Integer value) {
            for (IMEMode ime : IMEMode.values()) {
                if (value != null && ime.value == value) {
                    return ime;
                }
            }
            return null;
        }

        /**
         * すべての定数の定数値と説明で構成された連想配列を取得する。
         *
         * @return 連想配列
         */
        public static LinkedHashMap<Integer, String> createLinkedHashMap() {
            LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
            for (IMEMode mode: IMEMode.values()) {
                hashMap.put(mode.getValue(), mode.getDescription());
            }
            return hashMap;
        }

    }
    
    /**
     * キーを押して離す動作を実行する。
     *
     * @param keyCodes
     */
    @SuppressWarnings("unchecked")
    public abstract void keyType(T... keyCodes);
    
    /**
     * IMEをOFFにする。
     *
     */
    public abstract void changeIMEOff();
    
    /**
     * IMEをひらがなにする。
     *
     */
    public abstract void changeIMEHiragana();
    
    /**
     * IMEをカタカナにする。
     *
     */
    public abstract void changeIMEKatakanaWide();
    
    /**
     * IMEをｶﾀｶﾅにする。
     *
     */
    public abstract void changeIMEKatakanaNarrow();
    
}
