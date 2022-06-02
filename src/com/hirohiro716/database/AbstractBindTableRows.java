package com.hirohiro716.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hirohiro716.RudeArray;

/**
 * ORMのようなものを提供する抽象クラス。
 *
 * @author hiro
 */
public abstract class AbstractBindTableRows extends AbstractBindTable {
    
    /**
     * コンストラクタ。
     *
     * @param database 接続済みDatabase
     */
    public AbstractBindTableRows(AbstractDatabase database) {
        super(database);
    }

    private List<RudeArray> rows = new ArrayList<>();
    
    /**
     * 編集中の全レコードの連想配列を取得する。
     *
     * @return 全レコードの連想配列
     */
    public List<RudeArray> getRows() {
        return this.rows;
    }
    
    /**
     * 連想配列を編集中の全レコードとしてセットする。
     *
     * @param rows 全レコードの連想配列
     */
    public void setRows(List<RudeArray> rows) {
        this.rows = rows;
    }
    
    /**
     * 連想配列を編集中の全レコードとしてセットする。
     *
     * @param rows 全レコードの連想配列
     */
    public void setRows(RudeArray[] rows) {
        this.rows.clear();
        for (RudeArray row: rows) {
            this.rows.add(row);
        }
    }
    
    /**
     * 連想配列を編集中のレコードとして追加する。
     *
     * @param row レコードの連想配列
     */
    public void addRow(RudeArray row) {
        this.rows.add(row);
    }
    
    /**
     * 連想配列を編集中のレコード郡から取り除く。
     *
     * @param row 対象のレコード連想配列
     * @return 結果
     */
    public boolean removeRow(RudeArray row) {
        return this.rows.remove(row);
    }
    
    /**
     * 編集中の全レコードの連想配列をすべてクリアする。
     *
     */
    public void clearRows() {
        this.rows.clear();
    }

    /**
     * 編集する全レコードの情報を連想配列として取得して排他処理する。
     *
     * @param afterSQL ORDER句などのFROM句の後に入力するオプション
     * @return 編集された全レコードの連想配列
     * @throws SQLException 
     */
    protected abstract RudeArray[] fetchEditRows(String afterSQL) throws SQLException;
    
    /**
     * 複数のレコードの編集を開始する。
     *
     * @param orderByColumns 並び替えを指定(ASC・DESCを含むカラム名)
     * @throws SQLException
     */
    public void edit(String... orderByColumns) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        if (orderByColumns.length > 0) {
            stringBuilder.append("ORDER BY ");
            for (int i = 0; i < orderByColumns.length; i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(orderByColumns[i]);
            }
        }
        this.setRows(this.fetchEditRows(stringBuilder.toString()));
    }
    
    /**
     * 検索条件が空の状態の上書き(全レコード置き換え)を許可するかどうか。
     *
     * @return 許可する場合はtrue
     */
    public abstract boolean isPermittedSearchConditioEmptyUpdate();
    
    /**
     * 編集している複数のレコードを保持している連想配列に置き換える。
     *
     * @throws SQLException
     */
    public void update() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("DELETE FROM ");
        stringBuilder.append(this.getTableName());
        if (this.getWhereSet() == null) {
            if (this.isPermittedSearchConditioEmptyUpdate() == false) {
                throw new SQLException("検索条件が未指定のため上書き保存できません。");
            }
            stringBuilder.append(";");
            this.getDatabase().execute(stringBuilder.toString());
        } else {
            stringBuilder.append(" WHERE ");
            stringBuilder.append(this.getWhereSet().buildParameterClause());
            stringBuilder.append(";");
            this.getDatabase().execute(stringBuilder.toString(), this.getWhereSet().buildParameters());
        }
        for (RudeArray row: this.rows) {
            this.getDatabase().insert(row, this.getTableName());
        }
    }

    /**
     * レコードが存在するか確認する。
     *
     * @return 存在するかどうか
     * @throws SQLException
     */
    public boolean isExist() throws SQLException {
        if (this.getWhereSet() == null) {
            throw new SQLException("検索条件を指定してください。");
        }
        StringBuilder stringBuilder = new StringBuilder("SELECT COUNT(");
        stringBuilder.append(this.getWhereSet().getWheres().get(0).getColumn());
        stringBuilder.append(") FROM ");
        stringBuilder.append(this.getTableName());
        stringBuilder.append(" WHERE ");
        stringBuilder.append(this.getWhereSet().buildParameterClause());
        stringBuilder.append(";");
        try  {
            String result = this.getDatabase().fetchOne(stringBuilder.toString(), this.getWhereSet().buildParameters());
            if (Integer.parseInt(result) > 0) {
                return true;
            }
        } catch (Exception exception) {
            throw new SQLException(exception);
        }
        return false;
    }}
