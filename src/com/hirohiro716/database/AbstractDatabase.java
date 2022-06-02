package com.hirohiro716.database;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.sql.Date;

import com.hirohiro716.StringConverter;
import com.hirohiro716.datetime.Datetime;
import com.hirohiro716.RudeArray;

/**
 * データベース処理の抽象クラス。
 *
 * @author hiro
 */
public abstract class AbstractDatabase implements Closeable {

    /**
     * ResultSetの更新タイプ
     * @author hiro
     */
    public enum ResultSetChange {
        /**
         * 読み取り専用
         */
        READ_ONLY,
        /**
         * 更新可能
         */
        UPDATABLE;
    }

    /**
     * ResultSetのカーソルタイプ
     * @author hiro
     */
    public enum ResultSetCursor {
        /**
         * 前方参照
         */
        FORWARD_ONLY,
        /**
         * 両方参照 他のセッションによる更新を考慮しない
         */
        SCROLL_INSENSITIVE,
        /**
         * 両方参照 他のセッションによる更新を考慮する
         */
        SCROLL_SENSITIVE;
    }

    /**
     * "データベースエラー" というダイアログタイトル用の文字列
     */
    public static final String ERROR_DIALOG_TITLE = "データベースエラー";

    /**
     * "データ取得失敗" というダイアログタイトル用の文字列
     */
    public static final String ERROR_DIALOG_TITLE_GET_FAILURE = "データ取得失敗";

    /**
     * "ほかのユーザーが編集中です。" というエラーメッセージ用の文字列
     */
    public static final String ERROR_MESSAGE_EDITING_FAILURE = "ほかのユーザーが編集中です。";

    /**
     * "トランザクションが開始されていません。" というエラーメッセージ用の文字列
     */
    public static final String ERROR_MESSAGE_TRANSACTION_NOT_BEGUN = "トランザクションが開始されていません。";

    // コネクション保持
    private Connection connection;

    /**
     * JDBCコネクションを取得する。
     *
     * @return コネクション
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * JDBCコネクションをセットする。
     *
     * @param connection コネクション
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private int timeout = 0;

    /**
     * タイムアウト(秒数)を取得する。
     *
     * @return タイムアウト秒数
     */
    public int getStatementTimeout() {
        return this.timeout;
    }

    /**
     * タイムアウト(秒数)を設定する。
     *
     * @param timeout タイムアウト秒数
     */
    public void setStatementTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 接続文字列を指定してコネクションを確立する。
     *
     * @param connectionString 接続文字列
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public abstract void connect(String connectionString) throws ClassNotFoundException, SQLException;
    
    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            // nop
        }
    }
    
    /**
     * コネクションが閉じられているかどうかを取得する。
     *
     * @return 結果
     * @throws SQLException
     */
    public boolean isClosed() throws SQLException {
        return (this.connection == null || this.connection.isClosed());
    }

    /**
     * 更新系SQLを実行する。
     *
     * @param sql 更新系SQL
     * @return 更新レコード数
     * @throws SQLException
     */
    public int execute(String sql) throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            statement.setQueryTimeout(this.timeout);
            return statement.executeUpdate(sql);
        }
    }

    /**
     * プリペアードステートメントで更新系SQLを実行する。
     *
     * @param sql 更新系SQL
     * @param params パラメータ
     * @return 更新レコード数
     * @throws SQLException
     */
    public int execute(String sql, Object[] params) throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setQueryTimeout(this.timeout);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, castSearchValue(params[i]));
            }
            return statement.executeUpdate();
        }
    }

    /**
     * プリペアードステートメントで更新系SQLを複数回実行する。
     *
     * @param sql 更新系SQL
     * @param paramsArray パラメータの２次元配列
     * @return 更新レコード数
     * @throws SQLException
     */
    public int execute(String sql, Collection<Object[]> paramsArray) throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setQueryTimeout(this.timeout);
            int updateCount = 0;
            for (Object[] params: paramsArray) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, castSearchValue(params[i]));
                }
                updateCount += statement.executeUpdate();
            }
            return updateCount;
        }
    }

    /**
     * プリペアードステートメントを内部で保持し何度も更新系SQLを実行する。
     *
     * @author hiro
     */
    public class PreparedStatementEach implements Closeable {

        private AbstractDatabase database = AbstractDatabase.this;

        private PreparedStatement statement;

        /**
         * コンストラクタで更新系SQLを指定する。
         *
         * @param sql 更新系SQL
         * @throws SQLException
         */
        public PreparedStatementEach(String sql) throws SQLException {
            this.statement = this.database.connection.prepareStatement(sql);
            this.statement.setQueryTimeout(this.database.timeout);
        }

        /**
         * コンストラクタで指定したSQLに対してパラメータのみを指定して更新を実行する。
         *
         * @param params パラメータ
         * @throws SQLException
         */
        public void execute(Object[] params) throws SQLException {
            for (int i = 0; i < params.length; i++) {
                this.statement.setObject(i + 1, castSearchValue(params[i]));
            }
            this.updateCount += this.statement.executeUpdate();
        }

        private int updateCount = 0;

        /**
         * 今までに更新した行数を取得する。
         *
         * @return 更新された行数
         */
        public int getUpdateCount() {
            return this.updateCount;
        }

        @Override
        public void close() throws IOException {
            try {
                this.statement.close();
            } catch (Exception exception) {
                // nop
            }
        }
    }

    /**
     * SELECT結果の最初のレコード. 最初のフィールドの値を取得する。
     *
     * @param sql
     * @return 値
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public String fetchOne(String sql) throws SQLException, DataNotFoundException {
        try (Statement statement = this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setQueryTimeout(this.timeout);
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    return resultSet.getObject(1).toString();
                }
                throw new DataNotFoundException();
            }
        }
    }

    /**
     * プリペアードステートメントでSELECT結果の最初のレコード. 最初のフィールドの値を取得する。
     *
     * @param sql
     * @param params パラメータ
     * @return 値
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public String fetchOne(String sql, Object[] params) throws SQLException, DataNotFoundException {
        try (PreparedStatement statement = this.connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setQueryTimeout(this.timeout);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, castSearchValue(params[i]));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getObject(1).toString();
                }
            }
        } catch (SQLException exception) {
            throw exception;
        } catch (NullPointerException exception) {
            // nop
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        throw new DataNotFoundException();
    }

    /**
     * SELECT結果の最初のレコードを連想配列で取得する。
     *
     * @param sql
     * @return 連想配列
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public RudeArray fetchRow(String sql) throws SQLException, DataNotFoundException {
        try (Statement statement = this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setQueryTimeout(this.timeout);
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    RudeArray row = new RudeArray();
                    ResultSetMetaData meta = resultSet.getMetaData();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(meta.getColumnName(i), castDatabaseValue(resultSet.getObject(i)));
                    }
                    return row;
                }
                throw new DataNotFoundException();
            }
        }
    }

    /**
     * プリペアードステートメントでSELECT結果の最初のレコードを連想配列で取得する。
     *
     * @param sql
     * @param params パラメータ
     * @return 連想配列
     * @throws SQLException
     * @throws DataNotFoundException
     */
    public RudeArray fetchRow(String sql, Object[] params) throws SQLException, DataNotFoundException {
        try (PreparedStatement statement = this.connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setQueryTimeout(this.timeout);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, castSearchValue(params[i]));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RudeArray row = new RudeArray();
                    ResultSetMetaData meta = resultSet.getMetaData();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(meta.getColumnName(i), castDatabaseValue(resultSet.getObject(i)));
                    }
                    return row;
                }
                throw new DataNotFoundException();
            }
       }
    }

    /**
     * SELECT結果のレコードを連想配列で取得する。
     *
     * @param sql
     * @return 複数の連想配列
     * @throws SQLException
     */
    public RudeArray[] fetchRows(String sql) throws SQLException {
        try (Statement statement = this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setQueryTimeout(this.timeout);
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                RudeArray rows = new RudeArray();
                while (resultSet.next()) {
                    RudeArray row = new RudeArray();
                    ResultSetMetaData meta = resultSet.getMetaData();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(meta.getColumnName(i), castDatabaseValue(resultSet.getObject(i)));
                    }
                    rows.add(row);
                }
                return rows.getValuesAtRudeArray();
            }
        }
    }

    /**
     * プリペアードステートメントでSELECT結果のレコードを連想配列で取得する。
     *
     * @param sql
     * @param params パラメータ
     * @return 複数の連想配列
     * @throws SQLException
     */
    public RudeArray[] fetchRows(String sql, Object[] params) throws SQLException {
        try (PreparedStatement statement = this.connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setQueryTimeout(this.timeout);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, castSearchValue(params[i]));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                RudeArray rows = new RudeArray();
                while (resultSet.next()) {
                    RudeArray row = new RudeArray();
                    ResultSetMetaData meta = resultSet.getMetaData();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.put(meta.getColumnName(i), castDatabaseValue(resultSet.getObject(i)));
                    }
                    rows.add(row);
                }
                return rows.getValuesAtRudeArray();
            }
        }
    }
    
    /**
     * 検索に使用するオブジェクトをデータベースで使用できる適当な型にキャストする。
     *
     * @param value 元のオブジェクト
     * @return キャストしたオブジェクト
     */
    private static Object castSearchValue(Object value) {
        if (value == null) {
            return value;
        }
        switch (value.getClass().getName()) {
        case "java.util.Date":
            java.util.Date date = (java.util.Date) value;
            return new Timestamp(date.getTime());
        default:
            return value;
        }
    }

    /**
     * データベースから取得した値をjavaで使用する適当な型にキャストする。
     *
     * @param value 元のオブジェクト
     * @return キャストしたオブジェクト
     */
    private static Object castDatabaseValue(Object value) {
        if (value == null) {
            return value;
        }
        switch (value.getClass().getName()) {
        case "java.sql.Timestamp":
            Timestamp timestamp = (Timestamp) value;
            return new Date(timestamp.getTime());
        case "java.math.BigDecimal":
            BigDecimal bigDecimal = (BigDecimal) value;
            return bigDecimal.doubleValue();
        default:
            return value;
        }
    }

    /**
     * テーブルのレコード数を取得する。
     *
     * @param tableName テーブル名
     * @return レコード数
     * @throws SQLException
     * @throws DataNotFoundException 
     */
    public long count(String tableName) throws SQLException, DataNotFoundException {
        return StringConverter.stringToLong(this.fetchOne(StringConverter.join("SELECT COUNT(*) FROM ", tableName, ";")));
    }

    /**
     * 連想配列の情報をテーブルに追加する。
     *
     * @param values 連想配列
     * @param tableName テーブル名
     * @throws SQLException
     */
    public void insert(RudeArray values, String tableName) throws SQLException {
        try (Statement statement = this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            statement.setQueryTimeout(this.timeout);
            statement.setFetchSize(1);
            try (ResultSet resultSet = statement.executeQuery(StringConverter.join("SELECT * FROM ", tableName, ";"))) {
                ResultSetMetaData meta = resultSet.getMetaData();
                resultSet.moveToInsertRow();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    String columnName = meta.getColumnName(i);
                    if (values.containsKey(columnName)) {
                        applyValueForResultSet(resultSet, i, values.get(columnName));
                    }
                }
                resultSet.insertRow();
            }
        }
    }

    /**
     * 抽出できたレコードをすべて連想配列の情報で更新する。
     *
     * @param values 連想配列
     * @param tableName テーブル名
     * @param whereSet 更新対象の抽出条件
     * @throws SQLException
     * @throws DataNotFoundException 
     */
    public void update(RudeArray values, String tableName, WhereSet whereSet) throws SQLException, DataNotFoundException {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(whereSet.buildParameterClause());
        stringBuilder.append(";");
        try (PreparedStatement statement = this.connection.prepareStatement(stringBuilder.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            statement.setQueryTimeout(this.timeout);
            Object[] params = whereSet.buildParameters();
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, castSearchValue(params[i]));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean isUpdated = false;
                ResultSetMetaData meta = resultSet.getMetaData();
                while (resultSet.next()) {
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        String columnName = meta.getColumnName(i);
                        if (values.containsKey(columnName)) {
                            applyValueForResultSet(resultSet, i, values.get(columnName));
                        }
                    }
                    resultSet.updateRow();
                    isUpdated = true;
                }
                if (isUpdated == false) {
                    throw new DataNotFoundException();
                }
            }
        }
    }

    /**
     * SELECTで抽出できたレコードをすべて連想配列の情報で更新する。
     *
     * @param values 連想配列
     * @param sql 更新対象を抽出するSQL
     * @throws SQLException
     * @throws DataNotFoundException 
     */
    public void update(RudeArray values, String sql) throws SQLException, DataNotFoundException {
        try (Statement statement = this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            statement.setQueryTimeout(this.timeout);
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                boolean isUpdated = false;
                ResultSetMetaData meta = resultSet.getMetaData();
                while (resultSet.next()) {
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        String columnName = meta.getColumnName(i);
                        if (values.containsKey(columnName)) {
                            applyValueForResultSet(resultSet, i, values.get(columnName));
                        }
                    }
                    resultSet.updateRow();
                    isUpdated = true;
                }
                if (isUpdated == false) {
                    throw new DataNotFoundException();
                }
            }
        }
    }

    /**
     * SELECTで抽出できたレコードをすべて連想配列の情報で更新する。
     *
     * @param values 連想配列
     * @param sql 更新対象を抽出するSQL
     * @param params パラメータ
     * @throws SQLException
     * @throws DataNotFoundException 
     */
    public void update(RudeArray values, String sql, Object[] params) throws SQLException, DataNotFoundException {
        try (PreparedStatement statement = this.connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            statement.setQueryTimeout(this.timeout);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, castSearchValue(params[i]));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean isUpdated = false;
                ResultSetMetaData meta = resultSet.getMetaData();
                while (resultSet.next()) {
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        String columnName = meta.getColumnName(i);
                        if (values.containsKey(columnName)) {
                            applyValueForResultSet(resultSet, i, values.get(columnName));
                        }
                    }
                    resultSet.updateRow();
                    isUpdated = true;
                }
                if (isUpdated == false) {
                    throw new DataNotFoundException();
                }
            }
        }
    }

    /**
     * ResultSetに適宜値を変換の上入力する。
     *
     * @param resultSet 対象ResultSet
     * @param columnIndex カラム番号(１から開始)
     * @param value 入力する値
     * @throws SQLException
     */
    private static void applyValueForResultSet(ResultSet resultSet, int columnIndex, Object value) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        switch (meta.getColumnType(columnIndex)) {
        case Types.BOOLEAN:
            Boolean booleanValue;
            try {
                try {
                    booleanValue = (boolean) value;
                } catch (ClassCastException exception) {
                    booleanValue = StringConverter.stringToBoolean(value.toString());
                }
            } catch (NullPointerException exception) {
                booleanValue = false;
            }
            resultSet.updateBoolean(columnIndex, booleanValue);
            break;
        case Types.INTEGER:
        case Types.SMALLINT:
        case Types.TINYINT:
            Integer integerValue;
            try {
                try {
                    integerValue = (int) value;
                    resultSet.updateInt(columnIndex, integerValue);
                } catch (ClassCastException exception) {
                    integerValue = StringConverter.stringToInteger(value.toString());
                    resultSet.updateInt(columnIndex, integerValue);
                }
            } catch (NullPointerException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.BIGINT:
            Long longValue;
            try {
                try {
                    longValue = (long) value;
                    resultSet.updateLong(columnIndex, longValue);
                } catch (ClassCastException exception) {
                    longValue = StringConverter.stringToLong(value.toString());
                    resultSet.updateLong(columnIndex, longValue);
                }
            } catch (NullPointerException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.REAL:
            Float floatValue;
            try {
                try {
                    floatValue = (float) value;
                    resultSet.updateFloat(columnIndex, floatValue);
                } catch (ClassCastException exception) {
                    floatValue = StringConverter.stringToFloat(value.toString());
                    resultSet.updateFloat(columnIndex, floatValue);
                }
            } catch (NullPointerException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.DOUBLE:
        case Types.FLOAT:
        case Types.DECIMAL:
        case Types.NUMERIC:
            Double doubleValue;
            try {
                try {
                    doubleValue = (double) value;
                    resultSet.updateDouble(columnIndex, doubleValue);
                } catch (ClassCastException exception) {
                    doubleValue = StringConverter.stringToDouble(value.toString());
                    resultSet.updateDouble(columnIndex, doubleValue);
                }
            } catch (NullPointerException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.NCLOB:
            String stringValue;
            try {
                stringValue = value.toString();
                resultSet.updateString(columnIndex, stringValue);
            } catch (NullPointerException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.NCHAR:
        case Types.NVARCHAR:
        case Types.LONGNVARCHAR:
            String nStringValue;
            try {
                nStringValue = value.toString();
                resultSet.updateNString(columnIndex, nStringValue);
            } catch (NullPointerException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
            try {
                resultSet.updateBytes(columnIndex, (byte[]) value);
            } catch (ClassCastException exception) {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.DATE:
            Date dateValue;
            // java.sql.Date
            try {
                dateValue = (Date) value;
            } catch (ClassCastException exception) {
                // Timestamp
                try {
                    dateValue = new Date(((Timestamp) value).getTime());
                } catch (ClassCastException exception2) {
                    // java.util.Date
                    try {
                        dateValue = new Date(((java.util.Date) value).getTime());
                    } catch (ClassCastException exception3) {
                        dateValue = new Date(Datetime.stringToDate(value.toString()).getTime());
                    }
                }
            } catch (NullPointerException exception) {
                dateValue = null;
            }
            if (dateValue != null) {
                resultSet.updateDate(columnIndex, dateValue);
            } else {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.TIME:
            Time timeValue;
            // Time
            try {
                timeValue = (Time) value;
            } catch (ClassCastException exception) {
                // Timestamp
                try {
                    timeValue = new Time(((Timestamp) value).getTime());
                } catch (ClassCastException exception2) {
                    // java.util.Date
                    try {
                        timeValue = new Time(((java.util.Date) value).getTime());
                    } catch (ClassCastException exception3) {
                        timeValue = new Time(Datetime.stringToDate(value.toString()).getTime());
                    }
                    timeValue = new Time(Datetime.stringToDate(value.toString()).getTime());
                }
            } catch (NullPointerException exception) {
                timeValue = null;
            }
            if (timeValue != null) {
                resultSet.updateTime(columnIndex, timeValue);
            } else {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        case Types.TIMESTAMP:
            Timestamp timestampValue;
            // Timestamp
            try {
                timestampValue = (Timestamp) value;
            } catch (ClassCastException exception) {
                // java.util.Date
                try {
                    timestampValue = new Timestamp(((java.util.Date) value).getTime());
                } catch (ClassCastException exception2) {
                    // java.sql.Date
                    try {
                        timestampValue = new Timestamp(((Date) value).getTime());
                    } catch (ClassCastException exception3) {
                        timestampValue = new Timestamp(Datetime.stringToDate(value.toString()).getTime());
                    }
                }
            } catch (NullPointerException exception) {
                timestampValue = null;
            }
            if (timestampValue != null) {
                resultSet.updateTimestamp(columnIndex, timestampValue);
            } else {
                resultSet.updateObject(columnIndex, null);
            }
            break;
        default:
            resultSet.updateObject(columnIndex, value);
        }
    }

    /**
     * AutoCommitが設定されているかを取得する。
     *
     * @return 結果
     */
    public boolean isAutoCommit() {
        try {
            return this.connection.getAutoCommit();
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * AutoCommitを設定する。
     *
     * @param isAutoCommit
     * @throws SQLException
     */
    public void setAutoCommit(boolean isAutoCommit) throws SQLException {
        this.connection.setAutoCommit(isAutoCommit);
    }

    /**
     * トランザクションをコミットする。
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        this.connection.commit();
    }

    /**
     * トランザクションをロールバックする。
     *
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    /**
     * Integerをキー/Stringが値のHashMapに準じたSQLのCASE文を生成する。
     *
     * @param hashMap 対象連想配列
     * @param columnName 対象カラム
     * @return CASE文
     */
    public static String makeCaseSqlFromHashMapInteger(HashMap<Integer, String> hashMap, String columnName) {
        if (hashMap.size() == 0) {
            return columnName;
        }
        StringBuilder builder = new StringBuilder("CASE ");
        builder.append(columnName);
        builder.append(" ");
        Iterator<Integer> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            builder.append("WHEN ");
            builder.append(key);
            builder.append(" THEN '");
            builder.append(hashMap.get(key));
            builder.append("' ");
        }
        builder.append("END");
        return builder.toString();
    }

    /**
     * Stringをキー/Stringが値のHashMapに準じたSQLのCASE文を生成する。
     *
     * @param hashMap 対象連想配列
     * @param columnName 対象カラム
     * @return CASE文
     */
    public static String makeCaseSqlFromHashMapString(HashMap<String, String> hashMap, String columnName) {
        if (hashMap.size() == 0) {
            return columnName;
        }
        StringBuilder builder = new StringBuilder("CASE ");
        builder.append(columnName);
        builder.append(" ");
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.append("WHEN '");
            builder.append(key);
            builder.append("' THEN '");
            builder.append(hashMap.get(key));
            builder.append("' ");
        }
        builder.append("END");
        return builder.toString();
    }
}
