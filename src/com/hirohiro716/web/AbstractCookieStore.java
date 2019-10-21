package com.hirohiro716.web;

import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hirohiro716.ByteConverter;
import com.hirohiro716.RudeArray;
import com.hirohiro716.datetime.Datetime;

/**
 * Cookieを保持する抽象クラス.
 * @author hiro
 */
public abstract class AbstractCookieStore implements CookieStore {
    
    private final HashMap<String, HashMap<String, HttpCookieWithDate>> domainAndPathWithCookies = new HashMap<>();
    
    private boolean isInitialized = false;
    
    /**
     * 初回の永続化済みCookieを読み込む処理.
     */
    private void initialize() {
        if (this.isInitialized) {
            return;
        }
        try {
            HashMap<String, String> domainAndPathWithSerializedCookies = this.loadAllPersistedCookies();
            for (String domainAndPath: domainAndPathWithSerializedCookies.keySet()) {
                String serializedString = domainAndPathWithSerializedCookies.get(domainAndPath);
                byte[] serialized = ByteConverter.stringToBytes(serializedString);
                HashMap<String, HttpCookieWithDate> cookies = new HashMap<>();
                RudeArray cookiesRudeArray = RudeArray.desirialize(serialized);
                for (String name: cookiesRudeArray.getKeysAtString()) {
                    HttpCookieWithDate httpCookieWithDate = HttpCookieWithDate.deserialize(cookiesRudeArray.getString(name));
                    cookies.put(name, httpCookieWithDate);
                }
                this.domainAndPathWithCookies.put(domainAndPath, cookies);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.isInitialized = true;
    }

    /**
     * URIからdomainとpathの条件に該当するcookieを検索する.
     * @param uri
     * @return ArrayList<HashMap<String, HttpCookieWithDate>>
     */
    private ArrayList<HashMap<String, HttpCookieWithDate>> findCookiesFromURI(URI uri) {
        String uriString = uri.getHost() + uri.getPath();
        ArrayList<HashMap<String, HttpCookieWithDate>> cookies = new ArrayList<>();
        for (String key: this.domainAndPathWithCookies.keySet()) {
            int indexOfResult = uriString.indexOf(key);
            if (indexOfResult >= 0 && indexOfResult < uri.getHost().length()) {
                cookies.add(this.domainAndPathWithCookies.get(key));
            }
        }
        return cookies;
    }
    
    @Override
    public synchronized void add(URI uri, HttpCookie httpCookie) {
        this.initialize();
        String key = httpCookie.getDomain() + httpCookie.getPath();
        HashMap<String, HttpCookieWithDate> cookies = this.domainAndPathWithCookies.get(key);
        if (cookies == null) {
            cookies = new HashMap<>();
            this.domainAndPathWithCookies.put(key, cookies);
        }
        HttpCookieWithDate httpCookieWithDate = new HttpCookieWithDate(httpCookie, new Date());
        cookies.put(httpCookie.getName(), httpCookieWithDate);
        try {
            this.serializeAndPersistCookies(httpCookieWithDate);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public synchronized List<HttpCookie> get(URI uri) {
        this.initialize();
        ArrayList<HttpCookie> listCookies = new ArrayList<>() ;
        for (HashMap<String, HttpCookieWithDate> cookies: this.findCookiesFromURI(uri)) {
            for (HttpCookieWithDate httpCookieWithDate: cookies.values()) {
                if (httpCookieWithDate.isInExpirationDate() == false) {
                    this.removePersistCookie(uri, httpCookieWithDate.getHttpCookie());
                    continue;
                }
                if (httpCookieWithDate.isVerifyURI(uri) == false) {
                    continue;
                }
                if (httpCookieWithDate.isVerifyPortNumber(uri) == false) {
                    continue;
                }
                if (httpCookieWithDate.getHttpCookie().getDiscard()) {
                    this.removePersistCookie(uri, httpCookieWithDate.getHttpCookie());
                }
                listCookies.add(httpCookieWithDate.getHttpCookie());
            }
        }
        return listCookies;
    }

    @Override
    public synchronized List<HttpCookie> getCookies() {
        this.initialize();
        ArrayList<HttpCookie> listCookies = new ArrayList<>() ;
        for (HashMap<String, HttpCookieWithDate> cookies: this.domainAndPathWithCookies.values()) {
            for (HttpCookieWithDate httpCookieWithDate: cookies.values()) {
                if (httpCookieWithDate.isInExpirationDate()) {
                    listCookies.add(httpCookieWithDate.getHttpCookie());
                }
            }
        }
        return listCookies;
    }

    @Override
    public synchronized List<URI> getURIs() {
        this.initialize();
        ArrayList<URI> listURIs = new ArrayList<>() ;
        for (String key: this.domainAndPathWithCookies.keySet()) {
            try {
                listURIs.add(new URI(key));
            } catch (URISyntaxException exception) {
                exception.printStackTrace();
            }
        }
        return listURIs;
    }

    @Override
    public synchronized boolean remove(URI uri, HttpCookie httpCookie) {
        this.initialize();
        try {
            for (HashMap<String, HttpCookieWithDate> cookies: this.findCookiesFromURI(uri)) {
                for (HttpCookieWithDate httpCookieWithDate: cookies.values()) {
                    HttpCookie compareCookie = httpCookieWithDate.getHttpCookie();
                    if (compareCookie.equals(httpCookie)) {
                        cookies.remove(httpCookie.getName());
                        this.serializeAndPersistCookies(httpCookieWithDate);
                        return true;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
    
    /**
     * 永続化したCookieの情報だけを削除する.
     * @param uri URI
     * @param httpCookie 対象のCookie
     */
    protected void removePersistCookie(URI uri, HttpCookie httpCookie) {
        try {
            for (HashMap<String, HttpCookieWithDate> cookies: this.findCookiesFromURI(uri)) {
                for (HttpCookieWithDate httpCookieWithDate: cookies.values()) {
                    HttpCookie compareCookie = httpCookieWithDate.getHttpCookie();
                    if (compareCookie.equals(httpCookie)) {
                        this.serializeAndPersistCookies(httpCookieWithDate);
                        return;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public synchronized boolean removeAll() {
        this.initialize();
        try {
            this.domainAndPathWithCookies.clear();
            this.clearAllPersistedCookies();
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Cookieをシリアライズして永続化する.
     * @param httpCookieWithDate
     * @throws Exception 
     */
    private void serializeAndPersistCookies(HttpCookieWithDate httpCookieWithDate) throws Exception {
        HttpCookie httpCookie = httpCookieWithDate.getHttpCookie();
        if (httpCookie.getMaxAge() == -1) {
            return;
        }
        String domainAndPath = httpCookie.getDomain() + httpCookie.getPath();
        HashMap<String, HttpCookieWithDate> cookies = this.domainAndPathWithCookies.get(domainAndPath);
        RudeArray rudeArray = new RudeArray();
        for (String name: cookies.keySet()) {
            rudeArray.put(name, cookies.get(name).serialize());
        }
        byte[] serialized = rudeArray.toSerialize();
        String serializedString = ByteConverter.bytesToString(serialized);
        this.persistCookies(domainAndPath, serializedString);
    }
    
    /**
     * Cookieを永続化する.
     * @param domainAndPath ドメインとパス
     * @param serialized シリアライズしたcookieの情報
     * @throws Exception 
     */
    protected abstract void persistCookies(String domainAndPath, String serialized) throws Exception;

    /**
     * 永続化したCookieの情報をすべて削除する.
     * @throws Exception 
     */
    protected abstract void clearAllPersistedCookies() throws Exception;

    /**
     * 永続化したCookieを読み込む.
     * @param domainAndPath ドメインとパス
     * @return シリアライズされたcookieの情報
     * @throws Exception 
     */
    protected abstract String loadPersistedCookies(String domainAndPath) throws Exception;
    
    /**
     * 永続化したすべてのCookie情報を読み込む.
     * @return HashMap<String, String> 「ドメインとパス」がkeyで「シリアライズされたCookie情報」が値の連想配列
     * @throws Exception
     */
    protected abstract HashMap<String, String> loadAllPersistedCookies() throws Exception;
    
    /**
     * HttpCookieと作成日を一緒に保持するクラス.
     * @author hiro
     */
    protected static class HttpCookieWithDate {
        
        /**
         * コンストラクタ.
         * @param httpCookie Cookie
         * @param date 作成日
         */
        public HttpCookieWithDate(HttpCookie httpCookie, Date date) {
           this.httpCookie = httpCookie;
           this.date = date;
        }
        
        private HttpCookie httpCookie;
        
        /**
         * Cookieを取得する.
         * @return HttpCookie
         */
        public HttpCookie getHttpCookie() {
            return this.httpCookie;
        }
        
        private Date date;
        
        /**
         * 作成日を取得する.
         * @return Date
         */
        public Date getDate() {
            return this.date;
        }
        
        /**
         * URIがCookieの条件を満たしているか確認する.
         * @param uri
         * @return 結果
         */
        public boolean isVerifyURI(URI uri) {
            if (this.httpCookie.getSecure()) {
                switch (uri.getScheme()) {
                case "https":
                case "HTTPS":
                    break;
                default:
                    return false;
                }
            }
            if (this.httpCookie.isHttpOnly()) {
                switch (uri.getScheme()) {
                case "http":
                case "https":
                case "HTTP":
                case "HTTPS":
                    break;
                default:
                    return false;
                }
            }
            return true;
        }
        
        /**
         * URIがCookieのポート番号条件を満たしているか確認する.
         * @param uri
         * @return 結果
         */
        public boolean isVerifyPortNumber(URI uri) {
            if (this.httpCookie.getPortlist() == null) {
                return true;
            }
            int uriPort = uri.getPort();
            if (uriPort == -1) {
                switch (uri.getScheme()) {
                case "http":
                case "HTTP":
                    uriPort = 80;
                    break;
                case "https":
                case "HTTPS":
                    uriPort = 443;
                    break;
                default:
                    return false;
                }
            }
            String[] portlists = this.httpCookie.getPortlist().split(",");
            for (String portString: portlists) {
                int port = Integer.parseInt(portString);
                if (port == uriPort) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * Cookieが有効期限内かを確認する.
         * @return 結果
         */
        public boolean isInExpirationDate() {
            if (this.httpCookie.getMaxAge() == -1) {
                return true;
            }
            Datetime maxAgeDatetime = new Datetime(this.date);
            int maxAgeDivision = (int) Math.floor(this.httpCookie.getMaxAge() / Integer.MAX_VALUE);
            int maxAgeSurplus = (int) this.httpCookie.getMaxAge() % Integer.MAX_VALUE;
            for (int index = 1; index <= maxAgeDivision; index++) {
                maxAgeDatetime.addSecond(Integer.MAX_VALUE);
            }
            maxAgeDatetime.addSecond(maxAgeSurplus);
            Date now = new Date();
            if (now.getTime() > maxAgeDatetime.getDate().getTime()) {
                return false;
            }
            return true;
        }
        
        /**
         * シリアライズする.
         * @return シリアライズしたbyte配列の文字列
         */
        public String serialize() {
            try {
                RudeArray array = new RudeArray();
                array.put("name", this.httpCookie.getName());
                array.put("value", this.httpCookie.getValue());
                array.put("comment", this.httpCookie.getComment());
                array.put("comment_url", this.httpCookie.getCommentURL());
                array.put("discard", this.httpCookie.getDiscard());
                array.put("domain", this.httpCookie.getDomain());
                array.put("max_age", this.httpCookie.getMaxAge());
                array.put("path", this.httpCookie.getPath());
                array.put("portlist", this.httpCookie.getPortlist());
                array.put("secure", this.httpCookie.getSecure());
                array.put("is_http_only", this.httpCookie.isHttpOnly());
                array.put("version", this.httpCookie.getVersion());
                array.put("date", Datetime.dateToString(this.date));
                byte[] serialized = array.toSerialize();
                return ByteConverter.bytesToString(serialized);
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        
        /**
         * デシリアライズする.
         * @param serializedString シリアライズされているbyte配列の文字列
         * @return HttpCookieWithDate
         */
        public static HttpCookieWithDate deserialize(String serializedString) {
            try {
                byte[] serialized = ByteConverter.stringToBytes(serializedString);
                RudeArray array = RudeArray.desirialize(serialized);
                HttpCookie httpCookie = new HttpCookie(array.getString("name"), array.getString("value"));
                httpCookie.setComment(array.getString("comment"));
                httpCookie.setCommentURL(array.getString("comment_url"));
                httpCookie.setDiscard(array.getBoolean("discard"));
                httpCookie.setDomain(array.getString("domain"));
                httpCookie.setMaxAge(array.getLong("max_age"));
                httpCookie.setPath(array.getString("path"));
                httpCookie.setPortlist(array.getString("portlist"));
                httpCookie.setSecure(array.getBoolean("secure"));
                httpCookie.setHttpOnly(array.getBoolean("is_http_only"));
                httpCookie.setVersion(array.getInteger("version"));
                Date date = array.getDate("date");
                return new HttpCookieWithDate(httpCookie, date);
            } catch (IOException|ClassNotFoundException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        
    }
    
}
