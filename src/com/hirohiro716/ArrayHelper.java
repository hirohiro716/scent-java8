package com.hirohiro716;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 配列操作に関するクラス。
 *
 * @author hiro
 */
public class ArrayHelper {
    
    /**
     * 配列を結合する。
     *
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
     * 配列をListに変換する。
     *
     * @param <T> 型
     * @param array 配列
     * @return Listオブジェクト
     */
    public static <T> List<T> createListFromArray(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }
    
    /**
     * 数値の配列をソートする。
     *
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
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                if (o1.doubleValue() < o2.doubleValue()) {
                    return -1;
                }
                if (o1.doubleValue() > o2.doubleValue()) {
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
     * 数値の配列を昇順でソートする。
     *
     * @param <T> Numberを継承した数値型
     * @param array 数値の配列
     */
    public static <T extends Number> void sort(T[] array) {
        sort(array, false);
    }
    
    /**
     * 配列中で最初に見つかった検索値に対するインデックスを取得する. 見つからなかった場合は-1を返す。
     *
     * @param <T> 
     * @param array 配列
     * @param searchValue 検索値
     * @return インデックス
     */
    public static <T> int findIndex(T[] array, T searchValue) {
        int findIndex = -1;
        if (array != null) {
            for (int index = 0; index < array.length; index++) {
                T value = array[index];
                if (value == null) {
                    if (value == searchValue) {
                        findIndex = index;
                        break;
                    }
                } else {
                    if (value.equals(searchValue)) {
                        findIndex = index;
                        break;
                    }
                }
            }
        }
        return findIndex;
    }

    /**
     * 配列中で最後に見つかった検索値に対するインデックスを取得する. 見つからなかった場合は-1を返す。
     *
     * @param <T> 型
     * @param array 配列
     * @param searchValue 検索値
     * @return インデックス
     */
    public static <T> int findLastIndex(T[] array, T searchValue) {
        int findIndex = -1;
        if (array != null) {
            for (int index = array.length - 1; index > -1; index--) {
                T value = array[index];
                if (value == null) {
                    if (value == searchValue) {
                        findIndex = index;
                        break;
                    }
                } else {
                    if (value.equals(searchValue)) {
                        findIndex = index;
                        break;
                    }
                }
            }
        }
        return findIndex;
    }

    /**
     * 配列をすべて区切り文字で連結する。
     *
     * @param <T> 型
     * @param array 配列
     * @param delimiter 区切り文字
     * @return 連結した文字列
     */
    public static <T> String join(T[] array, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T value: array) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(delimiter);
            }
            if (value != null) {
                stringBuilder.append(value.toString());
            }
        }
        return stringBuilder.toString();
    }}
