package com.hirohiro716;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 配列操作に関するクラス.
 * @author hiro
 */
public class ArrayHelper {
    
    /**
     * 配列を結合する.
     * @param <T> 
     * @param arrays 結合する配列
     * @return 結合された配列
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[]... arrays) {
        ArrayList<T> merged = new ArrayList<>();
        for (T[] array: arrays) {
            for (T value: array) {
                merged.add(value);
            }
        }
        return merged.toArray(arrays[0]);
    }
    
    /**
     * 配列をListに変換する.
     * @param <T>
     * @param array 配列
     * @return Listオブジェクト
     */
    public static <T> List<T> createListFromArray(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
    
}
