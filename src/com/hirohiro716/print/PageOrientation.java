package com.hirohiro716.print;

import java.util.LinkedHashMap;

/**
 * ページ向きの列挙型.
 * @author hiro
 */
public enum PageOrientation {
    /**
     * 縦
     */
    PORTRAIT(1, "縦向き"),
    /**
     * 横
     */
    LANDSCAPE(2, "横向き"),
    /**
     * 縦向き180度回転
     */
    REVERSE_PORTRAIT(3, "縦向き180度回転"),
    /**
     * 横向き180度回転
     */
    REVERSE_LANDSCAPE(4, "横向き180度回転"),
    ;

    private PageOrientation(int value, String description) {
        this.value = value;
        this.description = description;
    }

    private int value;

    /**
     * それぞれのページ向きを表す数値を取得する.
     * @return 値
     */
    public int getValue() {
        return this.value;
    }

    private String description;

    /**
     * ページ向きの説明を取得する.
     * @return 説明
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * 数値からページ向きを取得する.
     * @param value ページ向きを表す数値
     * @return PageOrientation
     */
    public static PageOrientation find(Integer value) {
        for (PageOrientation type: PageOrientation.values()) {
            if (value != null && type.value == value) {
                return type;
            }
        }
        return null;
    }

    /**
     * ページ向き一覧の連想配列を取得する.
     * @return LinkedHashMap
     */
    public static LinkedHashMap<Integer, String> createLinkedHashMap() {
        LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
        for (PageOrientation type: PageOrientation.values()) {
            hashMap.put(type.getValue(), type.getDescription());
        }
        return hashMap;
    }

}
