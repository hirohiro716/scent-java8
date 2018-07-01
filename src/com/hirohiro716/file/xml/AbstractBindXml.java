package com.hirohiro716.file.xml;

import java.io.IOException;

import com.hirohiro716.RudeArray;

/**
 * ORMのようなものを提供する抽象クラス.
 * @author hiro
 */
public abstract class AbstractBindXml {

    private PropertyXML xmlHelper;

    /**
     * コンストラクタ.
     */
    public AbstractBindXml() {
        this.xmlHelper = new PropertyXML();
    }

    /**
     * XMLファイルの場所を指定する. ファイルがない場合は作成される.
     * @param xmlFileLocation ファイルのフルパス
     * @throws IOException
     */
    public void entryXmlFile(String xmlFileLocation) throws IOException {
        this.xmlHelper.entryFile(xmlFileLocation);
    }

    private RudeArray properties;

    /**
     * 全てのプロパティをセットする.
     * @param properties
     */
    public void setProperties(RudeArray properties) {
        this.properties = properties;
    }
    
    /**
     * 全てのプロパティを取得する.
     * @return properties
     */
    public RudeArray getProperties() {
        return this.properties;
    }
    
    /**
     * 初期値が入力されたデータを取得する.
     * @return 初期データ
     */
    public abstract RudeArray createDefaultRow();
    
    /**
     * 初期値が入力されたデータを取得する.
     * @param properties プロパティ一覧
     * @return 初期データ
     */
    public static RudeArray createDefaultRow(InterfaceProperty[] properties) {
        RudeArray row = new RudeArray();
        for (InterfaceProperty property : properties) {
            row.put(property.getPhysicalName(), property.getDefaultValue());
        }
        return row;
    }
    
    /**
     * プロパティが正しいか検証する.
     * @throws ValidationException
     * @throws Exception
     */
    public abstract void validate() throws ValidationException, Exception;
    
    /**
     * プロパティを正しく変換する.
     * @throws Exception
     */
    public abstract void normalize() throws Exception;
    
    /**
     * すべてのプロパティをXMLファイルから取得する.
     */
    public abstract void fetchAllProperties();
    
    /**
     * すべてのプロパティをXMLに書き込む.
     * @throws IOException
     */
    public abstract void updateAllProperties() throws IOException;

    /**
     * プロパティが存在するか確認する.
     * @param name プロパティ名
     * @return 結果
     */
    public boolean isExistProperty(String name) {
        return this.xmlHelper.isExist(name);
    }

    /**
     * プロパティ値をXMLファイルから取得する.
     * @param name プロパティ名
     * @return プロパティ値
     */
    public String getValue(String name) {
        return this.xmlHelper.get(name);
    }

    /**
     * プロパティ値をXMLファイルに保存する.
     * @param name プロパティ名
     * @param value プロパティ値
     * @throws IOException
     */
    public void setValue(String name, String value) throws IOException {
        this.xmlHelper.put(name, value);
    }

}
