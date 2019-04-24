package com.hirohiro716.file.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * プロパティXMLファイルを作成します.
 * @author hiro
 */
public class PropertyXML {
    // プロパティ保持用
    private Properties properties;
    // XMLファイルの場所
    private String fileLocation;

    /**
     * コメント用
     */
    private String comment = "This was made by com.hirohiro716.file.PropertyXML";

    /**
     * XMLファイルの場所を指定する. ファイルがない場合は作成される.
     * @param fileLocation ファイルのフルパス
     * @throws IOException
     */
    public void entryFile(String fileLocation) throws IOException {
        this.properties = new Properties();
        this.fileLocation = fileLocation;
        try (InputStream inputStream = new FileInputStream(fileLocation)) {
            this.properties.loadFromXML(inputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            saveFile();
        } catch (IOException ioException) {
            this.properties = null;
            throw ioException;
        }
    }

    /**
     * 設定値を取得する.
     * @param name プロパティ名
     * @return 値
     */
    public String get(String name) {
        if (this.properties == null) {
            return null;
        }
        if (this.properties.containsKey(name)) {
            return this.properties.getProperty(name);
        }
        return null;
    }

    /**
     * 値を保存する.
     * @param name プロパティ名
     * @param value 値
     * @throws IOException
     */
    public void put(String name, String value) throws IOException {
        if (this.properties == null) {
            return;
        }
        this.properties.setProperty(name, value);
        saveFile();
    }

    /**
     * プロパティが存在するか確認する.
     * @param name
     * @return 結果
     */
    public boolean isExist(String name) {
        if (this.properties == null) {
            return false;
        }
        return this.properties.containsKey(name);
    }

    /**
     * コメントをセットします. データ作成時のみ意味がある.
     * @param value 値
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * ファイルを保存する.
     * @throws IOException 
     */
    private void saveFile() throws IOException {
        try (OutputStream outputStream = new FileOutputStream(this.fileLocation)) {
            this.properties.storeToXML(outputStream, this.comment);
        }
    }

}