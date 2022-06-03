package com.hirohiro716.awt;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import static com.hirohiro716.StringConverter.*;

/**
 * java.awtでバーコードを生成するクラス。
 *
 * @author hiro
 *
 */
public class JAN13Helper extends com.hirohiro716.barcode.JAN13Helper {

    /**
     * JAN13バーコードを描画します。
     *
     * @param barcode バーコード
     * @param width 描画幅
     * @param height 描画高さ
     * @param x 描画位置x
     * @param y 描画位置y
     * @param graphics2d Graphics2Dインスタンス
     */
    public static void drawBarcode(String barcode, int width, int height, int x, int y, Graphics2D graphics2d) {
        // モジュール単位を算出
        double moduleOne = width / 115d;
        if (moduleOne <= 0) {
            return;
        }
        // バーコードの整合性をチェック
        if (isValid(barcode) == false) {
            return;
        }
        // 描画位置を設定
        double printX = x;
        // ホワイトスペース
        printX += (moduleOne * 10);
        // スタートコード
        graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
        printX += (moduleOne * 2);
        graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
        printX += moduleOne;
        // バーコード最初の1文字を取得する
        int firstChar = stringToInteger(barcode.substring(0, 1));
        // 左側を描画
        int[] leftParityType = LEFT_PARITIES_TYPES[firstChar];
        for (int charNumber = 1; charNumber <= 6; charNumber++) {
            int type = leftParityType[charNumber - 1];
            int printChar = stringToInteger(barcode.substring(charNumber, charNumber + 1));
            int[] printParity = LEFT_PARITIES[type][printChar];
            for (int p : printParity) {
                if (p == 1) {
                    graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
                }
                printX += moduleOne;
            }
        }
        // センターコード
        printX += moduleOne;
        graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
        printX += (moduleOne * 2);
        graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
        printX += (moduleOne * 2);
        // 右側を描画
        for (int charNumber = 7; charNumber <= 12; charNumber++) {
            int printChar = stringToInteger(barcode.substring(charNumber, charNumber + 1));
            int[] printParity = RIGHT_PARITIES[printChar];
            for (int p : printParity) {
                if (p == 1) {
                    graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
                }
                printX += moduleOne;
            }
        }
        // ストップコード
        graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
        printX += (moduleOne * 2);
        graphics2d.fill(new Rectangle2D.Double(printX, y, moduleOne, height));
    }
}
