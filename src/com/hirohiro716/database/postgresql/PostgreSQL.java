package com.hirohiro716.database.postgresql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import com.hirohiro716.StringConverter;
import com.hirohiro716.database.AbstractDatabase;
import com.hirohiro716.database.DataNotFoundException;
import com.hirohiro716.RudeArray;

/**
 * PostgreSQLへのJDBC接続を補助するクラス.
 * @author hiro
 */
public class PostgreSQL extends AbstractDatabase {

    @Override
    public void connect(String connectionString) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        this.setConnection(DriverManager.getConnection(connectionString));
    }

    /**
     * PostgreSQLデータベースに接続する.
     * @param server サーバー名(IP)
     * @param dbName データベース名
     * @param user ユーザー名
     * @param pass パスワード
     * @param characterEncoding 文字セット(utf8など)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void connect(String server, String dbName, String user, String pass, String characterEncoding) throws ClassNotFoundException, SQLException {
        String connectionString = StringConverter.join("jdbc:postgresql://", server, "/", dbName, "?user=", user, "&password=", pass, "&characterEncoding=", characterEncoding);
        this.connect(connectionString);
    }

    /**
     * PostgreSQLデータベースに接続する.
     * @param server サーバー名(IP)
     * @param dbName データベース名
     * @param user ユーザー名
     * @param pass パスワード
     * @param characterEncoding 文字セット(utf8など)
     * @param port ポート番号
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void connect(String server, String dbName, String user, String pass, String characterEncoding, int port) throws ClassNotFoundException, SQLException {
        String connectionString = StringConverter.join("jdbc:postgresql://", server, ":", port, "/", dbName, "?user=", user, "&password=", pass, "&characterEncoding=", characterEncoding);
        this.connect(connectionString);
    }

    /**
     * テーブルの読み書きをロックする.
     * @param tableName テーブル名
     * @throws SQLException
     */
    public void lockTable(String tableName) throws SQLException {
        String sql = StringConverter.join("LOCK TABLE ", tableName, " IN ACCESS EXCLUSIVE MODE;");
        this.execute(sql);
    }

    /**
     * テーブルの書き込みをロックする.
     * @param tableName テーブル名
     * @throws SQLException
     */
    public void lockTableReadonly(String tableName) throws SQLException {
        String sql = StringConverter.join("LOCK TABLE ", tableName, " IN EXCLUSIVE MODE;");
        this.execute(sql);
    }

    /**
     * データベースサーバーの現在の時刻を取得する.
     * @return 現在の時刻
     * @throws SQLException
     */
    public Date fetchNow() throws SQLException {
        try {
            String sql = "SELECT CLOCK_TIMESTAMP() AS nowtime;";
            RudeArray row = this.fetchRow(sql);
            return row.getDate("nowtime");
        } catch (DataNotFoundException exception) {
            return null;
        }
    }

}
