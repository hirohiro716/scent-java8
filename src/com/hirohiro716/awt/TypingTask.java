package com.hirohiro716.awt;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.util.Collection;

import com.hirohiro716.robot.AbstractTypingTask;

/**
 * AWTで自動キー入力を行うクラス.
 * @author hiro
 */
public class TypingTask extends AbstractTypingTask<Integer> {
    
    /**
     * コンストラクタ.
     * @throws AWTException
     */
    public TypingTask() throws AWTException {
        this.robot = new RobotJapanese();
    }
    
    private RobotJapanese robot;
    
    @Override
    public void keyType(Collection<Integer> keyCodes) {
        this.robot.keyType(keyCodes.toArray(new Integer[] {}));
    }

    @Override
    public Integer findTypingKeyCode(KeyCode keyCode) {
        switch (keyCode) {
        case A:
        case B:
        case C:
        case D:
        case E:
        case F:
        case G:
        case H:
        case I:
        case J:
        case K:
        case L:
        case M:
        case N:
        case O:
        case P:
        case Q:
        case R:
        case S:
        case T:
        case U:
        case V:
        case W:
        case X:
        case Y:
        case Z:
            return KeyEvent.getExtendedKeyCodeForChar(keyCode.toString().charAt(0));
        case ENTER:
            return KeyEvent.VK_ENTER;
        case ESCAPE:
            return KeyEvent.VK_ESCAPE;
        case DELETE:
            return KeyEvent.VK_DELETE;
        case BACKSPACE:
            return KeyEvent.VK_BACK_SPACE;
        case SHIFT:
            return KeyEvent.VK_SHIFT;
        case CONTROL:
            return KeyEvent.VK_CONTROL;
        case ALT:
            return KeyEvent.VK_ALT;
        case TAB:
            return KeyEvent.VK_TAB;
        case HOME:
            return KeyEvent.VK_HOME;
        case END:
            return KeyEvent.VK_END;
        case UNDEFINED:
            break;
        }
        return KeyEvent.VK_UNDEFINED;
    }

    @Override
    public KeyCode findTaskKeyCode(Integer keyCode) {
        switch (keyCode) {
        case KeyEvent.VK_A:
        case KeyEvent.VK_B:
        case KeyEvent.VK_C:
        case KeyEvent.VK_D:
        case KeyEvent.VK_E:
        case KeyEvent.VK_F:
        case KeyEvent.VK_G:
        case KeyEvent.VK_H:
        case KeyEvent.VK_I:
        case KeyEvent.VK_J:
        case KeyEvent.VK_K:
        case KeyEvent.VK_L:
        case KeyEvent.VK_M:
        case KeyEvent.VK_N:
        case KeyEvent.VK_O:
        case KeyEvent.VK_P:
        case KeyEvent.VK_Q:
        case KeyEvent.VK_R:
        case KeyEvent.VK_S:
        case KeyEvent.VK_T:
        case KeyEvent.VK_U:
        case KeyEvent.VK_V:
        case KeyEvent.VK_W:
        case KeyEvent.VK_X:
        case KeyEvent.VK_Y:
        case KeyEvent.VK_Z:
            return KeyCode.find(KeyEvent.getKeyText(keyCode));
        case KeyEvent.VK_ENTER:
            return KeyCode.ENTER;
        case KeyEvent.VK_ESCAPE:
            return KeyCode.ESCAPE;
        case KeyEvent.VK_DELETE:
            return KeyCode.DELETE;
        case KeyEvent.VK_BACK_SPACE:
            return KeyCode.BACKSPACE;
        case KeyEvent.VK_SHIFT:
            return KeyCode.SHIFT;
        case KeyEvent.VK_CONTROL:
            return KeyCode.CONTROL;
        case KeyEvent.VK_ALT:
            return KeyCode.ALT;
        case KeyEvent.VK_TAB:
            return KeyCode.TAB;
        case KeyEvent.VK_HOME:
            return KeyCode.HOME;
        case KeyEvent.VK_END:
            return KeyCode.END;
        }
        return KeyCode.UNDEFINED;
    }
    
}
