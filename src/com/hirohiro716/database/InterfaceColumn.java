package com.hirohiro716.database;

/**
 * データベースカラムや設定名などの項目を表すインターフェース.
 * @author hiro
 */
public interface InterfaceColumn {
    
    /**
     * 項目の日本語名を取得する.
     * @return 日本語名
     */
    public String getLogicalName();
    
    /**
     * 項目の物理名を取得する.
     * @return 物理名
     */
    public default String getPhysicalName() {
        return this.toString().toLowerCase();
    }

    /**
     * 項目のフル物理名を取得する.
     * @return 物理名
     */
    public String getFullPhysicalName();

    /**
     * 項目の初期値を取得する.
     * @return 初期値
     */
    public Object getDefaultValue();
    
    /**
     * 項目値の最大文字数を取得する.(-1は無制限を示す)
     * @return 最大文字数(-1は無制限を示す)
     */
    public int getMaxLength();
}
