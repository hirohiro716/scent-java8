package com.hirohiro716;

import java.util.ArrayList;

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
    
}
