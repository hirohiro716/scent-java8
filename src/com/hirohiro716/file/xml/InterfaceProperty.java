package com.hirohiro716.file.xml;

import com.hirohiro716.database.InterfaceColumn;

/**
 * プロパティXMLの項目名を表すインターフェース.
 * @author hiro
 */
public interface InterfaceProperty extends InterfaceColumn {
    
    @Override
    default String getFullPhysicalName() {
        return this.getPhysicalName();
    }
    
}
