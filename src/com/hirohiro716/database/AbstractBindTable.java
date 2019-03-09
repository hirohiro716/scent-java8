package com.hirohiro716.database;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hirohiro716.RudeArray;
import com.hirohiro716.StringConverter;
import com.hirohiro716.file.xml.InterfaceProperty;

/**
 * ORMのようなものを提供する抽象クラス.
 * @author hiro
 */
public abstract class AbstractBindTable {

    /**
     * コンストラクタ.
     * @param database
     */
    public AbstractBindTable(AbstractDatabase database) {
        this.database = database;
    }

    private AbstractDatabase database;
    
    /**
     * コンストラクタで指定したデータベースオブジェクトを取得する.
     * @return AbstractDatabase
     */
    public AbstractDatabase getDatabase() {
        return this.database;
    }
    
    /**
     * データベースオブジェクトを再指定する.
     * @param database
     */
    protected void setDatabase(AbstractDatabase database) {
        this.database = database;
    }
    
    /**
     * テーブル名を取得する.
     * @return テーブル名
     */
    public abstract String getTableName();

    /**
     * テーブル名を取得する.
     * @param <T> テーブル名を求めるAbstractBindTableを継承したクラス
     * @param <D> Tのクラスで使用されているデータベースクラス
     * @param tableClass テーブル名を求めるクラス
     * @param databaseClass テーブル名のクラスで使用されているデータベースクラス
     * @return テーブル名
     */
    public static <T extends AbstractBindTable, D extends AbstractDatabase> String getTableName(Class<T> tableClass, Class<D> databaseClass) {
        try {
            AbstractDatabase database = null;
            T instance = tableClass.getConstructor(databaseClass).newInstance(database);
            return instance.getTableName();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * テーブルの説明を取得する.
     * @return テーブルの説明
     */
    public abstract String getDescription();

    /**
     * テーブルの説明を取得する.
     * @param <T> テーブルの説明を求めるAbstractBindTableを継承したクラス
     * @param <D> Tのクラスで使用されているデータベースクラス
     * @param tableClass テーブルの説明を求めるクラス
     * @param databaseClass テーブルの説明のクラスで使用されているデータベースクラス
     * @return テーブルの説明
     */
    public static <T extends AbstractBindTable, D extends AbstractDatabase> String getDescription(Class<T> tableClass, Class<D> databaseClass) {
        try {
            AbstractDatabase database = null;
            T instance = tableClass.getConstructor(databaseClass).newInstance(database);
            return instance.getDescription();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * 初期値が入力されたレコード用の連想配列を取得する.
     * @return 連想配列
     */
    public abstract RudeArray createDefaultRow();

    /**
     * 初期値が入力されたレコード用の連想配列を取得する.
     * @param columns カラム一覧
     * @return 連想配列
     */
    public static RudeArray createDefaultRow(InterfaceColumn[] columns) {
    	return createDefaultRow((InterfaceProperty[]) columns);
    }

    /**
     * 初期値が入力されたレコード用の連想配列を取得する.
     * @param columns カラム一覧
     * @return 連想配列
     */
    public static RudeArray createDefaultRow(InterfaceProperty[] columns) {
        RudeArray row = new RudeArray();
        for (InterfaceColumn column: columns) {
            row.put(column.getPhysicalName(), column.getDefaultValue());
        }
        return row;
    }

    private WhereSet whereSet = null;
    
    /**
     * 編集・更新・削除に使用するレコード特定用のWhereSetを取得する.
     * @return whereSet
     */
    protected WhereSet getWhereSet() {
        return this.whereSet;
    }
    
    /**
     * 編集・更新・削除に使用するレコード特定用のWhereSetを指定する.<br>
     * 編集する前は必ずこのメソッドを使用して抽出条件を指定する.
     * @param whereSet 
     */
    public void setWhereSet(WhereSet whereSet) {
        this.whereSet = whereSet;
    }

    /**
     * 保持している連想配列が有効か検証する.
     * @throws ValidationException
     * @throws Exception 
     */
    public abstract void validate() throws ValidationException, Exception;
    
    /**
     * 保持している連想配列を標準化（全角を半角に変換したり）する.
     * @throws Exception
     */
    public abstract void normalize() throws Exception;

    /**
     * レコードを検索する.
     * @param selectSQL WHERE句の前のSELECT句
     * @param afterSQL WHERE句の後に付与するオプション句
     * @param whereSetArray 検索条件（複数指定するとOR検索になる）
     * @return 検索結果
     * @throws SQLException
     */
    protected RudeArray[] search(String selectSQL, String afterSQL, WhereSet... whereSetArray) throws SQLException {
        String sql = selectSQL;
        if (whereSetArray.length > 0) {
            sql += " WHERE ";
        }
        StringBuilder builder = new StringBuilder();
        ArrayList<Object> params = new ArrayList<>();
        for (WhereSet whereSet: whereSetArray) {
            if (builder.length() > 0) {
                builder.append(" OR ");
            }
            builder.append(whereSet.buildParameterClause());
            for (Object param: whereSet.buildParameters()) {
                params.add(param);
            }
        }
        sql = StringConverter.join(sql, builder.toString(), " ", afterSQL, ";");
        return this.getDatabase().fetchRows(sql, params.toArray());
    }

    /**
     * レコードを検索する.
     * @param whereSetArray 検索条件（複数指定するとOR検索になる）
     * @return 検索結果
     * @throws SQLException
     */
    public RudeArray[] search(WhereSet... whereSetArray) throws SQLException {
        return this.search(StringConverter.join("SELECT * FROM ", this.getTableName()), "", whereSetArray);
    }

    /**
     * レコードを検索する.
     * @param afterSQL WHERE句の後に付与するオプション句
     * @param whereSetArray 検索条件（複数指定するとOR検索になる）
     * @return 検索結果
     * @throws SQLException
     */
    public RudeArray[] search(String afterSQL, WhereSet... whereSetArray) throws SQLException {
        return this.search(StringConverter.join("SELECT * FROM ", this.getTableName()), afterSQL, whereSetArray);
    }

}
