package com.hirohiro716.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.hirohiro716.StringConverter;
import com.hirohiro716.RudeArray;

/**
 * WebRequestを処理するクラス.
 * @author hiro
 */
public class WEBRequest {

    // 処理するURL
    private String urlString = null;

    /**
     * URLを指定してインスタンス生成
     * @param url
     */
    public WEBRequest(String url) {
        this.urlString = url;
    }

    // 文字コード
    private String charsetName = "UTF-8";

    /**
     * 使用する文字コードを指定する.
     * @param charsetName 初期値は "UTF-8"
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * 使用する文字コードを取得する.
     * @return charsetName
     */
    public String getCharsetName() {
        return this.charsetName;
    }

    // 送信するパラメータ
    private RudeArray params = new RudeArray();

    /**
     * パラメータをセットする.
     * @param key キー
     * @param value 値
     */
    public void putParam(String key, String value) {
        this.params.put(key, value);
    }

    /**
     * パラメータを取得する.
     * @param key キー
     * @return 値
     */
    public String getParam(String key) {
        return this.params.getString(key);
    }

    /**
     * セットしてあるパラメータをURLに送信できる形にして取得する.
     * @return URLパラメータ文字列
     */
    private String makeUrlParamsString() {
        String paramsString = "";
        for (String key : this.params.getKeysAtString()) {
            String value;
            try {
                value = URLEncoder.encode(this.params.getString(key), this.charsetName);
            } catch (UnsupportedEncodingException exception) {
                return null;
            }
            if (paramsString.length() > 0) {
                paramsString += "&";
            }
            paramsString = StringConverter.join(paramsString, key, "=", value);
        }
        return paramsString;
    }

    /**
     * GET送信して結果を取得する.
     * @return body文字列
     * @throws IOException
     */
    public String requestGetResult() throws IOException {
        String params = makeUrlParamsString();
        URL url;
        if (params.length() > 0) {
            url = new URL(StringConverter.join(this.urlString, "?", params));
        } else {
            url = new URL(this.urlString);
        }
        URLConnection connection = url.openConnection();
        connection.setReadTimeout(0);
        connection.setConnectTimeout(0);
        String body = "";
        // StreamReaderを使ってStreamをbyteで読む
        try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), this.charsetName)) {
            // BufferedReaderを使ってbyteを文字列に変換しながら読む
            try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    body = StringConverter.join(body, line, "\n");
                }
            }
        }
        return body;
    }

    /**
     * POST送信して結果を取得する.
     * @return body文字列
     * @throws IOException
     */
    public String requestPostResult() throws IOException {
        URL url = new URL(this.urlString);
        URLConnection connection = url.openConnection();
        String body = "";
//        connection.setReadTimeout(0);
//        connection.setConnectTimeout(0);
//        // StreamReaderを使ってStreamをbyteで読む
//        try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), this.charsetName)) {
//            // BufferedReaderを使ってbyteを文字列として読む
//            try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    body = ConvertValue.stringJoin(body, line, "\n");
//                }
//            }
//        }
        // HTML上のパラメータを取得する
        this.applyFormParams(body);
        // パラメータをPOSTする
//        connection = url.openConnection();
        connection.setReadTimeout(0);
        connection.setConnectTimeout(0);
        connection.setDoOutput(true);
        String params = makeUrlParamsString();
        if (params.length() > 0) {
            // OutputStreamWriterを使ってパラメータを書き込む
            try (OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream(), this.charsetName)) {
                streamWriter.write(params);
                streamWriter.flush();
            }
        }
        // 結果を取得
        body = "";
        // StreamReaderを使ってStreamをbyteで読む
        try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), this.charsetName)) {
            // BufferedReaderを使ってbyteを文字列として読む
            try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    body = StringConverter.join(body, line, "\n");
                }
            }
        }
        return body;
    }

    private void applyFormParams(String body) {
        String[] inputTags = body.split("<input ");
        for (String inputTag: inputTags) {
            // valueを取得する
            int valueStartIndex = inputTag.indexOf(" value=");
            String valueTag = null;
            if (valueStartIndex > -1) {
                valueTag = inputTag.substring(valueStartIndex + 8);
                int valueEndIndex = valueTag.indexOf('"');
                valueTag = valueTag.substring(0, valueEndIndex);
            }
            // nameを取得する
            int nameStartIndex = inputTag.indexOf(" name=");
            String nameTag = null;
            if (nameStartIndex > -1) {
                nameTag = inputTag.substring(nameStartIndex + 7);
                int nameEndIndex = nameTag.indexOf('"');
                nameTag = nameTag.substring(0, nameEndIndex);
            }
            if (valueTag != null && nameTag != null && this.params.containsKey(nameTag) == false) {
                this.params.put(nameTag, valueTag);
            }
        }
    }

}
