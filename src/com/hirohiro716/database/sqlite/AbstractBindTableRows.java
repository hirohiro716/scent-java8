package com.hirohiro716.database.sqlite;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

import com.hirohiro716.RudeArray;
import com.hirohiro716.database.sqlite.SQLite.IsolationLevel;

/**
 * ORMのようなものを提供する抽象クラス。
 *
 * @author hiro
 */
public abstract class AbstractBindTableRows extends com.hirohiro716.database.AbstractBindTableRows implements Closeable {

    /**
     * コンストラクタ。
     *
     * @param sqlite
     */
    public AbstractBindTableRows(SQLite sqlite) {
        super(sqlite);
    }

    /**
     * 内部のデータベースオブジェクトを取得する。
     *
     * @return SQLite
     */
    @Override
    public SQLite getDatabase() {
        return (SQLite) super.getDatabase();
    }
    
    @Override
    protected RudeArray[] fetchEditRows(String afterSQL) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(this.getTableName());
        if (this.getWhereSet() == null) {
            // 検索条件がない場合
            stringBuilder.append(" ");
            stringBuilder.append(afterSQL);
            stringBuilder.append(";");
            return this.getDatabase().fetchRows(stringBuilder.toString());
        }
        // 検索条件がある場合
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(" ");
        stringBuilder.append(afterSQL);
        stringBuilder.append(";");
        return this.getDatabase().fetchRows(stringBuilder.toString(), this.getWhereSet().buildParameters());
    }
    
    private boolean isEditMode = false;
    
    @Override
    public void edit(String... orders) throws SQLException {
        if (this.isEditMode) {
            return;
        }
        super.edit(orders);
        try (SQLite sqlite = new SQLite()) {
            sqlite.connect(this.getDatabase().getDatabaseLocation());
            sqlite.begin(IsolationLevel.EXCLUSIVE);
            if (this.isEditing(sqlite)) {
                throw new SQLException(SQLite.ERROR_MESSAGE_EDITING_FAILURE);
            }
            this.updateToEditing(sqlite);
            sqlite.commit();
            this.isEditMode = true;
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * レコードが編集中かどうかの判定メソッド。これはedit()メソッドから自動的に呼び出され編集するかの判定に使われる。
     *
     * @param sqlite 分離レベルEXCLUSIVEでトランザクションが開始されたDatabase
     * @return 編集中かどうか
     * @throws SQLException 
     */
    public abstract boolean isEditing(SQLite sqlite) throws SQLException;
    
    /**
     * レコードを編集中に変更する。これはedit()メソッドから自動的に呼び出される。
     *
     * @param sqlite 分離レベルEXCLUSIVEでトランザクションが開始されたDatabase
     * @throws SQLException 
     */
    protected abstract void updateToEditing(SQLite sqlite) throws SQLException;
    
    /**
     * 編集中を解除する。これはclose()メソッドから自動的に呼び出される。
     *
     * @param sqlite 分離レベルEXCLUSIVEでトランザクションが開始されたDatabase
     * @throws SQLException
     */
    protected abstract void updateToEditingFinish(SQLite sqlite) throws SQLException;
    
    /**
     * 編集中を解除してデータを閉じる。
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            if (this.isEditMode) {
                String location = this.getDatabase().getDatabaseLocation();
                this.getDatabase().close();
                try (SQLite sqlite = new SQLite()) {
                    sqlite.connect(location);
                    sqlite.begin(IsolationLevel.EXCLUSIVE);
                    this.updateToEditingFinish(sqlite);
                    sqlite.commit();
                } catch (ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
                this.isEditMode = false;
            }
            this.getRows().clear();
        } catch (SQLException exception) {
            throw new IOException(exception);
        }
    }
}
