package com.hirohiro716.database.postgresql;

import java.sql.SQLException;

import com.hirohiro716.RudeArray;
import com.hirohiro716.database.DataNotFoundException;

/**
 * ORMのようなものを提供する抽象クラス.
 * @author hiro
 */
public abstract class AbstractBindTableRow extends com.hirohiro716.database.AbstractBindTableRow {

    /**
     * コンストラクタ.
     * @param postgreSQL
     */
    public AbstractBindTableRow(PostgreSQL postgreSQL) {
        super(postgreSQL);
    }

    @Override
    public PostgreSQL getDatabase() {
        return (PostgreSQL) super.getDatabase();
    }

    @Override
    protected RudeArray fetchEditRow() throws SQLException, DataNotFoundException {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(this.getTableName());
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(" FOR UPDATE NOWAIT;");
        return this.getDatabase().fetchRow(stringBuilder.toString(), this.getWhereSet().buildParameters());
    }

}
