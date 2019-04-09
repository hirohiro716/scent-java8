package com.hirohiro716;

import java.util.LinkedHashMap;

/**
 * 日本語用のキー入力機能を実装するRobotのインターフェース.
 * @author hiro
 */
public interface InterfaceRobotJapanese {

    /**
     * IMEモード.
     * @author hiro
     */
    public enum ImeMode {
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

        private ImeMode(int value, String description) {
            this.value = value;
            this.description = description;
        }

        private int value;

        /**
         * 定数値を取得する.
         * @return 定数を表す値
         */
        public int getValue() {
            return this.value;
        }

        private String description;

        /**
         * 定数の説明を取得する.
         * @return 説明
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * 定数値から定数を取得する.
         * @param value
         * @return 定数
         */
        public static ImeMode find(Integer value) {
            for (ImeMode ime : ImeMode.values()) {
                if (value != null && ime.value == value) {
                    return ime;
                }
            }
            return null;
        }

        /**
         * すべての定数の定数値と説明で構成された連想配列を取得する.
         * @return 連想配列
         */
        public static LinkedHashMap<Integer, String> createLinkedHashMap() {
            LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
            for (ImeMode mode: ImeMode.values()) {
                hashMap.put(mode.getValue(), mode.getDescription());
            }
            return hashMap;
        }

    }
    
    /**
     * キーを押して離す動作を実行する.
     * @param keyCodes
     */
    public void keyType(int... keyCodes);
    
    /**
     * IMEをOFFにする.
     */
    public void changeImeOff();
    
    /**
     * IMEをひらがなにする.
     */
    public void changeImeHiragana();
    
    /**
     * IMEをカタカナにする.
     */
    public void changeImeKatakanaWide();
    
    /**
     * IMEをｶﾀｶﾅにする.
     */
    public void changeImeKatakanaNarrow();
    
}
