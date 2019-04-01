package com.hirohiro716.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import com.hirohiro716.StringConverter;

/**
 * CSVファイルの作成をサポートする.
 * @author hiro
 */
public class CSV {
    
    private ArrayList<String> headers = null;
    
    /**
     * ヘッダーをセットする.
     * @param headers
     */
    public void setHeaders(String[] headers) {
        this.headers = new ArrayList<>();
        for (String header: headers) {
            this.headers.add(header);
        }
    }
    
    /**
     * ヘッダーをセットする.
     * @param headers
     */
    public void setHeaders(Collection<String> headers) {
        this.headers = new ArrayList<>();
        this.headers.addAll(headers);
    }
    
    private ArrayList<ArrayList<String>> rows = new ArrayList<>();
    
    /**
     * 新規行を追加する.
     * @param row
     */
    public void addRow(String[] row) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String value: row) {
            arrayList.add(value);
        }
        this.rows.add(arrayList);
    }
    
    /**
     * 新規行を追加する.
     * @param row
     */
    public void addRow(Collection<String> row) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(row);
        this.rows.add(arrayList);
    }
    
    /**
     * ファイルに書き込む.
     * @param file ファイル
     * @param charsetName 文字セット
     * @throws IOException 
     */
    public void exportFile(File file, String charsetName) throws IOException {
        // 文字エンコーディング
        Charset charset = Charset.defaultCharset();
        try {
            charset = Charset.forName(charsetName);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        // カラム数
        int numberOfColumns = this.headers.size();
        for (ArrayList<String> row: this.rows) {
            if (numberOfColumns < row.size()) {
                numberOfColumns = row.size();
            }
        }
        // CSV作成
        StringBuilder csv = new StringBuilder();
        if (this.headers != null) {
            for (int index = 0; index < numberOfColumns; index++) {
                String header = "";
                if (this.headers.size() > index) {
                    header = StringConverter.nullReplace(this.headers.get(index), "");
                }
                if (index > 0) {
                    csv.append(",");
                }
                csv.append("\"");
                csv.append(header);
                csv.append("\"");
            }
            csv.append(StringConverter.LINE_SEPARATOR);
        }
        for (ArrayList<String> row: this.rows) {
            for (int index = 0; index < numberOfColumns; index++) {
                String value = "";
                if (row.size() > index) {
                    value = StringConverter.nullReplace(row.get(index), "");
                }
                if (index > 0) {
                    csv.append(",");
                }
                csv.append("\"");
                csv.append(value);
                csv.append("\"");
            }
            csv.append(StringConverter.LINE_SEPARATOR);
        }
        // ファイルに保存
        FileHelper.createTextFile(csv.toString(), file, charset);
    }
    
}
