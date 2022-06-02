package com.hirohiro716.database.postgresql;

import java.sql.SQLException;

import com.hirohiro716.RudeArray;

/**
 * ORMのようなものを提供する抽象クラス。
 *
 * @author hiro
 */
public abstract class AbstractBindTableRows extends com.hirohiro716.database.AbstractBindTableRows {

    /**
     * コンストラクタ。
     *
     * @param postgreSQL
     */
    public AbstractBindTableRows(PostgreSQL postgreSQL) {
        super(postgreSQL);
    }
    
    @Override
    public PostgreSQL getDatabase() {
        return (PostgreSQL) super.getDatabase();
    }

    @Override
    protected RudeArray[] fetchEditRows(String afterSQL) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(this.getTableName());
        if (this.getWhereSet() == null) {
            // 検索条件がない場合はテーブルごとロック
            stringBuilder.append(" ");
            stringBuilder.append(afterSQL);
            stringBuilder.append(";");
            this.getDatabase().lockTableReadonly(this.getTableName());
            return this.getDatabase().fetchRows(stringBuilder.toString());
        }
        // 検索条件がある場合は行ロック
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(" ");
        stringBuilder.append(afterSQL);
        stringBuilder.append(" FOR UPDATE NOWAIT");
        stringBuilder.append(";");
        return this.getDatabase().fetchRows(stringBuilder.toString(), this.getWhereSet().buildParameters());
    }

}
