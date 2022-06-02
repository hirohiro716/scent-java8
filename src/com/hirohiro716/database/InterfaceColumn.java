package com.hirohiro716.database;

import com.hirohiro716.InterfaceProperty;

/**
 * データベースカラムや設定名などの項目を表すインターフェース。
 *
 * @author hiro
 */
public interface InterfaceColumn extends InterfaceProperty {
    
    /**
     * 項目のフル物理名を取得する。
     *
     * @return 物理名
     */
    public abstract String getFullPhysicalName();
    
}
