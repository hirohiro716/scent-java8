package com.hirohiro716.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.hirohiro716.OSHelper;
import com.hirohiro716.robot.InterfaceTypingRobotJapanese;

/**
 * AWTのRobotクラスに日本語用の機能を足したクラス.
 * @author hiro
 */
public class RobotJapanese extends Robot implements InterfaceTypingRobotJapanese<Integer> {

    /**
     * コンストラクタ.
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
    
    private List<Integer[]> changeImeOffKeys = new ArrayList<>();
    
    /**
     * IMEをOFFにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeOffKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeImeOffKeys.add(key);
        }
    }
    
    @Override
    public void changeImeOff() {
        if (this.changeImeOffKeys.size() > 0) {
            for (Integer[] key: this.changeImeOffKeys) {
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

    private List<Integer[]> changeImeHiraganaKeys = new ArrayList<>();
    
    /**
     * IMEをひらがなにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeHiraganaKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeImeHiraganaKeys.add(key);
        }
    }
    
    @Override
    public void changeImeHiragana() {
        if (this.changeImeHiraganaKeys.size() > 0) {
            for (Integer[] key: this.changeImeHiraganaKeys) {
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

    private List<Integer[]> changeImeKatakanaKeys = new ArrayList<>();
    
    /**
     * IMEをカタカナにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeKatakanaKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeImeKatakanaKeys.add(key);
        }
    }
    
    @Override
    public void changeImeKatakanaWide() {
        if (this.changeImeKatakanaKeys.size() > 0) {
            for (Integer[] key: this.changeImeKatakanaKeys) {
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

    private List<Integer[]> changeImeKatakanaNarrowKeys = new ArrayList<>();
    
    /**
     * IMEをｶﾀｶﾅにするためのホットキーをセットする.
     * @param keyCodes
     */
    public void setChangeImeKatakanaNarrowKeys(Integer[]... keyCodes) {
        for (Integer[] key: keyCodes) {
            this.changeImeKatakanaKeys.add(key);
        }
    }
    
    @Override
    public void changeImeKatakanaNarrow() {
        if (this.changeImeKatakanaNarrowKeys.size() > 0) {
            for (Integer[] key: this.changeImeKatakanaNarrowKeys) {
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