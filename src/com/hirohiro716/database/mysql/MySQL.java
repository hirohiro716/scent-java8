package com.hirohiro716.database.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.hirohiro716.StringConverter;
import com.hirohiro716.database.AbstractDatabase;

/**
 * MySQLへのJDBC接続を補助するクラス。
 *
 * @author hiro
 *
 */
public class MySQL extends AbstractDatabase {

    @Override
    public void connect(String connectionString) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        super.setConnection(DriverManager.getConnection(connectionString));
    }

    /**
     * MySQLデータベースに接続する。
     *
     * @param server サーバー名(IP)
     * @param dbName データベース名
     * @param user ユーザー名
     * @param pass パスワード
     * @param characterEncoding 文字セット(utf8など)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void connect(String server, String dbName, String user, String pass, String characterEncoding) throws ClassNotFoundException, SQLException {
        String connectionString = StringConverter.join("jdbc:mysql://", server, "/", dbName, "?user=", user, "&password=", pass, "&characterEncoding=", characterEncoding);
        this.connect(connectionString);
    }

    /**
     * MySQLデータベースに接続する。
     *
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
        String connectionString = StringConverter.join("jdbc:mysql://", server, ":", port, "/", dbName, "?user=", user, "&password=", pass, "&characterEncoding=", characterEncoding);
        this.connect(connectionString);
    }
}
