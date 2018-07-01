package com.hirohiro716.number;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

/**
 * 端数処理方法の定数
 * @author hiro
 */
public enum Fraction {
    /**
     * 四捨五入
     */
    ROUND(1, "四捨五入"),
    /**
     * 切り捨て
     */
    FLOOR(2, "切り捨て"),
    /**
     * 切り上げ
     */
    CEIL(3, "切り上げ"),
    ;

    private Fraction(int value, String description) {
        this.value = value;
        this.description = description;
    }

    private int value;

    /**
     * それぞれの端数処理を表す数値を取得する.
     * @return 値
     */
    public int getValue() {
        return this.value;
    }

    private String description;

    /**
     * 端数処理の説明を取得する.
     * @return 説明
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 端数処理を行い結果を取得する.
     * @param value 対象値
     * @return 端数処理されたlong
     */
    public long execute(double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        switch (this) {
        case FLOOR:
            return bigDecimal.setScale(0, RoundingMode.FLOOR).longValue();
        case CEIL:
            return bigDecimal.setScale(0, RoundingMode.CEILING).longValue();
        default:
            return bigDecimal.setScale(0, RoundingMode.HALF_UP).longValue();
        }
    }

    /**
     * 端数処理を行い結果を取得する.
     * @param value 対象値
     * @param digit 桁数
     * @return 端数処理された値
     */
    public double execute(double value, int digit) {
        BigDecimal bigDecimal = new BigDecimal(value);
        switch (this) {
        case FLOOR:
            return bigDecimal.setScale(digit, RoundingMode.FLOOR).doubleValue();
        case CEIL:
            return bigDecimal.setScale(digit, RoundingMode.CEILING).doubleValue();
        default:
            return bigDecimal.setScale(digit, RoundingMode.HALF_UP).doubleValue();
        }
    }

    /**
     * 数値から端数処理を取得する.
     * @param value 端数処理を表す数値
     * @return FractionType
     */
    public static Fraction find(Integer value) {
        for (Fraction type: Fraction.values()) {
            if (value != null && type.value == value) {
                return type;
            }
        }
        return null;
    }

    /**
     * 端数処理一覧の連想配列を取得する.
     * @return LinkedHashMap
     */
    public static LinkedHashMap<Integer, String> createLinkedHashMap() {
        LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
        for (Fraction type: Fraction.values()) {
            hashMap.put(type.getValue(), type.getDescription());
        }
        return hashMap;
    }

}
