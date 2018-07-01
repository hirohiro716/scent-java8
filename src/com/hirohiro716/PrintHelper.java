package com.hirohiro716;

/**
 * 印刷関連の静的関数を提供する.
 * @author hiro
 */
public class PrintHelper {

    // これを乗算するとmm→ポイントになる
    private static final double MILLIMETER_TO_POINT_RATE = 72.0 / 25.4;

    /**
     * ミリメートルからポイントを算出する..
     * @param millimeter ミリメートル
     * @return ポイント
     */
    public static double millimeterToPoint(double millimeter) {
        return millimeter * MILLIMETER_TO_POINT_RATE;
    }

    /**
     * ポイントからミリメートルを算出する.
     * @param point ポイント
     * @return ミリメートル
     */
    public static double pointToMillimeter(double point) {
        return point / MILLIMETER_TO_POINT_RATE;
    }

}
