package com.hirohiro716.print;

import java.util.ArrayList;

/**
 * 印刷関連の静的関数を提供する。
 *
 * @author hiro
 */
public class PrintHelper {

    // これを乗算するとmm→ポイントになる
    private static final double MILLIMETER_TO_POINT_RATE = 72.0 / 25.4;

    /**
     * ミリメートルからポイントを算出する。
     *
     * @param millimeter ミリメートル
     * @return ポイント
     */
    public static double millimeterToPoint(double millimeter) {
        return millimeter * MILLIMETER_TO_POINT_RATE;
    }

    /**
     * ポイントからミリメートルを算出する。
     *
     * @param point ポイント
     * @return ミリメートル
     */
    public static double pointToMillimeter(double point) {
        return point / MILLIMETER_TO_POINT_RATE;
    }

    private static final ArrayList<String> VERTICAL_STRING_LIST = new ArrayList<>();
    
    /**
     * 縦書きの場合に回転する必要がある文字列を取得する。
     *
     * @return 縦書き時に回転する必要がある文字列リスト
     */
    public static ArrayList<String> getVerticalStringList() {
        if (VERTICAL_STRING_LIST.size() == 0) {
            VERTICAL_STRING_LIST.add("(");
            VERTICAL_STRING_LIST.add(")");
            VERTICAL_STRING_LIST.add("-");
            VERTICAL_STRING_LIST.add("<");
            VERTICAL_STRING_LIST.add("=");
            VERTICAL_STRING_LIST.add(">");
            VERTICAL_STRING_LIST.add("[");
            VERTICAL_STRING_LIST.add("]");
            VERTICAL_STRING_LIST.add("{");
            VERTICAL_STRING_LIST.add("|");
            VERTICAL_STRING_LIST.add("}");
            VERTICAL_STRING_LIST.add("~");
            VERTICAL_STRING_LIST.add("‐");
            VERTICAL_STRING_LIST.add("−");
            VERTICAL_STRING_LIST.add("〈");
            VERTICAL_STRING_LIST.add("〉");
            VERTICAL_STRING_LIST.add("《");
            VERTICAL_STRING_LIST.add("》");
            VERTICAL_STRING_LIST.add("「");
            VERTICAL_STRING_LIST.add("」");
            VERTICAL_STRING_LIST.add("『");
            VERTICAL_STRING_LIST.add("』");
            VERTICAL_STRING_LIST.add("【");
            VERTICAL_STRING_LIST.add("】");
            VERTICAL_STRING_LIST.add("〔");
            VERTICAL_STRING_LIST.add("〕");
            VERTICAL_STRING_LIST.add("〜");
            VERTICAL_STRING_LIST.add("ー");
            VERTICAL_STRING_LIST.add("（");
            VERTICAL_STRING_LIST.add("）");
            VERTICAL_STRING_LIST.add("＝");
            VERTICAL_STRING_LIST.add("［");
            VERTICAL_STRING_LIST.add("］");
            VERTICAL_STRING_LIST.add("｛");
            VERTICAL_STRING_LIST.add("｜");
            VERTICAL_STRING_LIST.add("｝");
        }
        return VERTICAL_STRING_LIST;
    }}
