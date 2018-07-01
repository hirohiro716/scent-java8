package com.hirohiro716.database;

import com.hirohiro716.RudeArray;

/**
 * 値の検証に失敗した場合に発生する例外クラス
 * @author hiro
 */
public class ValidationException extends com.hirohiro716.validate.ValidationException {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1733617763538002291L;

    /**
     * コンストラクタ. 値検証失敗の新規例外を構築する.
     * @param message 詳細メッセージ
     * @param causeRow 例外の原因となった行
     * @param causeColumn 例外の原因となった項目
     */
    public ValidationException(String message, RudeArray causeRow, InterfaceColumn causeColumn) {
        super(message);
        this.causeRow = causeRow;
        this.causeColumn = causeColumn;
    }

    /**
     * コンストラクタ. 値検証失敗の新規例外を構築する.
     * @param message 詳細メッセージ
     * @param causeColumn 例外の原因となった項目
     */
    public ValidationException(String message, InterfaceColumn causeColumn) {
        super(message);
        this.causeColumn = causeColumn;
    }
    
    private RudeArray causeRow;
    
    /**
     * 例外の原因となった行を取得する.
     * @return DynamicTypeArray
     */
    public RudeArray getCauseRow() {
        return this.causeRow;
    }

    private InterfaceColumn causeColumn;
    
    /**
     * 例外の原因となった項目を取得する.
     * @return InterfaceColumn
     */
    public InterfaceColumn getCauseColumn() {
        return this.causeColumn;
    }
    
}
