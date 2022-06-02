package com.hirohiro716.number;

import com.hirohiro716.StringConverter;

/**
 * 計算関数を格納するクラス。
 *
 * @author hiro
 */
public class CalculationHelper {

    /**
     * 文字列内の全ての数値を加算して返します。
     *
     * @param value 数値が含まれている文字列
     * @return 結果
     */
    public static int calculateAddingAllNumberStrings(String value) {
        int sum = 0;
        for (int i = 0; i < value.length(); i++) {
            sum += StringConverter.stringToInteger(value.substring(i, i + 1));
        }
        return sum;
    }

    /**
     * intの配列のすべての値を加算して返します。
     *
     * @param values int配列
     * @return 結果
     */
    public static int calculateAddingAllIntegers(int[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

}
