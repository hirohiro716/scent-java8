package com.hirohiro716;

import static com.hirohiro716.StringConverter.*;

/**
 * 例外を処理するための補助メソッドクラス.
 * @author hiro
 */
public class ExceptionHelper {
    
    /**
     * 例外から詳細メッセージを作成する.
     * @param exception 発生例外
     * @return 詳細メッセージ
     */
    public static String createDetailMessage(Exception exception) {
        return createDetailMessage(exception.getMessage(), exception);
    }
    
    /**
     * 新しいメッセージと例外から詳細メッセージを作成する.
     * @param newMessage 新しいメッセージ
     * @param exception 発生例外
     * @return 詳細メッセージ
     */
    public static String createDetailMessage(String newMessage, Exception exception) {
        exception.printStackTrace();
        StringBuilder message = new StringBuilder();
        if (newMessage != null && newMessage.length() > 0) {
            message.append(newMessage);
            message.append(LINE_SEPARATOR);
        }
        message.append(exception.getClass().getName());
        return message.toString();
    }
    
}
