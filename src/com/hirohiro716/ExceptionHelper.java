package com.hirohiro716;

import static com.hirohiro716.StringConverter.*;

/**
 * 例外を処理するための補助メソッドクラス。
 *
 * @author hiro
 *
 */
public class ExceptionHelper {
    
    /**
     * 例外から詳細メッセージを作成する。
     *
     * @param exception 発生例外
     * @return 詳細メッセージ
     */
    public static String createDetailMessage(Exception exception) {
        return createDetailMessage(exception.getMessage(), exception);
    }
    
    /**
     * 新しいメッセージと例外から詳細メッセージを作成する。
     *
     * @param newMessage 新しいメッセージ
     * @param exception 発生例外
     * @return 詳細メッセージ
     */
    public static String createDetailMessage(String newMessage, Exception exception) {
        exception.printStackTrace();
        StringBuilder message = new StringBuilder(newMessage);
        if (message.length() > 0) {
            message.append(LINE_SEPARATOR);
            message.append(LINE_SEPARATOR);
        }
        message.append("An exception occurred in ");
        StackTraceElement stackTraceElement = exception.getStackTrace()[0];
        message.append(stackTraceElement.getClassName());
        message.append(":");
        message.append(stackTraceElement.getLineNumber());
        message.append(LINE_SEPARATOR);
        if (exception.getMessage() != null && exception.getMessage().length() > 0) {
            message.append(exception.getClass().getName());
            if (message.indexOf(exception.getMessage()) == -1) {
                message.append(": ");
                message.append(exception.getMessage());
            }
        } else {
            message.append(exception.getClass().getName());
        }
        return message.toString();
    }}
