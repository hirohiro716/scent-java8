package com.hirohiro716.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.hirohiro716.file.xml.PropertyXML;

/**
 * XMLファイルにCookieを保持するクラス。
 *
 * @author hiro
 *
 */
public class XMLCookieStore extends AbstractCookieStore {

    /**
     * コンストラクタ。
     *
     * @param xmlFile Cookie情報を保存するXMLファイル
     * @throws IOException 
     */
    public XMLCookieStore(File xmlFile) throws IOException {
        if (xmlFile.getParentFile().exists() == false) {
            xmlFile.getParentFile().mkdir();
        }
        this.xml.entryFile(xmlFile);
    }
    
    private final PropertyXML xml = new PropertyXML();
    
    @Override
    protected void persistCookies(String domainAndPath, String serialized) throws Exception {
        this.xml.write(domainAndPath, serialized);
    }

    @Override
    protected String loadPersistedCookies(String domainAndPath) throws Exception {
        return this.xml.read(domainAndPath);
    }

    @Override
    protected void clearAllPersistedCookies() throws Exception {
        this.xml.clear();
    }

    @Override
    protected HashMap<String, String> loadAllPersistedCookies() throws Exception {
        return this.xml.createHashMap();
    }}
