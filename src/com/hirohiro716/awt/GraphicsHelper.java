package com.hirohiro716.awt;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.hirohiro716.RudeArray;

import static com.hirohiro716.StringConverter.*;

/**
 * Graphicsを使用して描画する際に便利なメソッドを格納したクラスです.
 * @author hiro
 */
public class GraphicsHelper {

    /**
     * 文字列の描画幅をポイントで取得します.
     * @param value 文字列
     * @param useFont 使用フォント
     * @param graphics2D
     * @return 幅（point）
     */
    public static int calculateStringWidth(String value, Font useFont, Graphics2D graphics2D) {
        graphics2D.setFont(useFont);
        return graphics2D.getFontMetrics().stringWidth(value);
    }

    /**
     * 文字列の描画高さをポイントで取得します.
     * @param useFont 使用フォント
     * @param graphics2D
     * @return 高さ（point）
     */
    public static int calculateStringHeight(Font useFont, Graphics2D graphics2D) {
        graphics2D.setFont(useFont);
        return (int) (graphics2D.getFontMetrics().getHeight() * 0.572);
    }

    /**
     * 文字列の描画高さをポイントで取得します.
     * @param value 文字列
     * @param useFont 使用フォント
     * @param graphics2D
     * @return 高さ（point）
     */
    public static int calculateStringHeight(String value, Font useFont, Graphics2D graphics2D) {
        graphics2D.setFont(useFont);
        String[] splitValue = value.split(LINE_SEPARATOR);
        double height = graphics2D.getFontMetrics().getHeight() * 0.572;
        return (int) (height * splitValue.length);
    }

    /**
     * 文字列の描画サイズをポイントで取得します.
     * @param value 文字列
     * @param useFont 使用フォント
     * @param graphics2D
     * @return [0]→幅／[1]→高さ
     */
    public static Dimension calculateStringDimension(String value, Font useFont, Graphics2D graphics2D) {
        graphics2D.setFont(useFont);
        Dimension dimension = new Dimension();
        dimension.setSize(graphics2D.getFontMetrics().stringWidth(value), (int) (graphics2D.getFontMetrics().getHeight() * 0.572));
        return dimension;
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, int height, String fontName, int fontSize) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return createApplySizeFont(value, width, height, fontName, fontSize, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param graphics2D
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, int height, String fontName, int fontSize, Graphics2D graphics2D) {
        return createApplySizeFont(value, width, fontName, fontSize, Font.PLAIN, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, int height, String fontName, int fontSize, int fontStyle) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return createApplySizeFont(value, width, height, fontName, fontSize, fontStyle, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @param graphics2D
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, int height, String fontName, int fontSize, int fontStyle, Graphics2D graphics2D) {
        Font newFont = new Font(fontName, fontStyle, fontSize);
        float newSize = fontSize;
        while (newSize != 1) {
            Dimension dimension = calculateStringDimension(value, newFont, graphics2D);
            if (dimension.getWidth() <= width && dimension.getHeight() <= height) {
                break;
            }
            newSize -= 0.5;
            newFont = newFont.deriveFont(newSize);
        }
        return newFont;
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, String fontName, int fontSize) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return createApplySizeFont(value, width, fontName, fontSize, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param graphics2D
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, String fontName, int fontSize, Graphics2D graphics2D) {
        return createApplySizeFont(value, width, fontName, fontSize, Font.PLAIN, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, String fontName, int fontSize, int fontStyle) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return createApplySizeFont(value, width, fontName, fontSize, fontStyle, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @param graphics2D
     * @return 調整後のフォント
     */
    public static Font createApplySizeFont(String value, int width, String fontName, int fontSize, int fontStyle, Graphics2D graphics2D) {
        Font newFont = new Font(fontName, fontStyle, fontSize);
        float newSize = fontSize;
        while (newSize != 1) {
            Dimension dimension = calculateStringDimension(value, newFont, graphics2D);
            if (dimension.getWidth() <= width) {
                break;
            }
            newSize -= 0.5;
            newFont = newFont.deriveFont(newSize);
        }
        return newFont;
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @return 調整後のフォント
     */
    public static int calculateApplyFontSize(String value, int width, int height, String fontName, int fontSize) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return calculateApplyFontSize(value, width, height, fontName, fontSize, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param graphics2D
     * @return 調整後のフォント
     */
    public static int calculateApplyFontSize(String value, int width, int height, String fontName, int fontSize, Graphics2D graphics2D) {
        return calculateApplyFontSize(value, width, height, fontName, fontSize, Font.PLAIN, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @return 調整後のフォントサイズ
     */
    public static int calculateApplyFontSize(String value, int width, int height, String fontName, int fontSize, int fontStyle) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return calculateApplyFontSize(value, width, height, fontName, fontSize, fontStyle, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param height 調節高さ
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @param graphics2D
     * @return 調整後のフォントサイズ
     */
    public static int calculateApplyFontSize(String value, int width, int height, String fontName, int fontSize, int fontStyle, Graphics2D graphics2D) {
        Font newFont = new Font(fontName, fontStyle, fontSize);
        float newSize = fontSize;
        while (newSize != 1) {
            Dimension dimension = calculateStringDimension(value, newFont, graphics2D);
            if (dimension.getWidth() <= width && dimension.getHeight() <= height) {
                break;
            }
            newSize -= 0.5;
            newFont = newFont.deriveFont(newSize);
        }
        return newFont.getSize();
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @return 調整後のフォントサイズ
     */
    public static int calculateApplyFontSize(String value, int width, String fontName, int fontSize) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return calculateApplyFontSize(value, width, fontName, fontSize, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param graphics2D
     * @return 調整後のフォントサイズ
     */
    public static int calculateApplyFontSize(String value, int width, String fontName, int fontSize, Graphics2D graphics2D) {
        return calculateApplyFontSize(value, width, fontName, fontSize, Font.PLAIN, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @return 調整後のフォントサイズ
     */
    public static int calculateApplyFontSize(String value, int width, String fontName, int fontSize, int fontStyle) {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        return calculateApplyFontSize(value, width, fontName, fontSize, fontStyle, graphics2D);
    }

    /**
     * フォントサイズを自動調節します.
     * @param value 文字列
     * @param width 調節幅
     * @param fontName フォント名
     * @param fontSize 初期サイズ
     * @param fontStyle フォントスタイル（Font.PLAINなど）
     * @param graphics2D
     * @return 調整後のフォントサイズ
     */
    public static int calculateApplyFontSize(String value, int width, String fontName, int fontSize, int fontStyle, Graphics2D graphics2D) {
        Font newFont = new Font(fontName, fontStyle, fontSize);
        float newSize = fontSize;
        while (newSize != 1) {
            Dimension dimension = calculateStringDimension(value, newFont, graphics2D);
            if (dimension.getWidth() <= width) {
                break;
            }
            newSize -= 0.5;
            newFont = newFont.deriveFont(newSize);
        }
        return newFont.getSize();
    }

    /**
     * 左上を基準として文字列を描画します.
     * @param value 文字列
     * @param x 左位置
     * @param y 上位置
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringTopLeft(String value, int x, int y, Font font, Graphics2D graphics2D) {
        int valueHeight = calculateStringHeight(value, font, graphics2D);
        graphics2D.setFont(font);
        graphics2D.drawString(value, x, y + valueHeight);
    }

    /**
     * 右上を基準として文字列を描画します.
     * @param value 文字列
     * @param x 左位置
     * @param y 上位置
     * @param width 幅
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringTopRight(String value, int x, int y, int width, Font font, Graphics2D graphics2D) {
        Dimension dimension = calculateStringDimension(value, font, graphics2D);
        graphics2D.setFont(font);
        graphics2D.drawString(value, x + width - (int) dimension.getWidth(), y + (int) dimension.getHeight());
    }

    /**
     * 中央上を基準として文字列を描画します.
     * @param value 文字列
     * @param x 左位置
     * @param y 上位置
     * @param width 幅
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringTopCenter(String value, int x, int y, int width, Font font, Graphics2D graphics2D) {
        Dimension dimension = calculateStringDimension(value, font, graphics2D);
        graphics2D.setFont(font);
        graphics2D.drawString(value, x + (width / 2) - ((int) dimension.getWidth() / 2), y + (int) dimension.getHeight());
    }

    /**
     * 左下を基準として文字列を描画します.
     * @param value 文字列
     * @param x 左位置
     * @param y 上位置
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringBottomLeft(String value, int x, int y, Font font, Graphics2D graphics2D) {
        graphics2D.setFont(font);
        graphics2D.drawString(value, x, y);
    }

    /**
     * 右下を基準として文字列を描画します.
     * @param value 文字列
     * @param x 左位置
     * @param y 上位置
     * @param width 幅
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringBottomRight(String value, int x, int y, int width, Font font, Graphics2D graphics2D) {
        int valueWidth = calculateStringWidth(value, font, graphics2D);
        graphics2D.setFont(font);
        graphics2D.drawString(value, x + width - valueWidth, y);
    }

    /**
     * 中央下を基準として文字列を描画します.
     * @param value 文字列
     * @param x 左位置
     * @param y 上位置
     * @param width 幅
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringBottomCenter(String value, int x, int y, int width, Font font, Graphics2D graphics2D) {
        int valueWidth = calculateStringWidth(value, font, graphics2D);
        graphics2D.setFont(font);
        graphics2D.drawString(value, x + (width / 2) - (valueWidth / 2), y);
    }

    /**
     * 文字列を自動改行して箱の中に収まるようにフォントサイズを自動調整します.
     * @param value 文字列
     * @param x 左位置
     * @param y 右位置
     * @param width 幅
     * @param height 高さ
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringAutoMultiLine(String value, int x, int y, int width, int height, Font font, Graphics2D graphics2D) {
        // 変動フォント
        Font newFont = font;
        float fontSize = font.getSize2D();
        boolean adjustFont = false;
        // 行高さ調整変数
        int valueHeight = 0;
        int lineMargin = 0;
        // 開始位置調整変数
        int newY = 0;
        // 一行の文字列を格納
        RudeArray drawLineStrings = new RudeArray();
        // フォントが決定するまでループ
        while (adjustFont == false) {
            // 高さ系の情報を取得
            valueHeight = calculateStringHeight(newFont, graphics2D);
            lineMargin = valueHeight / 3;
            // 開始位置調整
            newY = y + valueHeight;
            // 文字列の初期化
            String tempLine;
            String restValue = value;
            drawLineStrings.clear();
            // 調整後行高さ
            int restHeight = 0;
            // 全文字処理
            while (restValue.length() > 0) {
                tempLine = "";
                // 収まらなくなるまでループ
                while (calculateStringWidth(subString(restValue, 0, tempLine.length() + 1), newFont, graphics2D) <= width) {
                    if (tempLine.length() == restValue.length()) {
                        break;
                    }
                    if (subString(restValue, tempLine.length(), 1).equals(LINE_SEPARATOR)) {
                        tempLine = subString(restValue, 0, tempLine.length() + 1);
                        break;
                    }
                    if (subString(restValue, tempLine.length(), 2).equals(LINE_SEPARATOR)) {
                        tempLine = subString(restValue, 0, tempLine.length() + 2);
                        break;
                    }
                    tempLine = subString(restValue, 0, tempLine.length() + 1);
                }
                // １行の文字列分を引く
                restValue = subString(restValue,tempLine.length());
                // 高さの加算
                restHeight += valueHeight + lineMargin;
                // 高さ上限に達してないか判定
                if (restHeight > height) {
                    break;
                }
                drawLineStrings.add(tempLine);
            }
            // 最後まで高さ上限を超えなければフォント決定
            if (restHeight <= height) {
                adjustFont = true;
            } else {
                // フォントを調整
                fontSize -= 0.5;
                newFont = newFont.deriveFont(fontSize);
            }
        }
        // 実際に描画する
        int lineOffset = 0;
        graphics2D.setFont(newFont);
        for (Object stringObj : drawLineStrings.getValues()) {
            graphics2D.drawString(stringObj.toString(), x, newY + lineOffset);
            lineOffset += valueHeight + lineMargin;
        }
    }

    /**
     * 文字列を自動改行して箱の中に収まるようにフォントサイズを自動調整し中央に配置します.
     * @param value 文字列
     * @param x 左位置
     * @param y 右位置
     * @param width 幅
     * @param height 高さ
     * @param font フォント
     * @param graphics2D インスタンス
     */
    public static void drawStringCenterAutoMultiLine(String value, int x, int y, int width, int height, Font font, Graphics2D graphics2D) {
        // 変動フォント
        Font newFont = font;
        float fontSize = font.getSize2D();
        boolean adjustFont = false;
        // 行高さ調整変数
        int valueHeight = 0;
        int lineMargin = 0;
        // 中央配置用に文字列の高さと幅を保持
        int valueMaxWidth = 0;
        int valueMaxHeight = 0;
        // 開始位置調整変数
        int newY = 0;
        // 一行の文字列を格納
        RudeArray drawLineStrings = new RudeArray();
        // フォントが決定するまでループ
        while (adjustFont == false) {
            // 高さ系の情報を取得
            valueHeight = calculateStringHeight(newFont, graphics2D);
            lineMargin = valueHeight / 3;
            // 開始位置調整
            newY = y + valueHeight;
            // 中央配置用に文字列の高さと幅を保持
            valueMaxWidth = 0;
            valueMaxHeight = 0;
            // 文字列の初期化
            String tempLine;
            String restValue = value;
            drawLineStrings.clear();
            // 調整後行高さ
            int restHeight = 0;
            // 全文字処理
            while (restValue.length() > 0) {
                tempLine = "";
                // 収まらなくなるまでループ
                while (calculateStringWidth(subString(restValue, 0, tempLine.length() + 1), newFont, graphics2D) <= width) {
                    if (tempLine.length() == restValue.length()) {
                        break;
                    }
                    if (subString(restValue, tempLine.length(), 1).equals(LINE_SEPARATOR)) {
                        tempLine = subString(restValue, 0, tempLine.length() + 1);
                        break;
                    }
                    if (subString(restValue, tempLine.length(), 2).equals(LINE_SEPARATOR)) {
                        tempLine = subString(restValue, 0, tempLine.length() + 2);
                        break;
                    }
                    tempLine = subString(restValue, 0, tempLine.length() + 1);
                    // 最大幅を更新
                    if (valueMaxWidth < calculateStringWidth(tempLine, newFont, graphics2D)) {
                        valueMaxWidth = calculateStringWidth(tempLine, newFont, graphics2D);
                    }
                }
                // １行の文字列分を引く
                restValue = subString(restValue,tempLine.length());
                // 高さの加算
                restHeight += valueHeight + lineMargin;
                // 最大高さ更新
                valueMaxHeight += valueHeight + lineMargin;
                // 高さ上限に達してないか判定
                if (restHeight > height) {
                    valueMaxHeight -= lineMargin;
                    break;
                }
                // 描画する値を追加
                drawLineStrings.add(tempLine);
            }
            // 最後まで高さ上限を超えなければフォント決定
            if (restHeight <= height) {
                adjustFont = true;
            } else {
                // フォントを調整
                fontSize -= 0.5;
                newFont = newFont.deriveFont(fontSize);
            }
        }
        // 実際に描画する
        int lineOffset = 0;
        graphics2D.setFont(newFont);
        for (String string : drawLineStrings.getValuesAtString()) {
            graphics2D.drawString(string, x + ((width - valueMaxWidth) / 2), newY + lineOffset + ((height - valueMaxHeight) / 2));
            lineOffset += valueHeight + lineMargin;
        }
    }

}