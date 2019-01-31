package com.hirohiro716.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.hirohiro716.RegexHelper.RegexPattern;
import com.hirohiro716.InterfaceKeyInputRobotJapanese;
import com.hirohiro716.OSHelper;
import com.hirohiro716.RudeArray;

/**
 * AWTのRobotクラスに日本語用の機能を足したクラス.
 * @author hiro
 */
public class RobotJapanese extends Robot implements InterfaceKeyInputRobotJapanese {

    /**
     * コンストラクタ.
     * @throws AWTException
     */
    public RobotJapanese() throws AWTException {
        super();
    }
    
    @Override
    public void keyType(int... keyCodes) {
        for (int key : keyCodes) {
            this.keyPress(key);
        }
        for (int key : keyCodes) {
            this.keyRelease(key);
        }
    }
    
    private List<int[]> changeImeOffKeys = new ArrayList<>();
    
    /**
     * IMEをOFFにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeOffKeys(int[]... keyCodes) {
        for (int[] key: keyCodes) {
            this.changeImeOffKeys.add(key);
        }
    }
    
    @Override
    public void changeImeOff() {
        if (this.changeImeOffKeys.size() > 0) {
            for (int[] key: this.changeImeOffKeys) {
                this.keyType(key);
            }
            this.waitForIdle();
        } else {
            switch (OSHelper.findOS()) {
            case WINDOWS:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_INPUT_METHOD_ON_OFF);
                this.waitForIdle();
                break;
            case LINUX:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_HALF_WIDTH);
                this.keyType(KeyEvent.VK_SHIFT, KeyEvent.VK_NONCONVERT);
                this.waitForIdle();
                break;
            case MAC:
                this.keyType(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON);
                this.waitForIdle();
                break;
            case UNKNOWN:
                break;
            }
        }
    }

    private List<int[]> changeImeHiraganaKeys = new ArrayList<>();
    
    /**
     * IMEをひらがなにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeHiraganaKeys(int[]... keyCodes) {
        for (int[] key: keyCodes) {
            this.changeImeHiraganaKeys.add(key);
        }
    }
    
    @Override
    public void changeImeHiragana() {
        if (this.changeImeHiraganaKeys.size() > 0) {
            for (int[] key: this.changeImeHiraganaKeys) {
                this.keyType(key);
            }
        } else {
            switch (OSHelper.findOS()) {
            case WINDOWS:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.waitForIdle();
                break;
            case LINUX:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.waitForIdle();
                break;
            case MAC:
                this.keyType(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_J);
                this.waitForIdle();
                break;
            case UNKNOWN:
                break;
            }
        }
    }

    private List<int[]> changeImeKatakanaKeys = new ArrayList<>();
    
    /**
     * IMEをカタカナにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeKatakanaKeys(int[]... keyCodes) {
        for (int[] key: keyCodes) {
            this.changeImeKatakanaKeys.add(key);
        }
    }
    
    @Override
    public void changeImeKatakanaWide() {
        if (this.changeImeKatakanaKeys.size() > 0) {
            for (int[] key: this.changeImeKatakanaKeys) {
                this.keyType(key);
            }
        } else {
            switch (OSHelper.findOS()) {
            case WINDOWS:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_KATAKANA);
                this.waitForIdle();
                break;
            case LINUX:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_KATAKANA);
                this.waitForIdle();
                break;
            case MAC:
                this.keyType(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_K);
                this.waitForIdle();
                break;
            case UNKNOWN:
                break;
            }
        }
    }

    private List<int[]> changeImeKatakanaNarrowKeys = new ArrayList<>();
    
    /**
     * IMEをｶﾀｶﾅにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeKatakanaNarrowKeys(int[]... keyCodes) {
        for (int[] key: keyCodes) {
            this.changeImeKatakanaKeys.add(key);
        }
    }
    
    @Override
    public void changeImeKatakanaNarrow() {
        if (this.changeImeKatakanaNarrowKeys.size() > 0) {
            for (int[] key: this.changeImeKatakanaNarrowKeys) {
                this.keyType(key);
            }
        } else {
            switch (OSHelper.findOS()) {
            case WINDOWS:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_KATAKANA);
                this.keyType(KeyEvent.VK_NONCONVERT);
                this.waitForIdle();
                break;
            case LINUX:
                this.keyType(KeyEvent.VK_HIRAGANA);
                this.keyType(KeyEvent.VK_KATAKANA);
                this.keyType(KeyEvent.VK_NONCONVERT);
                this.waitForIdle();
                break;
            case MAC:
                this.keyType(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_K);
                this.waitForIdle();
                break;
            case UNKNOWN:
                break;
            }
        }
    }

    private static final RudeArray withShiftKeyTypedList = new RudeArray();

    /**
     * Shiftキーと同時押しで入力する記号などをタイプ
     * @param word 記号など
     * @return 結果
     */
    public boolean getKeyTypeWithShift(String word) {
        if (withShiftKeyTypedList.size() == 0) {
            withShiftKeyTypedList.put("!", KeyEvent.VK_1);
            withShiftKeyTypedList.put("\\", KeyEvent.VK_2);
            withShiftKeyTypedList.put("#", KeyEvent.VK_3);
            withShiftKeyTypedList.put("$", KeyEvent.VK_4);
            withShiftKeyTypedList.put("%", KeyEvent.VK_5);
            withShiftKeyTypedList.put("&", KeyEvent.VK_6);
            withShiftKeyTypedList.put("'", KeyEvent.VK_7);
            withShiftKeyTypedList.put("(", KeyEvent.VK_8);
            withShiftKeyTypedList.put(")", KeyEvent.VK_9);
            withShiftKeyTypedList.put("=", KeyEvent.VK_MINUS);
            withShiftKeyTypedList.put("~", KeyEvent.VK_CIRCUMFLEX);
            withShiftKeyTypedList.put("|", KeyEvent.VK_BACK_SLASH);
            withShiftKeyTypedList.put("`", KeyEvent.VK_AT);
            withShiftKeyTypedList.put("{", KeyEvent.VK_BRACELEFT);
            withShiftKeyTypedList.put("}", KeyEvent.VK_BRACERIGHT);
            withShiftKeyTypedList.put("+", KeyEvent.VK_SEMICOLON);
            withShiftKeyTypedList.put("*", KeyEvent.VK_COLON);
            withShiftKeyTypedList.put("<", KeyEvent.VK_COMMA);
            withShiftKeyTypedList.put(">", KeyEvent.VK_PERIOD);
            withShiftKeyTypedList.put("?", KeyEvent.VK_SLASH);
        }
        if (withShiftKeyTypedList.get(word) != null) {
            this.keyType(KeyEvent.VK_SHIFT, (int) RobotJapanese.withShiftKeyTypedList.get(word));
            this.waitForIdle();
            return true;
        }
        return false;
    }

    private static final RudeArray japaneseKeyTypeList = new RudeArray();
    
    /**
     * ひらがなとカタカナの文字列入力をエミュレート
     * @param japanese ひらがなまたはカタカナ
     * @return 結果
     */
    public boolean getKeyTypeJapanese(String japanese) {
        if (japaneseKeyTypeList.size() == 0) {
            japaneseKeyTypeList.put("あ", new int[] {KeyEvent.VK_A});
            japaneseKeyTypeList.put("い", new int[] {KeyEvent.VK_I});
            japaneseKeyTypeList.put("う", new int[] {KeyEvent.VK_U});
            japaneseKeyTypeList.put("え", new int[] {KeyEvent.VK_E});
            japaneseKeyTypeList.put("お", new int[] {KeyEvent.VK_O});
            japaneseKeyTypeList.put("か", new int[] {KeyEvent.VK_K, KeyEvent.VK_A});
            japaneseKeyTypeList.put("き", new int[] {KeyEvent.VK_K, KeyEvent.VK_I});
            japaneseKeyTypeList.put("く", new int[] {KeyEvent.VK_K, KeyEvent.VK_U});
            japaneseKeyTypeList.put("け", new int[] {KeyEvent.VK_K, KeyEvent.VK_E});
            japaneseKeyTypeList.put("こ", new int[] {KeyEvent.VK_K, KeyEvent.VK_O});
            japaneseKeyTypeList.put("さ", new int[] {KeyEvent.VK_S, KeyEvent.VK_A});
            japaneseKeyTypeList.put("し", new int[] {KeyEvent.VK_S, KeyEvent.VK_I});
            japaneseKeyTypeList.put("す", new int[] {KeyEvent.VK_S, KeyEvent.VK_U});
            japaneseKeyTypeList.put("せ", new int[] {KeyEvent.VK_S, KeyEvent.VK_E});
            japaneseKeyTypeList.put("そ", new int[] {KeyEvent.VK_S, KeyEvent.VK_O});
            japaneseKeyTypeList.put("た", new int[] {KeyEvent.VK_T, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ち", new int[] {KeyEvent.VK_T, KeyEvent.VK_I});
            japaneseKeyTypeList.put("つ", new int[] {KeyEvent.VK_T, KeyEvent.VK_U});
            japaneseKeyTypeList.put("て", new int[] {KeyEvent.VK_T, KeyEvent.VK_E});
            japaneseKeyTypeList.put("と", new int[] {KeyEvent.VK_T, KeyEvent.VK_O});
            japaneseKeyTypeList.put("な", new int[] {KeyEvent.VK_N, KeyEvent.VK_A});
            japaneseKeyTypeList.put("に", new int[] {KeyEvent.VK_N, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ぬ", new int[] {KeyEvent.VK_N, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ね", new int[] {KeyEvent.VK_N, KeyEvent.VK_E});
            japaneseKeyTypeList.put("の", new int[] {KeyEvent.VK_N, KeyEvent.VK_O});
            japaneseKeyTypeList.put("は", new int[] {KeyEvent.VK_H, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ひ", new int[] {KeyEvent.VK_H, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ふ", new int[] {KeyEvent.VK_H, KeyEvent.VK_U});
            japaneseKeyTypeList.put("へ", new int[] {KeyEvent.VK_H, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ほ", new int[] {KeyEvent.VK_H, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ま", new int[] {KeyEvent.VK_M, KeyEvent.VK_A});
            japaneseKeyTypeList.put("み", new int[] {KeyEvent.VK_M, KeyEvent.VK_I});
            japaneseKeyTypeList.put("む", new int[] {KeyEvent.VK_M, KeyEvent.VK_U});
            japaneseKeyTypeList.put("め", new int[] {KeyEvent.VK_M, KeyEvent.VK_E});
            japaneseKeyTypeList.put("も", new int[] {KeyEvent.VK_M, KeyEvent.VK_O});
            japaneseKeyTypeList.put("や", new int[] {KeyEvent.VK_Y, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ゆ", new int[] {KeyEvent.VK_Y, KeyEvent.VK_U});
            japaneseKeyTypeList.put("よ", new int[] {KeyEvent.VK_Y, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ら", new int[] {KeyEvent.VK_R, KeyEvent.VK_A});
            japaneseKeyTypeList.put("り", new int[] {KeyEvent.VK_R, KeyEvent.VK_I});
            japaneseKeyTypeList.put("る", new int[] {KeyEvent.VK_R, KeyEvent.VK_U});
            japaneseKeyTypeList.put("れ", new int[] {KeyEvent.VK_R, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ろ", new int[] {KeyEvent.VK_R, KeyEvent.VK_O});
            japaneseKeyTypeList.put("わ", new int[] {KeyEvent.VK_W, KeyEvent.VK_A});
            japaneseKeyTypeList.put("を", new int[] {KeyEvent.VK_W, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ん", new int[] {KeyEvent.VK_N, KeyEvent.VK_N});
            japaneseKeyTypeList.put("が", new int[] {KeyEvent.VK_G, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ぎ", new int[] {KeyEvent.VK_G, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ぐ", new int[] {KeyEvent.VK_G, KeyEvent.VK_U});
            japaneseKeyTypeList.put("げ", new int[] {KeyEvent.VK_G, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ご", new int[] {KeyEvent.VK_G, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ざ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_A});
            japaneseKeyTypeList.put("じ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ず", new int[] {KeyEvent.VK_Z, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ぜ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ぞ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_O});
            japaneseKeyTypeList.put("だ", new int[] {KeyEvent.VK_D, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ぢ", new int[] {KeyEvent.VK_D, KeyEvent.VK_I});
            japaneseKeyTypeList.put("づ", new int[] {KeyEvent.VK_D, KeyEvent.VK_U});
            japaneseKeyTypeList.put("で", new int[] {KeyEvent.VK_D, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ど", new int[] {KeyEvent.VK_D, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ば", new int[] {KeyEvent.VK_B, KeyEvent.VK_A});
            japaneseKeyTypeList.put("び", new int[] {KeyEvent.VK_B, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ぶ", new int[] {KeyEvent.VK_B, KeyEvent.VK_U});
            japaneseKeyTypeList.put("べ", new int[] {KeyEvent.VK_B, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ぼ", new int[] {KeyEvent.VK_B, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ぱ", new int[] {KeyEvent.VK_P, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ぴ", new int[] {KeyEvent.VK_P, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ぷ", new int[] {KeyEvent.VK_P, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ぺ", new int[] {KeyEvent.VK_P, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ぽ", new int[] {KeyEvent.VK_P, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ぁ", new int[] {KeyEvent.VK_X, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ぃ", new int[] {KeyEvent.VK_X, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ぅ", new int[] {KeyEvent.VK_X, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ぇ", new int[] {KeyEvent.VK_X, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ぉ", new int[] {KeyEvent.VK_X, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ゃ", new int[] {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ゅ", new int[] {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ょ", new int[] {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_O});
            japaneseKeyTypeList.put("っ", new int[] {KeyEvent.VK_X, KeyEvent.VK_T, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ー", new int[] {KeyEvent.VK_MINUS});
            japaneseKeyTypeList.put("、", new int[] {KeyEvent.VK_COMMA});
            japaneseKeyTypeList.put("。", new int[] {KeyEvent.VK_PERIOD});
            japaneseKeyTypeList.put("ア", new int[] {KeyEvent.VK_A});
            japaneseKeyTypeList.put("イ", new int[] {KeyEvent.VK_I});
            japaneseKeyTypeList.put("ウ", new int[] {KeyEvent.VK_U});
            japaneseKeyTypeList.put("エ", new int[] {KeyEvent.VK_E});
            japaneseKeyTypeList.put("オ", new int[] {KeyEvent.VK_O});
            japaneseKeyTypeList.put("カ", new int[] {KeyEvent.VK_K, KeyEvent.VK_A});
            japaneseKeyTypeList.put("キ", new int[] {KeyEvent.VK_K, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ク", new int[] {KeyEvent.VK_K, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ケ", new int[] {KeyEvent.VK_K, KeyEvent.VK_E});
            japaneseKeyTypeList.put("コ", new int[] {KeyEvent.VK_K, KeyEvent.VK_O});
            japaneseKeyTypeList.put("サ", new int[] {KeyEvent.VK_S, KeyEvent.VK_A});
            japaneseKeyTypeList.put("シ", new int[] {KeyEvent.VK_S, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ス", new int[] {KeyEvent.VK_S, KeyEvent.VK_U});
            japaneseKeyTypeList.put("セ", new int[] {KeyEvent.VK_S, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ソ", new int[] {KeyEvent.VK_S, KeyEvent.VK_O});
            japaneseKeyTypeList.put("タ", new int[] {KeyEvent.VK_T, KeyEvent.VK_A});
            japaneseKeyTypeList.put("チ", new int[] {KeyEvent.VK_T, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ツ", new int[] {KeyEvent.VK_T, KeyEvent.VK_U});
            japaneseKeyTypeList.put("テ", new int[] {KeyEvent.VK_T, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ト", new int[] {KeyEvent.VK_T, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ナ", new int[] {KeyEvent.VK_N, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ニ", new int[] {KeyEvent.VK_N, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ヌ", new int[] {KeyEvent.VK_N, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ネ", new int[] {KeyEvent.VK_N, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ノ", new int[] {KeyEvent.VK_N, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ハ", new int[] {KeyEvent.VK_H, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ヒ", new int[] {KeyEvent.VK_H, KeyEvent.VK_I});
            japaneseKeyTypeList.put("フ", new int[] {KeyEvent.VK_H, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ヘ", new int[] {KeyEvent.VK_H, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ホ", new int[] {KeyEvent.VK_H, KeyEvent.VK_O});
            japaneseKeyTypeList.put("マ", new int[] {KeyEvent.VK_M, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ミ", new int[] {KeyEvent.VK_M, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ム", new int[] {KeyEvent.VK_M, KeyEvent.VK_U});
            japaneseKeyTypeList.put("メ", new int[] {KeyEvent.VK_M, KeyEvent.VK_E});
            japaneseKeyTypeList.put("モ", new int[] {KeyEvent.VK_M, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ヤ", new int[] {KeyEvent.VK_Y, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ユ", new int[] {KeyEvent.VK_Y, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ヨ", new int[] {KeyEvent.VK_Y, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ラ", new int[] {KeyEvent.VK_R, KeyEvent.VK_A});
            japaneseKeyTypeList.put("リ", new int[] {KeyEvent.VK_R, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ル", new int[] {KeyEvent.VK_R, KeyEvent.VK_U});
            japaneseKeyTypeList.put("レ", new int[] {KeyEvent.VK_R, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ロ", new int[] {KeyEvent.VK_R, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ワ", new int[] {KeyEvent.VK_W, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ヲ", new int[] {KeyEvent.VK_W, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ン", new int[] {KeyEvent.VK_N, KeyEvent.VK_N});
            japaneseKeyTypeList.put("ガ", new int[] {KeyEvent.VK_G, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ギ", new int[] {KeyEvent.VK_G, KeyEvent.VK_I});
            japaneseKeyTypeList.put("グ", new int[] {KeyEvent.VK_G, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ゲ", new int[] {KeyEvent.VK_G, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ゴ", new int[] {KeyEvent.VK_G, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ザ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ジ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ズ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ゼ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ゾ", new int[] {KeyEvent.VK_Z, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ダ", new int[] {KeyEvent.VK_D, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ヂ", new int[] {KeyEvent.VK_D, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ヅ", new int[] {KeyEvent.VK_D, KeyEvent.VK_U});
            japaneseKeyTypeList.put("デ", new int[] {KeyEvent.VK_D, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ド", new int[] {KeyEvent.VK_D, KeyEvent.VK_O});
            japaneseKeyTypeList.put("バ", new int[] {KeyEvent.VK_B, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ビ", new int[] {KeyEvent.VK_B, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ブ", new int[] {KeyEvent.VK_B, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ベ", new int[] {KeyEvent.VK_B, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ボ", new int[] {KeyEvent.VK_B, KeyEvent.VK_O});
            japaneseKeyTypeList.put("パ", new int[] {KeyEvent.VK_P, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ピ", new int[] {KeyEvent.VK_P, KeyEvent.VK_I});
            japaneseKeyTypeList.put("プ", new int[] {KeyEvent.VK_P, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ペ", new int[] {KeyEvent.VK_P, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ポ", new int[] {KeyEvent.VK_P, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ァ", new int[] {KeyEvent.VK_X, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ィ", new int[] {KeyEvent.VK_X, KeyEvent.VK_I});
            japaneseKeyTypeList.put("ゥ", new int[] {KeyEvent.VK_X, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ェ", new int[] {KeyEvent.VK_X, KeyEvent.VK_E});
            japaneseKeyTypeList.put("ォ", new int[] {KeyEvent.VK_X, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ャ", new int[] {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_A});
            japaneseKeyTypeList.put("ュ", new int[] {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_U});
            japaneseKeyTypeList.put("ョ", new int[] {KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_O});
            japaneseKeyTypeList.put("ッ", new int[] {KeyEvent.VK_X, KeyEvent.VK_T, KeyEvent.VK_U});
        }
        if (japaneseKeyTypeList.get(japanese) != null) {
            if (RegexPattern.HIRAGANA_ONLY.getPattern().matcher(japanese).find()) {
                this.changeImeHiragana();
            }
            if (RegexPattern.KATAKANA_WIDE_ONLY.getPattern().matcher(japanese).find()) {
                this.changeImeKatakanaWide();
            }
            int[] keys = (int[]) RobotJapanese.japaneseKeyTypeList.get(japanese);
            for (int key : keys) {
                this.keyType(key);
            }
            this.waitForIdle();
            return true;
        }
        return false;
    }
}