package com.hirohiro716;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    
    /**
     * 数値の配列をソートする.
     * @param <T> Numberを継承した数値型
     * @param array 数値の配列
     * @param isReverseOrder 降順かどうか
     */
    public static <T extends Number> void sort(T[] array, boolean isReverseOrder) {
        Comparator<T> comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null || o1.doubleValue() < o2.doubleValue()) {
                    return -1;
                }
                if (o2 == null || o1.doubleValue() > o2.doubleValue()) {
                    return 1;
                }
                return 0;
            }
        };
        if (isReverseOrder) {
            Arrays.sort(array, Collections.reverseOrder(comparator));
        } else {
            Arrays.sort(array, comparator);
        }
    }

    /**
     * 数値の配列を昇順でソートする.
     * @param <T> Numberを継承した数値型
     * @param array 数値の配列
     */
    public static <T extends Number> void sort(T[] array) {
        sort(array, false);
    }
    
}
