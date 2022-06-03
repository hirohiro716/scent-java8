package com.hirohiro716.database.sqlite;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.hirohiro716.StringConverter;
import com.hirohiro716.database.AbstractDatabase;
import com.hirohiro716.database.DataNotFoundException;
import com.hirohiro716.database.WhereSet;
import com.hirohiro716.ArrayHelper;
import com.hirohiro716.RudeArray;

/**
 * SQLiteへのJDBC接続を補助するクラス。
 *
 * @author hiro
 *
 */
public class SQLite extends AbstractDatabase {

    /**
     * SQLiteにはBoolean型が無いのでINTEGERで代用する際の有効を表す数値。
     */
    public static final int BOOLEAN_VALUE_ENABLED = 1;
    
    /**
     * SQLiteにはBoolean型が無いのでINTEGERで代用する際の無効を表す数値。
     */
    public static final int BOOLEAN_VALUE_DISABLED = 0;
    
    /**
     * SQLiteのトランザクション分離レベル。
     *
     * @author hiro
     */
    public enum IsolationLevel {
        /**
         * SQL実行時にロックをかける
         */
        DEFERRED,
        /**
         * トランザクション開始時に書き込みロックをかける
         */
        IMMEDIATE,
        /**
         * トランザクション開始時に排他ロックをかける
         */
        EXCLUSIVE,
    }

    private IsolationLevel isolationLevel = null;

    /**
     * 開始されているトランザクションの分離レベルを取得する。
     *
     * @return トランザクションが開始されていなければnull
     */
    public IsolationLevel getIsolationLevel() {
        return this.isolationLevel;
    }

    /**
     * データベースファイルを指定してコネクションを確立する。
     *
     * @param databaseLocation データベースファイル
     */
    @Override
    public void connect(String databaseLocation) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.setConnection(DriverManager.getConnection("jdbc:sqlite:" + databaseLocation));
        this.databaseLocation = databaseLocation;
        this.isolationLevel = null;
    }
    
    private String databaseLocation = null;
    
    /**
     * connectメソッドで指定したデータベースファイル。
     *
     * @return データベースファイル
     */
    public String getDatabaseLocation() {
        return this.databaseLocation;
    }

    /**
     * テーブルが存在するか確認する。
     *
     * @param tableName テーブル名
     * @return 結果
     * @throws SQLException
     */
    public boolean isExistTable(String tableName) throws SQLException {
        try {
            String countString = this.fetchOne(StringConverter.join("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='", tableName, "';"));
            if (Integer.valueOf(countString) == 0) {
                return false;
            }
            return true;
        } catch (DataNotFoundException exception) {
            return false;
        }
    }

    @Override
    public void insert(RudeArray values, String tableName) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        String[] keys = values.getKeysAtString();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(keys[i]);
        }
        sql.subSequence(0, sql.length() - 2);
        sql.append(") VALUES (");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("?");
        }
        sql.subSequence(0, sql.length() - 2);
        sql.append(");");
        this.execute(sql.toString(), values.getValues());
    }

    @Override
    public void update(RudeArray values, String tableName, WhereSet whereSet) throws SQLException, DataNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");
        String[] keys = values.getKeysAtString();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(key);
            sql.append(" = ?");
        }
        sql.append(" WHERE ");
        sql.append(whereSet.buildParameterClause());
        sql.append(";");
        Object[] parameters = ArrayHelper.merge(values.getValues(), whereSet.buildParameters());
        int result = this.execute(sql.toString(), parameters);
        if (result == 0) {
            throw new DataNotFoundException();
        }
    }

    /**
     * トランザクションを開始する。
     *
     * @param isolationLevel 分離レベル。
     *
     * @throws SQLException
     */
    public void begin(IsolationLevel isolationLevel) throws SQLException {
        this.execute(StringConverter.join("BEGIN ", isolationLevel.toString(), ";"));
        this.isolationLevel = isolationLevel;
    }
    
    @Override
    public void commit() throws SQLException {
        this.execute("COMMIT;");
        this.isolationLevel = null;
    }

    @Override
    public void rollback() throws SQLException {
        this.execute("ROLLBACK;");
        this.isolationLevel = null;
    }

    @Override @Deprecated
    public void setAutoCommit(boolean isAutoCommit) throws SQLException {
        super.setAutoCommit(isAutoCommit);
    }
    
    @Override @Deprecated
    public boolean isAutoCommit() {
        return super.isAutoCommit();
    }
}
