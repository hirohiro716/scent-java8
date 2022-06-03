package com.hirohiro716.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.hirohiro716.RudeArray;
import com.hirohiro716.StringConverter;

/**
 * JSON(RFC8259)オブジェクトクラス。
 *
 * 
 * @author hiro
 */
public class JSONObject {
    
    /**
     * コンストラクタ。
     */
    public JSONObject() {
        super();
    }
    
    /**
     * コンストラクタ。
     *
     * @param json
     */
    public JSONObject(String json) {
        String temporary = json.trim();
        if (temporary.length() == 0) {
            return;
        }
        String prefix = temporary.substring(0, 1);
        switch (prefix) {
        case "{": // JSONオブジェクト
            this.parseJSONObject(temporary);
            break;
        default: // 配列|真偽値|数値|null
            Object value = this.parseValue(json);
            if (value != null) {
                this.jsonObject.add(value);
            }
            break;
        }
    }
    
    private RudeArray jsonObject = new RudeArray();

    /**
     * JSONの文字列を整形する。
     *
     * @param jsonStringValue
     * @return 整形後文字列
     */
    private String shapeStringValue(String jsonStringValue) {
        String temporary = jsonStringValue.trim();
        if (temporary.indexOf("\"") == -1) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean isStartExtract = false;
        boolean isEscape = false;
        for (int i = 0; i < temporary.length(); i++) {
            String one = temporary.substring(i, i + 1);
            // 終了
            if (isStartExtract && isEscape == false && one.equals("\"")) {
                break;
            }
            // 取り込み
            if (isStartExtract) {
                if (isEscape) {
                    switch (one) {
                    case "b":
                        result.append("\b");
                        break;
                    case "f":
                        result.append("\f");
                        break;
                    case "n":
                        result.append("\n");
                        break;
                    case "r":
                        result.append("\r");
                        break;
                    case "t":
                        result.append("\t");
                        break;
                    case "u":
                        String code = temporary.substring(i + 1, i + 5);
                        result.append((char) Integer.parseInt(code, 16));
                        i += 4;
                        break;
                    default:
                        result.append(one);
                    }
                    isEscape = false;
                } else {
                    switch (one) {
                    case "\\":
                        isEscape = true;
                        break;
                    default:
                        result.append(one);
                        break;
                    }
                }
            }
            // 開始
            if (isStartExtract == false && isEscape == false) {
                isStartExtract = true;
            }
        }
        return result.toString();
    }

    /**
     * 特定の文字列{}[]に囲まれた内部の値をエスケープやネストを考慮して文字列として取得する。
     *
     * @param wrapJsonString 抽出対象の文字列
     * @param openingBracket 開始記号
     * @param endingBracket 終了記号
     * @return 抽出した文字列
     */
    private String extractWrapContent(String wrapJsonString, String openingBracket, String endingBracket) {
        String temporary = wrapJsonString.trim();
        if (temporary.indexOf(openingBracket) == -1 || temporary.indexOf(endingBracket) == -1) {
            return "";
        }
        LinkedHashMap<String, Integer> onInnerBracketDepth = new LinkedHashMap<>();
        onInnerBracketDepth.put("{", 0);
        onInnerBracketDepth.put("[", 0);
        onInnerBracketDepth.put("\"", 0);
        StringBuilder result = new StringBuilder();
        boolean isStartExtract = false;
        boolean isEscape = false;
        for (int i = 0; i < temporary.length(); i++) {
            String one = temporary.substring(i, i + 1);
            // 終了カッコ
            if (isStartExtract && isEscape == false && one.equals(endingBracket)) {
                if (onInnerBracketDepth.get("{") < 1 && onInnerBracketDepth.get("[") < 1 && onInnerBracketDepth.get("\"") < 1) {
                    break;
                }
            }
            // 取り込み
            if (isStartExtract) {
                if (isEscape) {
                    result.append(one);
                    isEscape = false;
                } else {
                    switch (one) {
                    case "\\":
                        result.append(one);
                        isEscape = true;
                        break;
                    case "{":
                    case "[":
                        if (onInnerBracketDepth.get("\"") < 1) {
                            onInnerBracketDepth.put(one, onInnerBracketDepth.get(one) + 1);
                        }
                        result.append(one);
                        break;
                    case "}":
                        if (onInnerBracketDepth.get("\"") < 1) {
                            onInnerBracketDepth.put("{", onInnerBracketDepth.get("{") - 1);
                        }
                        result.append(one);
                        break;
                    case "]":
                        if (onInnerBracketDepth.get("\"") < 1) {
                            onInnerBracketDepth.put("[", onInnerBracketDepth.get("[") - 1);
                        }
                        result.append(one);
                        break;
                    case "\"":
                        if (onInnerBracketDepth.get(one) == 0) {
                            onInnerBracketDepth.put(one, 1);
                        } else {
                            onInnerBracketDepth.put(one, 0);
                        }
                        result.append(one);
                        break;
                    default:
                        result.append(one);
                        break;
                    }
                }
            }
            // 開始カッコ
            if (isStartExtract == false && isEscape == false && one.equals(openingBracket)) {
                isStartExtract = true;
            }
        }
        return result.toString();
    }

    /**
     * JSONオブジェクトや配列などの文字列を分割する。
     *
     * @param jsonArrayString
     * @param delimiter
     * @return 配列
     */
    private String[] split(String jsonArrayString, String delimiter) {
        String temporary = jsonArrayString;
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean isEscape = false;
        LinkedHashMap<String, Integer> onInnerBracketDepth = new LinkedHashMap<>();
        onInnerBracketDepth.put("{", 0);
        onInnerBracketDepth.put("[", 0);
        onInnerBracketDepth.put("\"", 0);
        for (int i = 0; i < temporary.length(); i++) {
            String one = temporary.substring(i, i + 1);
            if (isEscape) {
                value.append(one);
                isEscape = false;
            } else {
                if (one.equals(delimiter)) {
                    if (onInnerBracketDepth.get("{") < 1 && onInnerBracketDepth.get("[") < 1 && onInnerBracketDepth.get("\"") < 1) {
                        strings.add(value.toString().trim());
                        value = new StringBuilder();
                    } else {
                        value.append(one);
                    }
                } else {
                    switch (one) {
                    case "\\":
                        value.append(one);
                        isEscape = true;
                        break;
                    case "{":
                    case "[":
                        if (onInnerBracketDepth.get("\"") < 1) {
                            onInnerBracketDepth.put(one, onInnerBracketDepth.get(one) + 1);
                        }
                        value.append(one);
                        break;
                    case "}":
                        if (onInnerBracketDepth.get("\"") < 1) {
                            onInnerBracketDepth.put("{", onInnerBracketDepth.get("{") - 1);
                        }
                        value.append(one);
                        break;
                    case "]":
                        if (onInnerBracketDepth.get("\"") < 1) {
                            onInnerBracketDepth.put("[", onInnerBracketDepth.get("[") - 1);
                        }
                        value.append(one);
                        break;
                    case "\"":
                        if (onInnerBracketDepth.get(one) == 0) {
                            onInnerBracketDepth.put(one, 1);
                        } else {
                            onInnerBracketDepth.put(one, 0);
                        }
                        value.append(one);
                        break;
                    default:
                        value.append(one);
                        break;
                    }
                }
            }
        }
        if (value.length() > 0) {
            strings.add(value.toString());
        }
        return strings.toArray(new String[] {});
    }

    /**
     * JSONの値部分をオブジェクトに変換する。
     *
     * @param jsonValuePart
     * @return 値オブジェクト
     */
    private Object parseValue(String jsonValuePart) {
        String temporary = StringConverter.nullReplace(jsonValuePart, "").trim();
        if (temporary.length() == 0) {
            return null;
        }
        String prefix = temporary.substring(0, 1);
        switch (prefix) {
        case "{": // JSONオブジェクト
            return new JSONObject(temporary);
        case "\"": // 文字列
            return this.shapeStringValue(temporary);
        case "[": // 配列
            String arrayString = this.extractWrapContent(temporary, "[", "]");
            String[] values = this.split(arrayString, ",");
            RudeArray rudeArray = new RudeArray();
            for (String value: values) {
                rudeArray.add(this.parseValue(value));
            }
            return rudeArray.getValues();
        case "t": // Boolean
            return true;
        case "f":
            return false;
        case "n": // null
            return null;
        default: // 数値
            return StringConverter.stringToDouble(jsonValuePart);
        }
    }

    /**
     * JSONを解析して連想配列で保持する。
     *
     * @param jsonObjectString
     */
    private void parseJSONObject(String jsonObjectString) {
        String temporary = this.extractWrapContent(jsonObjectString, "{", "}");
        String[] values = this.split(temporary, ",");
        RudeArray rudeArray = new RudeArray();
        for (String keyAndValueString: values) {
            String[] keyAndValue = this.split(keyAndValueString, ":");
            if (keyAndValue.length == 2) {
                String key = this.parseValue(keyAndValue[0].trim()).toString();
                Object value = this.parseValue(keyAndValue[1].trim());
                rudeArray.put(key, value);
            }
        }
        this.jsonObject = rudeArray;
    }

    /**
     * JSONオブジェクト内のアイテム数を取得する。
     * 
     * @return 結果
     */
    public int size() {
        return this.jsonObject.size();
    }
    
    /**
     * 文字列として取得する。
     *
     * @return 文字列 キャストに失敗した場合はnull
     */
    public String getString() {
        return this.jsonObject.getString(0);
    }
    
    /**
     * 文字列をセットする。
     *
     * @param value
     */
    public void setString(String value) {
        this.jsonObject.clear();
        this.jsonObject.add(value);
    }
    
    /**
     * 文字列を取得する。
     *
     * @param name 
     * @return 文字列 キャストに失敗した場合はnull
     */
    public String getString(String name) {
        return this.jsonObject.getString(name);
    }
    
    /**
     * JSONオブジェクト内に名前を指定して文字列をセットする。
     *
     * @param name
     * @param value
     */
    public void putString(String name, String value) {
        this.jsonObject.put(name, value);
    }

    /**
     * 数値を取得する。
     *
     * @return Number キャストに失敗した場合はnull
     */
    public Number getNumber() {
        return this.jsonObject.getNumber(0);
    }
    
    /**
     * 数値をセットする。
     *
     * @param value
     */
    public void setNumber(Number value) {
        this.jsonObject.clear();
        this.jsonObject.add(value);
    }
    
    /**
     * 数値を取得する。
     *
     * @param name
     * @return Number キャストに失敗した場合はnull
     */
    public Number getNumber(String name) {
        return this.jsonObject.getNumber(name);
    }
    
    /**
     * JSONオブジェクト内に名前を指定して数値をセットする。
     *
     * @param name
     * @param value
     */
    public void putNumber(String name, Number value) {
        this.jsonObject.put(name, value);
    }
    
    /**
     * 真偽値を取得する。
     *
     * @return Boolean キャストに失敗した場合はnull
     */
    public Boolean getBoolean() {
        return this.jsonObject.getBoolean(0);
    }
    
    /**
     * 真偽値をセットする。
     *
     * @param value
     */
    public void setBoolean(boolean value) {
        this.jsonObject.clear();
        this.jsonObject.add(value);
    }
    
    /**
     * 真偽値を取得する。
     *
     * @param name
     * @return Boolean キャストに失敗した場合はnull
     */
    public Boolean getBoolean(String name) {
        return this.jsonObject.getBoolean(name);
    }
    
    /**
     * JSONオブジェクト内に名前を指定して真偽値をセットする。
     *
     * @param name
     * @param value
     */
    public void putBoolean(String name, boolean value) {
        this.jsonObject.put(name, value);
    }
    
    /**
     * 配列を取得する。
     *
     * @return Object[] キャストに失敗した場合はnull
     */
    public Object[] getArray() {
        try {
            return (Object[]) this.jsonObject.get(0);
        } catch (Exception exception) {
            return null;
        }
    }
    
    /**
     * 配列をセットする。
     *
     * @param values
     */
    public void setArray(Object[] values) {
        this.jsonObject.clear();
        this.jsonObject.add(values);
    }

    /**
     * 配列を取得する。
     *
     * @param name
     * @return 結果
     */
    public Object[] getArray(String name) {
        try {
            return (Object[]) this.jsonObject.get(name);
        } catch (Exception exception) {
            return null;
        }
    }
    
    /**
     * JSONオブジェクト内に名前を指定して配列をセットする。
     *
     * @param name
     * @param values
     */
    public void putArray(String name, Object[] values) {
        this.jsonObject.put(name, values);
    }
    
    /**
     * JSONオブジェクトを取得する。
     *
     * @param name
     * @return JSONObject キャストに失敗した場合はnull
     */
    public JSONObject getJSONObject(String name) {
        try {
            return (JSONObject) this.jsonObject.get(name);
        } catch (Exception exception) {
            return null;
        }
    }
    
    /**
     * JSONオブジェクト内に名前を指定してJSONオブジェクトをセットする。
     *
     * @param name
     * @param jsonObject
     */
    public void putJSONObject(String name, JSONObject jsonObject) {
        this.jsonObject.put(name, jsonObject);
    }
    
    /**
     * 値をJSONに変換する。
     *
     * @param value
     * @return JSON
     */
    private String buildValueOfJSON(Object value) {
        StringBuilder json = new StringBuilder();
        // JSONオブジェクト
        if (value instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) value;
            json.append(jsonObject.toString());
        }
        // 配列
        if (value instanceof Object[]) {
            json.append("[");
            Object[] values = (Object[]) value;
            for (Object temporaryValue: values) {
                if (json.length() > 1) {
                    json.append(",");
                }
                json.append(this.buildValueOfJSON(temporaryValue));
            }
            json.append("]");
        }
        // 文字列
        if (value instanceof String) {
            json.append("\"");
            String stringValue = (String) value;
            stringValue = stringValue.replace("\\", "\\\\");
            stringValue = stringValue.replace("\b", "\\b");
            stringValue = stringValue.replace("\f", "\\f");
            stringValue = stringValue.replace("\n", "\\n");
            stringValue = stringValue.replace("\r", "\\r");
            stringValue = stringValue.replace("\t", "\\t");
            json.append(stringValue);
            json.append("\"");
        }
        // 真偽値
        if (value instanceof Boolean) {
            boolean booleanValue = (boolean) value;
            if (booleanValue) {
                json.append("true");
            } else {
                json.append("false");
            }
        }
        // 数値
        if (value instanceof Number) {
            json.append(value.toString());
        }
        // null
        if (json.length() == 0) {
            json.append("null");
        }
        return json.toString();
    }
    
    /**
     * 内部のオブジェクトからJSONを出力する。
     *
     * @return JSON
     */
    @Override
    public String toString() {
        if (this.jsonObject.size() == 1 && this.jsonObject.containsKey(0)) {
            return buildValueOfJSON(this.jsonObject.get(0));
        }
        StringBuilder json = new StringBuilder();
        json.append("{");
        for (String key: this.jsonObject.getKeysAtString()) {
            if (json.length() > 1) {
                json.append(",");
            }
            json.append(this.buildValueOfJSON(key));
            json.append(":");
            Object value = this.jsonObject.get(key);
            json.append(this.buildValueOfJSON(value));
        }
        json.append("}");
        return json.toString();
    }
}
