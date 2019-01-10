package com.hirohiro716.database;

import java.sql.SQLException;

import com.hirohiro716.RudeArray;

/**
 * ORMのようなものを提供する抽象クラス.
 * @author hiro
 */
public abstract class AbstractBindTableRow extends AbstractBindTable {

    /**
     * コンストラクタ.
     * @param database
     */
    public AbstractBindTableRow(AbstractDatabase database) {
        super(database);
        this.setDefaultRow();
    }

    private RudeArray row;
    
    /**
     * 編集中レコードの連想配列を取得する.
     * @return レコードの連想配列
     */
    public RudeArray getRow() {
        return this.row;
    }
    
    /**
     * 連想配列を編集中のレコードとしてセットする.
     * @param row
     */
    public void setRow(RudeArray row) {
        this.row = row;
    }
    
    /**
     * 初期値が入力されたレコード用の連想配列を編集中のレコードとしてセットする.
     */
    public void setDefaultRow() {
        this.row = this.createDefaultRow();
    }
    
    /**
     * 保持している情報をテーブルに追加する.
     * @throws SQLException
     */
    public void insert() throws SQLException {
        this.getDatabase().insert(this.row, this.getTableName());
    }
    
    /**
     * レコードの情報を連想配列として取得して排他処理する.
     * @return 編集されたレコードの連想配列
     * @throws SQLException 
     * @throws DataNotFoundException 
     */
    protected abstract RudeArray fetchEditRow() throws SQLException, DataNotFoundException;
    
    /**
     * レコードの編集を開始する.
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public void edit() throws SQLException, DataNotFoundException {
        this.setRow(this.fetchEditRow());
        if (this.isDeleted()) {
            throw new DataNotFoundException();
        }
    }
    
    /**
     * 取得したレコードが削除済みかどうかの判定メソッド. これはedit(WhereSet)メソッドから自動的に呼び出され編集するかの判定に使われる.
     * @return 削除済みかどうか
     */
    public abstract boolean isDeleted();
    
    /**
     * 編集中のレコードを保持している連想配列で更新する.
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public void update() throws SQLException, DataNotFoundException {
        if (this.getWhereSet() == null) {
            throw new SQLException("Invalid operation because search condition is not set.");
        }
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(this.getTableName());
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(";");
        this.getDatabase().update(this.row, stringBuilder.toString(), this.getWhereSet().buildParameters());
    }
    
    /**
     * レコードを物理削除する.
     * @throws SQLException
     * @throws DataNotFoundException
     */
    protected void physicalDelete() throws SQLException, DataNotFoundException {
        if (this.getWhereSet() == null) {
            throw new SQLException("検索条件を指定してください。");
        }
        StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
        stringBuilder.append(this.getTableName());
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(";");
        int result = this.getDatabase().execute(stringBuilder.toString(), this.getWhereSet().buildParameters());
        if (result == 0) {
            throw new DataNotFoundException();
        }
    }
    
    /**
     * 編集中のレコードを削除する.
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public abstract void delete() throws SQLException, DataNotFoundException;

    /**
     * レコードが存在するか確認する.
     * @return 存在するかどうか
     * @throws SQLException
     */
    public boolean isExist() throws SQLException {
        if (this.getWhereSet() == null) {
            throw new SQLException("検索条件を指定してください。");
        }
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(this.getTableName());
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(";");
        RudeArray oldRow = this.getRow();
        try  {
            this.setRow(this.getDatabase().fetchRow(stringBuilder.toString(), this.getWhereSet().buildParameters()));
            if (this.isDeleted() == false) {
                return true;
            }
        } catch (DataNotFoundException exception) {
        } finally {
            this.setRow(oldRow);
        }
        return false;
    }
    
}
