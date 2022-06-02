package com.hirohiro716.file.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

import com.hirohiro716.file.FileHelper;

/**
 * プロパティXMLファイルを作成します。
 *
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
     * XMLファイルの場所を指定する. ファイルがない場合は作成される。
     *
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
     * XMLファイルの場所を指定する. ファイルがない場合は作成される。
     *
     * @param file ファイル
     * @throws IOException
     */
    public void entryFile(File file) throws IOException {
        this.properties = new Properties();
        this.fileLocation = FileHelper.generateOptimizedPathFromURI(file.toURI());
        try (InputStream inputStream = new FileInputStream(file)) {
            this.properties.loadFromXML(inputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            saveFile();
        } catch (IOException ioException) {
            this.properties = null;
            throw ioException;
        }
    }
    
    /**
     * 設定値を取得する。
     *
     * @param name プロパティ名
     * @return 値
     */
    public String read(String name) {
        if (this.properties == null) {
            return null;
        }
        if (this.properties.containsKey(name)) {
            return this.properties.getProperty(name);
        }
        return null;
    }

    /**
     * 値を保存する。
     *
     * @param name プロパティ名
     * @param value 値
     * @throws IOException
     */
    public void write(String name, String value) throws IOException {
        if (this.properties == null) {
            return;
        }
        this.properties.setProperty(name, value);
        saveFile();
    }
    
    /**
     * 値を削除する。
     *
     * @param name プロパティ名
     * @throws IOException
     */
    public void remove(String name) throws IOException {
        if (this.properties == null) {
            return;
        }
        this.properties.remove(name);
        saveFile();
    }

    /**
     * 値をすべて削除する。
     *
     * @throws IOException
     */
    public void clear() throws IOException {
        if (this.properties == null) {
            return;
        }
        this.properties.clear();
        saveFile();
    }

    /**
     * プロパティが存在するか確認する。
     *
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
     * 保存済みのすべてのプロパティを取得する。
     *
     * @return HashMap<String, String>
     */
    public HashMap<String, String> createHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        for (Object keyObject: this.properties.keySet()) {
            String key = (String) keyObject;
            hashMap.put(key, this.properties.getProperty(key)); 
        }
        return hashMap;
    }

    /**
     * コメントをセットします. データ作成時のみ意味がある。
     *
     * @param value 値
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * ファイルを保存する。
     *
     * @throws IOException 
     */
    private void saveFile() throws IOException {
        try (OutputStream outputStream = new FileOutputStream(this.fileLocation)) {
            this.properties.storeToXML(outputStream, this.comment);
        }
    }
}