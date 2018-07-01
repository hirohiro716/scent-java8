package com.hirohiro716.file.xml;

/**
 * 値の検証に失敗した場合に発生する例外クラス
 * @author hiro
 */
public class ValidationException extends com.hirohiro716.validate.ValidationException {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4635778972111023471L;

    /**
     * コンストラクタ. 値検証失敗の新規例外を構築する.
     * @param message 詳細メッセージ
     * @param causeProperty 例外の原因となったプロパティ
     */
    public ValidationException(String message, InterfaceProperty causeProperty) {
        super(message);
        this.causeProperty = causeProperty;
    }
    
    private InterfaceProperty causeProperty;
    
    /**
     * 例外の原因となったプロパティを取得する.
     * @return InterfaceProperty
     */
    public InterfaceProperty getCauseProperty() {
        return this.causeProperty;
    }
    
}
