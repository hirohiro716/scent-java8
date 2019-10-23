package com.hirohiro716.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * ヘッダーを取得する.
     * @return ヘッダー
     */
    public String[] getHeaders() {
        return this.headers.toArray(new String[] {});
    }
    
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
     * 行数を取得する.
     * @return 行数
     */
    public int size() {
        return this.rows.size();
    }
    
    /**
     * 1行を取得する.
     * @param index 位置
     * @return 行情報(indexが範囲外ならnull)
     */
    public String[] getRow(int index) {
        if (this.rows.size() <= index) {
            return null;
        }
        return this.rows.get(index).toArray(new String[] {});
    }
    
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
    
    private String delimiter = ",";
    
    /**
     * 区切り文字をセットする.
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    /**
     * ファイルから読み込む.
     * @param file ファイル
     * @param charsetName 文字セット
     * @param firstRowIsHeader 最初の行をヘッダーにするかどうか
     * @throws IOException
     */
    public void importFile(File file, String charsetName, boolean firstRowIsHeader) throws IOException {
        // 文字エンコーディング
        Charset charset = Charset.defaultCharset();
        try {
            if (charsetName != null) {
                charset = Charset.forName(charsetName);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        // ファイルから読み込む
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            boolean isStringImporting = false;
            boolean isHeaderDone = false;
            ArrayList<String> values = new ArrayList<>();
            StringBuilder value = new StringBuilder();
            int firstChar = reader.read();
            int secondChar = reader.read();
            int thirdChar = reader.read();
            while (firstChar > -1) {
                String first = new String(new char[] {(char) firstChar});
                String second = new String(new char[] {(char) secondChar});
                String third = new String(new char[] {(char) thirdChar});
                if (first.equals(this.delimiter) && isStringImporting == false) {
                    // 次の値
                    values.add(value.toString());
                    value = new StringBuilder();
                } else if (first.equals("\n") && isStringImporting == false || first.equals("\r") && isStringImporting == false) {
                    // 行として取り込んで次の行へ
                    if (values.size() > 0 || value.length() > 0) {
                        values.add(value.toString());
                        if (firstRowIsHeader && isHeaderDone == false) {
                            this.setHeaders(values);
                            isHeaderDone = true;
                        } else {
                            this.addRow(values);
                        }
                        isStringImporting = false;
                        values = new ArrayList<>();
                        value = new StringBuilder();
                    }
                } else if (first.equals("\"")) {
                    // エスケープ
                    if (second.equals("\"") && third.equals(",") == false || second.equals("\"") && thirdChar == -1
                            || second.equals("\"") && third.equals("\n") || second.equals("\"") && third.equals("\r")) {
                        value.append(first);
                        reader.skip(1);
                    } else {
                        if (isStringImporting) {
                            isStringImporting = false;
                        } else {
                            isStringImporting = true;
                        }
                    }
                } else {
                    // その他
                    value.append(first);
                }
                firstChar = secondChar;
                secondChar = thirdChar;
                thirdChar = reader.read();
                first = new String(new char[] {(char) firstChar});
                second = new String(new char[] {(char) secondChar});
                third = new String(new char[] {(char) thirdChar});
            }
            // 最終行が未取り込みなら取り込む
            if (values.size() > 0 || value.length() > 0) {
                values.add(value.toString());
                if (firstRowIsHeader && isHeaderDone == false) {
                    this.setHeaders(values);
                } else {
                    this.addRow(values);
                }
            }
        }
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
            if (charsetName != null) {
                charset = Charset.forName(charsetName);
            }
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
                    header = StringConverter.nullReplace(this.headers.get(index), "").replaceAll("\"", "\"\"");
                }
                if (index > 0) {
                    csv.append(this.delimiter);
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
                    value = StringConverter.nullReplace(row.get(index), "").replaceAll("\"", "\"\"");
                }
                if (index > 0) {
                    csv.append(this.delimiter);
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
