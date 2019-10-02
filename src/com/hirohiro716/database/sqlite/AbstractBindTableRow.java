package com.hirohiro716.database.sqlite;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

import com.hirohiro716.RudeArray;
import com.hirohiro716.database.DataNotFoundException;
import com.hirohiro716.database.sqlite.SQLite.IsolationLevel;

/**
 * ORMのようなものを提供する抽象クラス.
 * @author hiro
 */
public abstract class AbstractBindTableRow extends com.hirohiro716.database.AbstractBindTableRow implements Closeable {
    
    /**
     * コンストラクタ.
     * @param sqlite 
     */
    public AbstractBindTableRow(SQLite sqlite) {
        super(sqlite);
    }

    /**
     * コンストラクタで指定したデータベースオブジェクトを取得する.
     * @return SQLite
     */
    @Override
    public SQLite getDatabase() {
        return (SQLite) super.getDatabase();
    }
    
    @Override
    protected RudeArray fetchEditRow() throws SQLException, DataNotFoundException {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(this.getTableName());
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(";");
        return this.getDatabase().fetchRow(stringBuilder.toString(), this.getWhereSet().buildParameters());
    }

    private boolean isEditMode = false;
    
    /**
     * レコードの編集を開始する.
     * @throws SQLException 
     * @throws DataNotFoundException 
     */
    @Override
    public void edit() throws SQLException, DataNotFoundException {
        if (this.isEditMode) {
            return;
        }
        super.edit();
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
     * 取得したレコードが編集中かどうかの判定メソッド. これはedit(WhereSet)メソッドから自動的に呼び出され編集するかの判定に使われる.
     * @param sqlite 分離レベルEXCLUSIVEでトランザクションが開始されたDatabase
     * @return 編集中かどうか
     */
    public abstract boolean isEditing(SQLite sqlite);
    
    /**
     * 編集中に変更する. これはedit(WhereSet)メソッドから自動的に呼び出される.
     * @param sqlite 分離レベルEXCLUSIVEでトランザクションが開始されたDatabase
     * @throws SQLException 
     * @throws DataNotFoundException 
     */
    protected abstract void updateToEditing(SQLite sqlite) throws SQLException, DataNotFoundException;

    /**
     * 編集中のレコードを保持している連想配列で更新する.
     * @throws SQLException
     * @throws DataNotFoundException
     */
    @Override
    public void update() throws SQLException, DataNotFoundException {
        this.getDatabase().update(this.getRow(), this.getTableName(), this.getWhereSet());
    }
    
    /**
     * 編集中を解除する. これはclose()メソッドから自動的に呼び出される.
     * @throws SQLException
     * @throws DataNotFoundException
     */
    protected abstract void updateToEditingFinish() throws SQLException, DataNotFoundException;
    
    /**
     * 編集中を解除してデータを閉じる.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            if (this.isEditMode) {
                this.updateToEditingFinish();
                if (this.getDatabase().getIsolationLevel() != null) {
                    switch (this.getDatabase().getIsolationLevel()) {
                    case DEFERRED:
                    case IMMEDIATE:
                    case EXCLUSIVE:
                        this.getDatabase().commit();
                        break;
                    }
                }
                this.isEditMode = false;
            }
            this.setRow(null);
        } catch (SQLException exception) {
            throw new IOException(exception);
        } catch (DataNotFoundException exception) {
        }
    }
    
}
