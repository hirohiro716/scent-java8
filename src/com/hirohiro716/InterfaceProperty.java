package com.hirohiro716;

/**
 * 設定項目などを表すインターフェース。
 *
 * @author hiro
 *
 */
public interface InterfaceProperty {
    
    /**
     * 項目の日本語名を取得する。
     *
     * @return 日本語名
     */
    public abstract String getLogicalName();
    
    /**
     * 項目の物理名を取得する。
     *
     * @return 物理名
     */
    public default String getPhysicalName() {
        return this.toString().toLowerCase();
    }
    
    /**
     * 項目の初期値を取得する。
     *
     * @return 初期値
     */
    public abstract Object getDefaultValue();
    
    /**
     * 項目値の最大文字数を取得する。(-1は無制限を示す)
     * @return 最大文字数(-1は無制限を示す)
     */
    public abstract int getMaxLength();
    
}
