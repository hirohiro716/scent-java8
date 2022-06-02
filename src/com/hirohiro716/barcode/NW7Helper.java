package com.hirohiro716.barcode;

import java.util.HashMap;

import com.hirohiro716.StringConverter;

/**
 * NW7のチェックなど
 * @author hiro
 */
public class NW7Helper {

    private static HashMap<String, int[]> dataCharacters = null;

    /**
     * それぞれの英数記号を表すバーコードキャラクタ印字パターンを取得する。
     *
     * @return HashMap
     */
    public static HashMap<String, int[]> getDataCharacters() {
        if (dataCharacters == null) {
            dataCharacters = new HashMap<>();
            dataCharacters.put("0", new int[] {1, 1, 1, 1, 1, 3, 3});
            dataCharacters.put("1", new int[] {1, 1, 1, 1, 3, 3, 1});
            dataCharacters.put("2", new int[] {1, 1, 1, 3, 1, 1, 3});
            dataCharacters.put("3", new int[] {3, 3, 1, 1, 1, 1, 1});
            dataCharacters.put("4", new int[] {1, 1, 3, 1, 1, 3, 1});
            dataCharacters.put("5", new int[] {3, 1, 1, 1, 1, 3, 1});
            dataCharacters.put("6", new int[] {1, 3, 1, 1, 1, 1, 3});
            dataCharacters.put("7", new int[] {1, 3, 1, 1, 3, 1, 1});
            dataCharacters.put("8", new int[] {1, 3, 3, 1, 1, 1, 1});
            dataCharacters.put("9", new int[] {3, 1, 1, 3, 1, 1, 1});
            dataCharacters.put("-", new int[] {1, 1, 1, 3, 3, 1, 1});
            dataCharacters.put("$", new int[] {1, 1, 3, 3, 1, 1, 1});
            dataCharacters.put(":", new int[] {3, 1, 1, 1, 3, 1, 3});
            dataCharacters.put("/", new int[] {3, 1, 3, 1, 1, 1, 3});
            dataCharacters.put(".", new int[] {3, 1, 3, 1, 3, 1, 1});
            dataCharacters.put("+", new int[] {1, 1, 3, 1, 3, 1, 3});
            dataCharacters.put("a", new int[] {1, 1, 3, 3, 1, 3, 1});
            dataCharacters.put("b", new int[] {1, 3, 1, 3, 1, 1, 3});
            dataCharacters.put("c", new int[] {1, 1, 1, 3, 1, 3, 3});
            dataCharacters.put("d", new int[] {1, 1, 1, 3, 3, 3, 1});
        }
        return dataCharacters;
    }

    /**
     * 7DRでチェックデジットを算出する。
     *
     * @param barcode バーコード
     * @return チェックデジット
     */
    public static String compute7DR(String barcode) {
        long barcodeLong = StringConverter.stringToLong(barcode);
        return String.valueOf(barcodeLong % 7);
    }

    /**
     * 7DSRでチェックデジットを算出する。
     *
     * @param barcode バーコード
     * @return チェックデジット
     */
    public static String compute7DSR(String barcode) {
        return String.valueOf(7 - Integer.parseInt(compute7DR(barcode)));
    }
}
