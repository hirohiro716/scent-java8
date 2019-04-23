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
     * 全行を取得する.
     * @return 全行
     */
    public String[][] getRows() {
        ArrayList<String[]> rows = new ArrayList<>();
        for (ArrayList<String> sourceRow: this.rows) {
            rows.add(sourceRow.toArray(new String[] {}));
        }
        return rows.toArray(new String[][] {});
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
        boolean isHeaderDone = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> values = new ArrayList<>();
                StringBuilder value = new StringBuilder();
                boolean isStringImporting = false;
                for (int index = 0; index < line.length(); index++) {
                    String one = line.substring(index, index + 1);
                    String two = one;
                    try {
                        two = line.substring(index, index + 2);
                    } catch (IndexOutOfBoundsException exception) {
                    }
                    if (one.equals(this.delimiter) && isStringImporting == false) {
                        values.add(value.toString());
                        value = new StringBuilder();
                    } else if (one.equals("\"")) {
                        if (two.equals("\"\"")) {
                            value.append(one);
                            index++;
                        } else {
                            if (isStringImporting) {
                                isStringImporting = false;
                            } else {
                                isStringImporting = true;
                            }
                        }
                    } else {
                        value.append(one);
                    }
                }
                values.add(value.toString());
                if (firstRowIsHeader && isHeaderDone == false) {
                    this.setHeaders(values);
                    isHeaderDone = true;
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
