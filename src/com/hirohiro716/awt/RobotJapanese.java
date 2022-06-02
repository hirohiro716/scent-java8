package com.hirohiro716.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.hirohiro716.OSHelper;
import com.hirohiro716.robot.InterfaceTypingRobotJapanese;

/**
 * AWTのRobotクラスに日本語用の機能を足したクラス。
 *
 * @author hiro
 */
public class RobotJapanese extends Robot implements InterfaceTypingRobotJapanese<Integer> {

    /**
     * コンストラクタ。
     *
     * @throws AWTException
     */
    public RobotJapanese() throws AWTException {
        super();
    }
    
    @Override
    public void keyType(Integer... keyCodes) {
        for (Integer key : keyCodes) {
            this.keyPress(key);
        }
        for (Integer key : keyCodes) {
            this.keyRelease(key);
        }
    }
    
    private List<Integer[]> changeIMEOffKeys = new ArrayList<>();
    
    /**
     * IMEをOFFにするためのホットキーをセットする。
     *
     * @param keyCodes
     */
    public void setChangeIMEOffKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeIMEOffKeys.add(key);
        }
    }
    
    @Override
    public void changeIMEOff() {
        if (this.changeIMEOffKeys.size() > 0) {
            for (Integer[] key: this.changeIMEOffKeys) {
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

    private List<Integer[]> changeIMEHiraganaKeys = new ArrayList<>();
    
    /**
     * IMEをひらがなにするためのホットキーをセットする。
     *
     * @param keyCodes
     */
    public void setChangeIMEHiraganaKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeIMEHiraganaKeys.add(key);
        }
    }
    
    @Override
    public void changeIMEHiragana() {
        if (this.changeIMEHiraganaKeys.size() > 0) {
            for (Integer[] key: this.changeIMEHiraganaKeys) {
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

    private List<Integer[]> changeIMEKatakanaKeys = new ArrayList<>();
    
    /**
     * IMEをカタカナにするためのホットキーをセットする。
     *
     * @param keyCodes
     */
    public void setChangeIMEKatakanaKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeIMEKatakanaKeys.add(key);
        }
    }
    
    @Override
    public void changeIMEKatakanaWide() {
        if (this.changeIMEKatakanaKeys.size() > 0) {
            for (Integer[] key: this.changeIMEKatakanaKeys) {
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

    private List<Integer[]> changeIMEKatakanaNarrowKeys = new ArrayList<>();
    
    /**
     * IMEをｶﾀｶﾅにするためのホットキーをセットする。
     *
     * @param keyCodes
     */
    public void setChangeIMEKatakanaNarrowKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeIMEKatakanaKeys.add(key);
        }
    }
    
    @Override
    public void changeIMEKatakanaNarrow() {
        if (this.changeIMEKatakanaNarrowKeys.size() > 0) {
            for (Integer[] key: this.changeIMEKatakanaNarrowKeys) {
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
}