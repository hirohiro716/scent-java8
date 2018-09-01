package com.hirohiro716;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.hirohiro716.datetime.Datetime;

/**
 * 値を変換するクラス.
 * @author hiro
 */
public class StringConverter {

    /**
     * 環境ごとの改行文字を取得しておく
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * 一括変換に使用するパラメータ
     */
    private final RudeArray params = new RudeArray();

    /**
     * 変換のパターン
     * @author hiro
     */
    private enum Pattern {
        TRIM,
        REPLACE,
        REPLACE_CR,
        REPLACE_LF,
        REPLACE_CRLF,
        REPLACE_NARROW_SPACE,
        REPLACE_WIDE_SPACE,
        REPLACE_TAB,
        SUB_STRING,
        PADDING_LEFT,
        PADDING_RIGHT,
        INTEGER,
        DECIMAL,
        TRY_NON_FRACTION,
        DATETIME,
        TELEPHONE,
        ENCRYPT_SHA256,
        ENCRYPT_AES128,
        DECRYPT_AES128,
        WIDE_TO_NARROW,
        NARROW_TO_WIDE,
        UPPER_TO_LOWER,
        LOWER_TO_UPPER,
        HIRAGANA_TO_KATAKANA,
        KATAKANA_TO_HIRAGANA;
    }

    /**
     * 両端の余白を取り除く処理を組み込む.
     */
    public void appendTrim() {
        this.params.put(Pattern.TRIM, null);
    }

    /**
     * 文字列を置き換える処理を組み込む.
     * @param targetString 検索文字列
     * @param replaceString 置き換え文字列
     */
    public void appendReplace(String targetString, String replaceString) {
        this.params.put(Pattern.REPLACE, new String[] {targetString, replaceString});
    }

    /**
     * キャリッジリターンを置き換える処理を組み込む.
     * @param replaceString 置き換え文字列
     */
    public void appendReplaceCr(String replaceString) {
        this.params.put(Pattern.REPLACE_CR, replaceString);
    }

    /**
     * ラインフィードを置き換える処理を組み込む.
     * @param replaceString 置き換え文字列
     */
    public void appendReplaceLf(String replaceString) {
        this.params.put(Pattern.REPLACE_LF, replaceString);
    }

    /**
     * CRLFを置き換える処理を組み込む.
     * @param replaceString 置き換え文字列
     */
    public void appendReplaceCrLf(String replaceString) {
        this.params.put(Pattern.REPLACE_CRLF, replaceString);
    }

    /**
     * 半角スペースを置き換える処理を組み込む.
     * @param replaceString 置き換え文字列
     */
    public void appendReplaceNarrowSpace(String replaceString) {
        this.params.put(Pattern.REPLACE_NARROW_SPACE, replaceString);
    }

    /**
     * 全角スペースを置き換える処理を組み込む.
     * @param replaceString 置き換え文字列
     */
    public void appendReplaceWideSpace(String replaceString) {
        this.params.put(Pattern.REPLACE_WIDE_SPACE, replaceString);
    }

    /**
     * タブ文字を置き換える処理を組み込む.
     * @param replaceString 置き換え文字列
     */
    public void appendReplaceTab(String replaceString) {
        this.params.put(Pattern.REPLACE_TAB, replaceString);
    }

    /**
     * 開始位置を指定して文字列を抜き出す処理を組み込む.
     * @param start 開始位置
     */
    public void appendSubString(int start) {
        this.params.put(Pattern.SUB_STRING, new int[]{start});
    }

    /**
     * 開始位置と文字数を指定して文字列を抜き出す処理を組み込む.
     * @param start 開始位置
     * @param length 文字数
     */
    public void appendSubString(int start, int length) {
        this.params.put(Pattern.SUB_STRING, new int[]{start, length});
    }

    /**
     * 指定された桁数まで指定された文字で左埋めの処理を組み込む.
     * @param length 固定長数
     * @param paddingChar 埋める文字列
     */
    public void appendPaddingLeft(int length, char paddingChar) {
        this.params.put(Pattern.PADDING_LEFT, new Object[] {length, paddingChar});
    }

    /**
     * 指定された桁数まで指定された文字で右埋めの処理を組み込む.
     * @param length 固定長数
     * @param paddingChar 埋める文字列
     */
    public void appendPaddingRight(int length, char paddingChar) {
        this.params.put(Pattern.PADDING_RIGHT, new Object[] {length, paddingChar});
    }

    /**
     * 数値として有効な文字列のみを抜き出す処理を組み込む.
     */
    public void appendIntegerString() {
        this.params.put(Pattern.INTEGER, null);
    }

    /**
     * 数値として有効な文字列のみを抜き出す処理を組み込む.
     */
    public void appendDecimalString() {
        this.params.put(Pattern.DECIMAL, null);
    }

    /**
     * 数値として有効な文字列のみを抜き出し小数点を省略する処理を組み込む.
     */
    public void appendTryNonFractionString() {
        this.params.put(Pattern.TRY_NON_FRACTION, null);
    }

    /**
     * 電話番号として有効な文字列のみ（数字,-）を抜き出す処理を組み込む.
     */
    public void appendTelephone() {
        this.params.put(Pattern.TELEPHONE, null);
    }

    /**
     * java.util.Dateオブジェクトまたは日付として有効な文字列をyyyy-MM-dd形式に変換する処理を組み込む.
     */
    public void appendDatetime() {
        this.params.put(Pattern.DATETIME, null);
    }

    /**
     * java.util.Dateオブジェクトまたは日付として有効な文字列をパターンに応じて変換する処理を組み込む.
     * @param formatPattern フォーマットパターン（yyyy-MM-ddなど）
     */
    public void appendDatetime(String formatPattern) {
        this.params.put(Pattern.DATETIME, formatPattern);
    }

    /**
     * SHA256で文字列を暗号化する処理を組み込む.
     */
    public void appendEncryptSha256() {
        this.params.put(Pattern.ENCRYPT_SHA256, null);
    }

    /**
     * キーを指定してAES128で暗号化する処理を組み込む.
     * @param key キー
     */
    public void appendEncryptAes128(String key) {
        this.params.put(Pattern.ENCRYPT_AES128, key);
    }

    /**
     * キーを指定してAES128で暗号化された文字列を復号化する処理を組み込む.
     * @param key キー
     */
    public void appendDecryptAes128(String key) {
        this.params.put(Pattern.DECRYPT_AES128, key);
    }

    /**
     * 全角文字を半角に変換する.
     */
    public void appendWideToNarrow() {
        this.params.put(Pattern.WIDE_TO_NARROW, null);
    }

    /**
     * 半角文字を全角に変換する.
     */
    public void appendNarrowToWide() {
        this.params.put(Pattern.NARROW_TO_WIDE, null);
    }

    /**
     * 大文字を小文字に変換する.
     */
    public void appendUpperToLower() {
        this.params.put(Pattern.UPPER_TO_LOWER, null);
    }

    /**
     * 小文字を大文字に変換する.
     */
    public void appendLowerToUpper() {
        this.params.put(Pattern.LOWER_TO_UPPER, null);
    }

    /**
     * 全角ひらがなを全角カタカナに変換する.
     */
    public void appendHiraganaToKatakana() {
        this.params.put(Pattern.HIRAGANA_TO_KATAKANA, null);
    }

    /**
     * 全角カタカナを全角ひらがなに変換する.
     */
    public void appendKatakanaToHiragana() {
        this.params.put(Pattern.KATAKANA_TO_HIRAGANA, null);
    }

    /**
     * 設定した変換パラメータを初期化する.
     */
    public void clear() {
        this.params.clear();
    }

    /**
     * 値の変換を実行する.
     * @param target 変換対象
     * @return 結果
     */
    public String execute(Object target) {
        String value = nullReplace(target, "");
        for (Object patternObject : this.params.getKeys()) {
            Pattern pattern = (Pattern) patternObject;
            switch (pattern) {
            case TRIM:
                value = trim(value);
                break;
            case REPLACE:
                String[] paramsStrings = this.params.getStrings(pattern);
                value = value.replace(paramsStrings[0], paramsStrings[1]);
                break;
            case REPLACE_CR:
                value = value.replace("\r", this.params.getString(pattern));
                break;
            case REPLACE_LF:
                value = value.replace("\n", this.params.getString(pattern));
                break;
            case REPLACE_CRLF:
                value = value.replace("\r\n", this.params.getString(pattern));
                break;
            case REPLACE_NARROW_SPACE:
                value = value.replace(" ", this.params.getString(pattern));
                break;
            case REPLACE_WIDE_SPACE:
                value = value.replace("　", this.params.getString(pattern));
                break;
            case REPLACE_TAB:
                value = value.replace("\t", this.params.getString(pattern));
                break;
            case SUB_STRING:
                int[] start_length;
                start_length = this.params.getIntegers(pattern);
                if (start_length.length == 1) {
                    value = subString(value, start_length[0]);
                } else {
                    value = subString(value, start_length[0], start_length[1]);
                }
                break;
            case PADDING_LEFT:
                Object[] paramsPaddingLeft = (Object[]) this.params.get(pattern);
                int lengthPaddingLeft = (int) paramsPaddingLeft[0];
                char paddingLeftChar = (char) paramsPaddingLeft[1];
                value = paddingLeft(value, paddingLeftChar, lengthPaddingLeft);
                break;
            case PADDING_RIGHT:
                Object[] paramsPaddingRight = (Object[]) this.params.get(pattern);
                int lengthPaddingRight = (int) paramsPaddingRight[0];
                char paddingRightChar = (char) paramsPaddingRight[1];
                value = paddingRight(value, paddingRightChar, lengthPaddingRight);
                break;
            case INTEGER:
                value = extractInteger(value);
                break;
            case DECIMAL:
                value = extractDecimal(value);
                break;
            case TRY_NON_FRACTION:
                value = extractDecimal(value);
                value = tryNonFraction(value);
                break;
            case TELEPHONE:
                value = extractTelephoneNumber(value);
                break;
            case DATETIME:
                Date date;
                if (target instanceof Date) {
                    date = (Date) target;
                } else {
                    date = Datetime.stringToDate(value);
                }
                if (date != null) {
                    String formatPattern = this.params.getString(pattern);
                    if (formatPattern == null) {
                        value = Datetime.dateToString(date);
                    } else {
                        value = Datetime.dateToString(date, formatPattern);
                    }
                } else {
                    value = "";
                }
                break;
            case ENCRYPT_SHA256:
                value = encryptSha256(value);
                break;
            case ENCRYPT_AES128:
                value = encryptAes128(this.params.getString(pattern), value);
                break;
            case DECRYPT_AES128:
                value = decryptAes128(this.params.getString(pattern), value);
                break;
            case WIDE_TO_NARROW:
                value = wideToNarrow(value);
                break;
            case NARROW_TO_WIDE:
                value = narrowToWide(value);
                break;
            case UPPER_TO_LOWER:
                value = value.toLowerCase();
                break;
            case LOWER_TO_UPPER:
                value = value.toUpperCase();
                break;
            case HIRAGANA_TO_KATAKANA:
                value = hiraganaToKatakana(value);
                break;
            case KATAKANA_TO_HIRAGANA:
                value = katakanaToHiragana(value);
                break;
            }
        }
        return value;
    }

    /**
     * 両端の全角スペースと半角スペースを除く.
     * @param value 対象
     * @return 文字列
     */
    public static String trim(String value) {
        String newValue = nullReplace(value, "").trim();
        if (newValue.length() > 0) {
            while (newValue.length() > 0 && newValue.substring(0, 1).equals("　")) {
                newValue = newValue.substring(1).trim();
            }
            while (newValue.length() > 0 && newValue.substring(newValue.length() - 1).equals("　")) {
                newValue = newValue.substring(0, newValue.length() - 1).trim();
            }
        }
        return newValue;
    }

    /**
     * 文字列の左側を指定桁数まで指定文字を埋める.
     * @param value 対象文字列
     * @param paddingCharactor 埋める文字
     * @param length 桁数
     * @return 結果
     */
    public static String paddingLeft(String value, char paddingCharactor, int length) {
        String paddingString = repeat(String.valueOf(paddingCharactor), length) + value;
        return subString(paddingString, paddingString.length() - length, length);
    }

    /**
     * 文字列の右側を指定桁数まで指定文字を埋める.
     * @param value 対象文字列
     * @param paddingCharactor 埋める文字
     * @param length 桁数
     * @return 結果
     */
    public static String paddingRight(String value, char paddingCharactor, int length) {
        String paddingString = value + repeat(String.valueOf(paddingCharactor), length);
        return subString(paddingString, 0, length);
    }

    /**
     * 文字列をStringBuilderで連結して返す.
     * @param objects 文字列郡
     * @return 連結した文字列
     */
    public static String join(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object s : objects) {
            if (s != null) {
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 指定回数繰り返した文字列を取得する.
     * @param string 繰り返す文字列
     * @param count 回数
     * @return 繰り返し連結した文字列
     */
    public static String repeat(String string, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
     * nullを指定の文字列に置き換える.
     * @param value 対象
     * @param newString nullの場合に返す文字列
     * @return 文字列
     */
    public static String nullReplace(Object value, String newString) {
        if (value == null) {
            return newString;
        }
        return value.toString();
    }

    /**
     * String型をboolean型に変換する. 変換できない場合はfalse.
     * @param boolString 有効文字列
     * @return 結果
     */
    public static boolean stringToBoolean(String boolString) {
        try {
            return Boolean.parseBoolean(boolString);
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * String型をshort型に変換する. 変換できない場合はnullを取得する. 小数点は無効です.
     * @param shortString 有効文字列
     * @return 結果
     */
    public static Short stringToShort(String shortString) {
        try {
            return Short.parseShort(shortString);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * String型をInteger型に変換する. 変換できない場合はnullを取得する. 小数点は無効です.
     * @param intString 有効文字列
     * @return 結果
     */
    public static Integer stringToInteger(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * String型をlong型に変換する. 変換できない場合はnullを取得する. 小数点は無効です.
     * @param longString 有効文字列
     * @return 結果
     */
    public static Long stringToLong(String longString) {
        try {
            return Long.parseLong(longString);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * String型をFloat型に変換する. 変換できない場合はnullを取得する.
     * @param floatString 有効文字列
     * @return 結果
     */
    public static Float stringToFloat(String floatString) {
        try {
            return Float.parseFloat(floatString);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * String型をDouble型に変換する. 変換できない場合はnullを取得する.
     * @param doubleString 有効文字列
     * @return 結果
     */
    public static Double stringToDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 小数点を省略できる場合は削除
     * @param value 小数
     * @return 整数または小数の文字列
     */
    public static String tryNonFraction(String value) {
        if (value == null) {
            return null;
        }
        if (value.endsWith(".0")) {
            return value.substring(0, value.length() - 2);
        }
        return value;
    }

    /**
     * 小数点を省略できる場合は削除
     * @param value 小数
     * @return 整数または小数の文字列
     */
    public static String tryNonFraction(Double value) {
        if (value == null) {
            return null;
        }
        String valueString = value.toString();
        if (valueString.endsWith(".0")) {
            return valueString.substring(0, valueString.length() - 2);
        }
        return valueString;
    }

    /**
     * value(元文字列)のstart(開始位置)からlength（文字数）分を取得する. 開始位置に矛盾があった場合は空文字""を取得する.<br>
     * lengthがvalueの文字数を超えた範囲を指定していた場合はあるだけ返す.
     * @param value 元の文字列
     * @param start 開始位置
     * @param length 文字数
     * @return 結果
     */
    public static String subString(String value, int start, int length) {
        if (value == null) {
            return value;
        }
        int valueLength = value.length();
        if (valueLength < start) {
            return "";
        }
        int newLength = length;
        if (length + start > valueLength) {
            newLength = valueLength - start;
        }
        return value.substring(start, newLength + start);
    }

    /**
     * value(元文字列)のstart（開始位置）からの文字列をすべて返す. 開始位置に矛盾があった場合は空文字""を取得する.
     * @param value 元の文字列
     * @param start 開始位置
     * @return 結果
     */
    public static String subString(String value, int start) {
        if (value == null) {
            return value;
        }
        int valueLength = value.length();
        if (valueLength < start) {
            return "";
        }
        return value.substring(start, valueLength);
    }

    /**
     * 文字列をsha256で不可逆暗号化して返す.
     * @param value 元の値
     * @return 暗号化した値
     */
    public static String encryptSha256(String value) {
        MessageDigest md;
        try {
            md = (MessageDigest) MessageDigest.getInstance("SHA-256").clone();
        } catch (NoSuchAlgorithmException | CloneNotSupportedException exception) {
            return "";
        }
        md.update(value.getBytes());
        byte[] bytes = md.digest();
        return ByteConverter.bytesToString(bytes);
    }

    /**
     * 文字列をAES128で暗号化して返す. 必ず16文字の暗号化キーを指定する. 復号化にはdecryptAes128を使用する.
     * @param key 16文字の暗号化キーを指定
     * @param value 元の値
     * @return 暗号化した値
     */
    public static String encryptAes128(String key, String value) {
        try {
            StringBuilder stringBuilder = new StringBuilder(key);
            for (int i = 0; i < 16; i++) {
                stringBuilder.append(" ");
            }
            String new_key = stringBuilder.toString().substring(0, 16);
            SecretKeySpec sksSpec = new SecretKeySpec(new_key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");  // 今のところ別のインスタンスを返してくれてる？
            cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
            byte[] bytes = cipher.doFinal(value.getBytes());
            return ByteConverter.bytesToString(bytes);
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * encryptAes128メソッドで暗号化された文字列を復号化する.
     * @param key 暗号化の際に使用したキーを指定
     * @param encrypted encrypt_aes128メソッドで暗号化された文字列
     * @return 元の値
     */
    public static String decryptAes128(String key, String encrypted) {
        try {
            StringBuilder stringBuilder = new StringBuilder(key);
            for (int i = 0; i < 16; i++) {
                stringBuilder.append(" ");
            }
            String new_key = stringBuilder.toString().substring(0, 16);
            SecretKeySpec sksSpec = new SecretKeySpec(new_key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");  // 別のインスタンスを返してくれる？
            cipher.init(Cipher.DECRYPT_MODE, sksSpec);
            byte[] bytes = ByteConverter.stringToBytes(encrypted);
            return new String(cipher.doFinal(bytes));
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * 番号として有効な文字列だけを取得する.
     * @param value 元の文字列
     * @return 変換後
     */
    public static String extractInteger(String value) {
        String number_pattern = "[^0-9]{1,}";
        return value.replaceAll(number_pattern, "");
    }

    /**
     * 少数として有効な文字列だけを取得する.
     * @param value 元の文字列
     * @return 変換後
     */
    public static String extractDecimal(String value) {
        String number_pattern = "[^0-9\\.]{1,}";
        return value.replaceAll(number_pattern, "");

    }

    /**
     * 電話番号として有効な文字列だけを取得する.
     * @param value 元の文字列
     * @return 変換後
     */
    public static String extractTelephoneNumber(String value) {
        String telephone_pattern = "[^0-9-]{1,}";
        return value.replaceAll(telephone_pattern, "");

    }

    /**
     * 全角文字を半角文字に変換する.
     * @param value 変換対象
     * @return 変換後
     */
    public static String wideToNarrow(String value) {
        if (toNarrowList == null) {
            toNarrowList = createWideToNarrowList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toNarrowList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toNarrowList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 半角文字を全角文字に変換する.
     * @param value 変換対象
     * @return 変換後
     */
    public static String narrowToWide(String value) {
        if (toWideList == null) {
            toWideList = createNarrowToWideList();
        }
        if (value == null) {
            return "";
        }
        String convString;
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (subString(value, i + 1, 1).equals("ﾟ") || subString(value, i + 1, 1).equals("ﾞ")) {
                convString = subString(value, i, 2);
                i += 1;
            } else {
                convString = subString(value, i, 1);
            }
            if (toWideList.containsKey(convString)) {
                newValue = join(newValue, toWideList.getString(convString));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * ひらがなをカタカナに変換する.
     * @param value 変換対象
     * @return 変換後
     */
    public static String hiraganaToKatakana(String value) {
        if (toKatakanaList == null) {
            toKatakanaList = createHiraganaToKatakanaList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toKatakanaList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toKatakanaList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * カタカナをひらがなに変換する.
     * @param value 変換対象
     * @return 変換後
     */
    public static String katakanaToHiragana(String value) {
        if (toHiraganaList == null) {
            toHiraganaList = createKatakanaToHiraganaList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toHiraganaList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toHiraganaList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 濁点を付けて返す.
     * @param value 変換対象
     * @return 変換後
     */
    public static String dakuten(String value) {
        if (toDakutenList == null) {
            toDakutenList = createClearToDakutenList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toDakutenList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toDakutenList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 濁点を取って返す.
     * @param value 変換対象
     * @return 変換後
     */
    public static String dakutenExcept(String value) {
        if (dakutenToList == null) {
            dakutenToList = createDakutenToClearList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (dakutenToList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, dakutenToList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 半濁点を付けて返す.
     * @param value 変換対象
     * @return 変換後
     */
    public static String dakutenHalf(String value) {
        if (toDakutenHalfList == null) {
            toDakutenHalfList = createClearToDakutenHalfList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toDakutenHalfList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toDakutenHalfList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 半濁点を取って返す.
     * @param value 変換対象
     * @return 変換後
     */
    public static String dakutenHalfExcept(String value) {
        if (dakutenHalfToList == null) {
            dakutenHalfToList = createDakutenHalfToClearList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (dakutenHalfToList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, dakutenHalfToList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 日本語の小文字を大文字にして返す.
     * @param value 変換対象
     * @return 変換後
     */
    public static String lowerToUpperJapanese(String value) {
        if (toUpperJapaneseList == null) {
            toUpperJapaneseList = createLowerToUpperJapaneseList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toUpperJapaneseList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toUpperJapaneseList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    /**
     * 日本語の大文字を小文字にして返す.
     * @param value 変換対象
     * @return 変換後
     */
    public static String upperToLowerJapanese(String value) {
        if (toLowerJapaneseList == null) {
            toLowerJapaneseList = createUpperToLowerJapaneseList();
        }
        if (value == null) {
            return "";
        }
        String newValue = "";
        for (int i = 0; i < value.length(); i++) {
            if (toLowerJapaneseList.containsKey(value.substring(i, i + 1))) {
                newValue = join(newValue, toLowerJapaneseList.getString(value.substring(i, i + 1)));
            } else {
                newValue = join(newValue, value.substring(i, i + 1));
            }
        }
        return newValue;
    }

    // 変換表連想配列
    private static RudeArray toNarrowList;
    private static RudeArray toWideList;
    private static RudeArray toKatakanaList;
    private static RudeArray toHiraganaList;
    private static RudeArray toDakutenList;
    private static RudeArray dakutenToList;
    private static RudeArray toDakutenHalfList;
    private static RudeArray dakutenHalfToList;
    private static RudeArray toUpperJapaneseList;
    private static RudeArray toLowerJapaneseList;

    /**
     * ひらがなからカタカナの変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createKatakanaToHiraganaList() {
        RudeArray array = new RudeArray();
        array.put("ァ", "ぁ");
        array.put("ア", "あ");
        array.put("ィ", "ぃ");
        array.put("イ", "い");
        array.put("ゥ", "ぅ");
        array.put("ウ", "う");
        array.put("ェ", "ぇ");
        array.put("エ", "え");
        array.put("ォ", "ぉ");
        array.put("オ", "お");
        array.put("カ", "か");
        array.put("ガ", "が");
        array.put("キ", "き");
        array.put("ギ", "ぎ");
        array.put("ク", "く");
        array.put("グ", "ぐ");
        array.put("ケ", "け");
        array.put("ゲ", "げ");
        array.put("コ", "こ");
        array.put("ゴ", "ご");
        array.put("サ", "さ");
        array.put("ザ", "ざ");
        array.put("シ", "し");
        array.put("ジ", "じ");
        array.put("ス", "す");
        array.put("ズ", "ず");
        array.put("セ", "せ");
        array.put("ゼ", "ぜ");
        array.put("ソ", "そ");
        array.put("ゾ", "ぞ");
        array.put("タ", "た");
        array.put("ダ", "だ");
        array.put("チ", "ち");
        array.put("ヂ", "ぢ");
        array.put("ッ", "っ");
        array.put("ツ", "つ");
        array.put("ヅ", "づ");
        array.put("テ", "て");
        array.put("デ", "で");
        array.put("ト", "と");
        array.put("ド", "ど");
        array.put("ナ", "な");
        array.put("ニ", "に");
        array.put("ヌ", "ぬ");
        array.put("ネ", "ね");
        array.put("ノ", "の");
        array.put("ハ", "は");
        array.put("バ", "ば");
        array.put("パ", "ぱ");
        array.put("ヒ", "ひ");
        array.put("ビ", "び");
        array.put("ピ", "ぴ");
        array.put("フ", "ふ");
        array.put("ブ", "ぶ");
        array.put("プ", "ぷ");
        array.put("ヘ", "へ");
        array.put("ベ", "べ");
        array.put("ペ", "ぺ");
        array.put("ホ", "ほ");
        array.put("ボ", "ぼ");
        array.put("ポ", "ぽ");
        array.put("マ", "ま");
        array.put("ミ", "み");
        array.put("ム", "む");
        array.put("メ", "め");
        array.put("モ", "も");
        array.put("ャ", "ゃ");
        array.put("ヤ", "や");
        array.put("ュ", "ゅ");
        array.put("ユ", "ゆ");
        array.put("ョ", "ょ");
        array.put("ヨ", "よ");
        array.put("ラ", "ら");
        array.put("リ", "り");
        array.put("ル", "る");
        array.put("レ", "れ");
        array.put("ロ", "ろ");
        array.put("ヮ", "ゎ");
        array.put("ワ", "わ");
        array.put("ヰ", "ゐ");
        array.put("ヱ", "ゑ");
        array.put("ヲ", "を");
        array.put("ン", "ん");
        return array;
    }

    /**
     * カタカナからひらがなの変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createHiraganaToKatakanaList() {
        RudeArray array = new RudeArray();
        array.put("ぁ", "ァ");
        array.put("あ", "ア");
        array.put("ぃ", "ィ");
        array.put("い", "イ");
        array.put("ぅ", "ゥ");
        array.put("う", "ウ");
        array.put("ぇ", "ェ");
        array.put("え", "エ");
        array.put("ぉ", "ォ");
        array.put("お", "オ");
        array.put("か", "カ");
        array.put("が", "ガ");
        array.put("き", "キ");
        array.put("ぎ", "ギ");
        array.put("く", "ク");
        array.put("ぐ", "グ");
        array.put("け", "ケ");
        array.put("げ", "ゲ");
        array.put("こ", "コ");
        array.put("ご", "ゴ");
        array.put("さ", "サ");
        array.put("ざ", "ザ");
        array.put("し", "シ");
        array.put("じ", "ジ");
        array.put("す", "ス");
        array.put("ず", "ズ");
        array.put("せ", "セ");
        array.put("ぜ", "ゼ");
        array.put("そ", "ソ");
        array.put("ぞ", "ゾ");
        array.put("た", "タ");
        array.put("だ", "ダ");
        array.put("ち", "チ");
        array.put("ぢ", "ヂ");
        array.put("っ", "ッ");
        array.put("つ", "ツ");
        array.put("づ", "ヅ");
        array.put("て", "テ");
        array.put("で", "デ");
        array.put("と", "ト");
        array.put("ど", "ド");
        array.put("な", "ナ");
        array.put("に", "ニ");
        array.put("ぬ", "ヌ");
        array.put("ね", "ネ");
        array.put("の", "ノ");
        array.put("は", "ハ");
        array.put("ば", "バ");
        array.put("ぱ", "パ");
        array.put("ひ", "ヒ");
        array.put("び", "ビ");
        array.put("ぴ", "ピ");
        array.put("ふ", "フ");
        array.put("ぶ", "ブ");
        array.put("ぷ", "プ");
        array.put("へ", "ヘ");
        array.put("べ", "ベ");
        array.put("ぺ", "ペ");
        array.put("ほ", "ホ");
        array.put("ぼ", "ボ");
        array.put("ぽ", "ポ");
        array.put("ま", "マ");
        array.put("み", "ミ");
        array.put("む", "ム");
        array.put("め", "メ");
        array.put("も", "モ");
        array.put("ゃ", "ャ");
        array.put("や", "ヤ");
        array.put("ゅ", "ュ");
        array.put("ゆ", "ユ");
        array.put("ょ", "ョ");
        array.put("よ", "ヨ");
        array.put("ら", "ラ");
        array.put("り", "リ");
        array.put("る", "ル");
        array.put("れ", "レ");
        array.put("ろ", "ロ");
        array.put("ゎ", "ヮ");
        array.put("わ", "ワ");
        array.put("ゐ", "ヰ");
        array.put("ゑ", "ヱ");
        array.put("を", "ヲ");
        array.put("ん", "ン");
        return array;
    }

    /**
     * 全角文字から半角文字の変換リスト
     */
    private static RudeArray createNarrowToWideList() {
        RudeArray array = new RudeArray();
        array.put("a", "ａ");
        array.put("b", "ｂ");
        array.put("c", "ｃ");
        array.put("d", "ｄ");
        array.put("e", "ｅ");
        array.put("f", "ｆ");
        array.put("g", "ｇ");
        array.put("h", "ｈ");
        array.put("i", "ｉ");
        array.put("j", "ｊ");
        array.put("k", "ｋ");
        array.put("l", "ｌ");
        array.put("m", "ｍ");
        array.put("n", "ｎ");
        array.put("o", "ｏ");
        array.put("p", "ｐ");
        array.put("q", "ｑ");
        array.put("r", "ｒ");
        array.put("s", "ｓ");
        array.put("t", "ｔ");
        array.put("u", "ｕ");
        array.put("v", "ｖ");
        array.put("w", "ｗ");
        array.put("x", "ｘ");
        array.put("y", "ｙ");
        array.put("z", "ｚ");
        array.put("A", "Ａ");
        array.put("B", "Ｂ");
        array.put("C", "Ｃ");
        array.put("D", "Ｄ");
        array.put("E", "Ｅ");
        array.put("F", "Ｆ");
        array.put("G", "Ｇ");
        array.put("H", "Ｈ");
        array.put("I", "Ｉ");
        array.put("J", "Ｊ");
        array.put("K", "Ｋ");
        array.put("L", "Ｌ");
        array.put("M", "Ｍ");
        array.put("N", "Ｎ");
        array.put("O", "Ｏ");
        array.put("P", "Ｐ");
        array.put("Q", "Ｑ");
        array.put("R", "Ｒ");
        array.put("S", "Ｓ");
        array.put("T", "Ｔ");
        array.put("U", "Ｕ");
        array.put("V", "Ｖ");
        array.put("W", "Ｗ");
        array.put("X", "Ｘ");
        array.put("Y", "Ｙ");
        array.put("Z", "Ｚ");
        array.put("0", "０");
        array.put("1", "１");
        array.put("2", "２");
        array.put("3", "３");
        array.put("4", "４");
        array.put("5", "５");
        array.put("6", "６");
        array.put("7", "７");
        array.put("8", "８");
        array.put("9", "９");
        array.put("ｧ", "ァ");
        array.put("ｱ", "ア");
        array.put("ｨ", "ィ");
        array.put("ｲ", "イ");
        array.put("ｩ", "ゥ");
        array.put("ｳ", "ウ");
        array.put("ｪ", "ェ");
        array.put("ｴ", "エ");
        array.put("ｫ", "ォ");
        array.put("ｵ", "オ");
        array.put("ｶ", "カ");
        array.put("ｶﾞ", "ガ");
        array.put("ｷ", "キ");
        array.put("ｷﾞ", "ギ");
        array.put("ｸ", "ク");
        array.put("ｸﾞ", "グ");
        array.put("ｹ", "ケ");
        array.put("ｹﾞ", "ゲ");
        array.put("ｺ", "コ");
        array.put("ｺﾞ", "ゴ");
        array.put("ｻ", "サ");
        array.put("ｻﾞ", "ザ");
        array.put("ｼ", "シ");
        array.put("ｼﾞ", "ジ");
        array.put("ｽ", "ス");
        array.put("ｽﾞ", "ズ");
        array.put("ｾ", "セ");
        array.put("ｾﾞ", "ゼ");
        array.put("ｿ", "ソ");
        array.put("ｿﾞ", "ゾ");
        array.put("ﾀ", "タ");
        array.put("ﾀﾞ", "ダ");
        array.put("ﾁ", "チ");
        array.put("ﾁﾞ", "ヂ");
        array.put("ｯ", "ッ");
        array.put("ﾂ", "ツ");
        array.put("ﾂﾞ", "ヅ");
        array.put("ﾃ", "テ");
        array.put("ﾃﾞ", "デ");
        array.put("ﾄ", "ト");
        array.put("ﾄﾞ", "ド");
        array.put("ﾅ", "ナ");
        array.put("ﾆ", "ニ");
        array.put("ﾇ", "ヌ");
        array.put("ﾈ", "ネ");
        array.put("ﾉ", "ノ");
        array.put("ﾊ", "ハ");
        array.put("ﾊﾞ", "バ");
        array.put("ﾊﾟ", "パ");
        array.put("ﾋ", "ヒ");
        array.put("ﾋﾞ", "ビ");
        array.put("ﾋﾟ", "ピ");
        array.put("ﾌ", "フ");
        array.put("ﾌﾞ", "ブ");
        array.put("ﾌﾟ", "プ");
        array.put("ﾍ", "ヘ");
        array.put("ﾍﾞ", "ベ");
        array.put("ﾍﾟ", "ペ");
        array.put("ﾎ", "ホ");
        array.put("ﾎﾞ", "ボ");
        array.put("ﾎﾟ", "ポ");
        array.put("ﾏ", "マ");
        array.put("ﾐ", "ミ");
        array.put("ﾑ", "ム");
        array.put("ﾒ", "メ");
        array.put("ﾓ", "モ");
        array.put("ｬ", "ャ");
        array.put("ﾔ", "ヤ");
        array.put("ｭ", "ュ");
        array.put("ﾕ", "ユ");
        array.put("ｮ", "ョ");
        array.put("ﾖ", "ヨ");
        array.put("ﾗ", "ラ");
        array.put("ﾘ", "リ");
        array.put("ﾙ", "ル");
        array.put("ﾚ", "レ");
        array.put("ﾛ", "ロ");
        array.put("ﾜ", "ワ");
        array.put("ｦ", "ヲ");
        array.put("ﾝ", "ン");
        array.put("ｳﾞ", "ヴ");
        array.put("!", "！");
        array.put("\"", "”");
        array.put("#", "＃");
        array.put("$", "＄");
        array.put("%", "％");
        array.put("&", "＆");
        array.put("'", "’");
        array.put("(", "(");
        array.put(")", ")");
        array.put("=", "＝");
        array.put("-", "－");
        array.put("~", "～");
        array.put("^", "＾");
        array.put("\\", "￥");
        array.put("@", "＠");
        array.put("+", "＋");
        array.put("*", "＊");
        array.put("{", "｛");
        array.put("}", "｝");
        array.put("[", "［");
        array.put("]", "］");
        array.put(";", "；");
        array.put(":", "：");
        array.put("<", "＜");
        array.put(">", "＞");
        array.put(",", "，");
        array.put(".", "．");
        array.put("?", "？");
        array.put("_", "＿");
        array.put("/", "／");
        array.put(" ", "　");
        return array;
    }

    /**
     * 半角文字から全角文字の変換リスト
     */
    private static RudeArray createWideToNarrowList() {
        RudeArray array = new RudeArray();
        array.put("ａ", "a");
        array.put("ｂ", "b");
        array.put("ｃ", "c");
        array.put("ｄ", "d");
        array.put("ｅ", "e");
        array.put("ｆ", "f");
        array.put("ｇ", "g");
        array.put("ｈ", "h");
        array.put("ｉ", "i");
        array.put("ｊ", "j");
        array.put("ｋ", "k");
        array.put("ｌ", "l");
        array.put("ｍ", "m");
        array.put("ｎ", "n");
        array.put("ｏ", "o");
        array.put("ｐ", "p");
        array.put("ｑ", "q");
        array.put("ｒ", "r");
        array.put("ｓ", "s");
        array.put("ｔ", "t");
        array.put("ｕ", "u");
        array.put("ｖ", "v");
        array.put("ｗ", "w");
        array.put("ｘ", "x");
        array.put("ｙ", "y");
        array.put("ｚ", "z");
        array.put("Ａ", "A");
        array.put("Ｂ", "B");
        array.put("Ｃ", "C");
        array.put("Ｄ", "D");
        array.put("Ｅ", "E");
        array.put("Ｆ", "F");
        array.put("Ｇ", "G");
        array.put("Ｈ", "H");
        array.put("Ｉ", "I");
        array.put("Ｊ", "J");
        array.put("Ｋ", "K");
        array.put("Ｌ", "L");
        array.put("Ｍ", "M");
        array.put("Ｎ", "N");
        array.put("Ｏ", "O");
        array.put("Ｐ", "P");
        array.put("Ｑ", "Q");
        array.put("Ｒ", "R");
        array.put("Ｓ", "S");
        array.put("Ｔ", "T");
        array.put("Ｕ", "U");
        array.put("Ｖ", "V");
        array.put("Ｗ", "W");
        array.put("Ｘ", "X");
        array.put("Ｙ", "Y");
        array.put("Ｚ", "Z");
        array.put("０", "0");
        array.put("１", "1");
        array.put("２", "2");
        array.put("３", "3");
        array.put("４", "4");
        array.put("５", "5");
        array.put("６", "6");
        array.put("７", "7");
        array.put("８", "8");
        array.put("９", "9");
        array.put("ァ", "ｧ");
        array.put("ア", "ｱ");
        array.put("ィ", "ｨ");
        array.put("イ", "ｲ");
        array.put("ゥ", "ｩ");
        array.put("ウ", "ｳ");
        array.put("ェ", "ｪ");
        array.put("エ", "ｴ");
        array.put("ォ", "ｫ");
        array.put("オ", "ｵ");
        array.put("カ", "ｶ");
        array.put("ガ", "ｶﾞ");
        array.put("キ", "ｷ");
        array.put("ギ", "ｷﾞ");
        array.put("ク", "ｸ");
        array.put("グ", "ｸﾞ");
        array.put("ケ", "ｹ");
        array.put("ゲ", "ｹﾞ");
        array.put("コ", "ｺ");
        array.put("ゴ", "ｺﾞ");
        array.put("サ", "ｻ");
        array.put("ザ", "ｻﾞ");
        array.put("シ", "ｼ");
        array.put("ジ", "ｼﾞ");
        array.put("ス", "ｽ");
        array.put("ズ", "ｽﾞ");
        array.put("セ", "ｾ");
        array.put("ゼ", "ｾﾞ");
        array.put("ソ", "ｿ");
        array.put("ゾ", "ｿﾞ");
        array.put("タ", "ﾀ");
        array.put("ダ", "ﾀﾞ");
        array.put("チ", "ﾁ");
        array.put("ヂ", "ﾁﾞ");
        array.put("ッ", "ｯ");
        array.put("ツ", "ﾂ");
        array.put("ヅ", "ﾂﾞ");
        array.put("テ", "ﾃ");
        array.put("デ", "ﾃﾞ");
        array.put("ト", "ﾄ");
        array.put("ド", "ﾄﾞ");
        array.put("ナ", "ﾅ");
        array.put("ニ", "ﾆ");
        array.put("ヌ", "ﾇ");
        array.put("ネ", "ﾈ");
        array.put("ノ", "ﾉ");
        array.put("ハ", "ﾊ");
        array.put("バ", "ﾊﾞ");
        array.put("パ", "ﾊﾟ");
        array.put("ヒ", "ﾋ");
        array.put("ビ", "ﾋﾞ");
        array.put("ピ", "ﾋﾟ");
        array.put("フ", "ﾌ");
        array.put("ブ", "ﾌﾞ");
        array.put("プ", "ﾌﾟ");
        array.put("ヘ", "ﾍ");
        array.put("ベ", "ﾍﾞ");
        array.put("ペ", "ﾍﾟ");
        array.put("ホ", "ﾎ");
        array.put("ボ", "ﾎﾞ");
        array.put("ポ", "ﾎﾟ");
        array.put("マ", "ﾏ");
        array.put("ミ", "ﾐ");
        array.put("ム", "ﾑ");
        array.put("メ", "ﾒ");
        array.put("モ", "ﾓ");
        array.put("ャ", "ｬ");
        array.put("ヤ", "ﾔ");
        array.put("ュ", "ｭ");
        array.put("ユ", "ﾕ");
        array.put("ョ", "ｮ");
        array.put("ヨ", "ﾖ");
        array.put("ラ", "ﾗ");
        array.put("リ", "ﾘ");
        array.put("ル", "ﾙ");
        array.put("レ", "ﾚ");
        array.put("ロ", "ﾛ");
        array.put("ワ", "ﾜ");
        array.put("ヲ", "ｦ");
        array.put("ン", "ﾝ");
        array.put("ヴ", "ｳﾞ");
        array.put("！", "!");
        array.put("”", "\"");
        array.put("＃", "#");
        array.put("＄", "$");
        array.put("％", "%");
        array.put("＆", "&");
        array.put("’", "'");
        array.put("(", "(");
        array.put(")", ")");
        array.put("＝", "=");
        array.put("－", "-");
        array.put("～", "~");
        array.put("＾", "^");
        array.put("￥", "\\");
        array.put("＠", "@");
        array.put("＋", "+");
        array.put("＊", "*");
        array.put("｛", "{");
        array.put("｝", "}");
        array.put("［", "[");
        array.put("］", "]");
        array.put("；", ";");
        array.put("：", ":");
        array.put("＜", "<");
        array.put("＞", ">");
        array.put("，", ",");
        array.put("．", ".");
        array.put("？", "?");
        array.put("＿", "_");
        array.put("／", "/");
        array.put("　", " ");
        return array;
    }

    /**
     * 濁点のひらがな/カタカナへの変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createClearToDakutenList() {
        RudeArray array = new RudeArray();
        array.put("か", "が");
        array.put("き", "ぎ");
        array.put("く", "ぐ");
        array.put("け", "げ");
        array.put("こ", "ご");
        array.put("さ", "ざ");
        array.put("し", "じ");
        array.put("す", "ず");
        array.put("せ", "ぜ");
        array.put("そ", "ぞ");
        array.put("た", "だ");
        array.put("ち", "ぢ");
        array.put("つ", "づ");
        array.put("て", "で");
        array.put("と", "ど");
        array.put("は", "ば");
        array.put("ひ", "び");
        array.put("ふ", "ぶ");
        array.put("へ", "べ");
        array.put("ほ", "ぼ");
        array.put("カ", "ガ");
        array.put("キ", "ギ");
        array.put("ク", "グ");
        array.put("ケ", "ゲ");
        array.put("コ", "ゴ");
        array.put("サ", "ザ");
        array.put("シ", "ジ");
        array.put("ス", "ズ");
        array.put("セ", "ゼ");
        array.put("ソ", "ゾ");
        array.put("タ", "ダ");
        array.put("チ", "ヂ");
        array.put("ツ", "ヅ");
        array.put("テ", "デ");
        array.put("ト", "ド");
        array.put("ハ", "バ");
        array.put("ヒ", "ビ");
        array.put("フ", "ブ");
        array.put("ヘ", "ベ");
        array.put("ホ", "ボ");
        return array;
    }

    /**
     * 半濁点のひらがな/カタカナへの変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createClearToDakutenHalfList() {
        RudeArray array = new RudeArray();
        array.put("は", "ぱ");
        array.put("ひ", "ぴ");
        array.put("ふ", "ぷ");
        array.put("へ", "ぺ");
        array.put("ほ", "ぽ");
        array.put("ハ", "パ");
        array.put("ヒ", "ピ");
        array.put("フ", "プ");
        array.put("ヘ", "ペ");
        array.put("ホ", "ポ");
        return array;
    }

    /**
     * 濁点のひらがな/カタカナから通常文字への変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createDakutenToClearList() {
        RudeArray array = new RudeArray();
        array.put("が", "か");
        array.put("ぎ", "き");
        array.put("ぐ", "く");
        array.put("げ", "け");
        array.put("ご", "こ");
        array.put("ざ", "さ");
        array.put("じ", "し");
        array.put("ず", "す");
        array.put("ぜ", "せ");
        array.put("ぞ", "そ");
        array.put("だ", "た");
        array.put("ぢ", "ち");
        array.put("づ", "つ");
        array.put("で", "て");
        array.put("ど", "と");
        array.put("ば", "は");
        array.put("び", "ひ");
        array.put("ぶ", "ふ");
        array.put("べ", "へ");
        array.put("ぼ", "ほ");
        array.put("ガ", "カ");
        array.put("ギ", "キ");
        array.put("グ", "ク");
        array.put("ゲ", "ケ");
        array.put("ゴ", "コ");
        array.put("ザ", "サ");
        array.put("ジ", "シ");
        array.put("ズ", "ス");
        array.put("ゼ", "セ");
        array.put("ゾ", "ソ");
        array.put("ダ", "タ");
        array.put("ヂ", "チ");
        array.put("ヅ", "ツ");
        array.put("デ", "テ");
        array.put("ド", "ト");
        array.put("バ", "ハ");
        array.put("ビ", "ヒ");
        array.put("ブ", "フ");
        array.put("ベ", "ヘ");
        array.put("ボ", "ホ");
        return array;
    }

    /**
     * 半濁点のひらがな/カタカナから通常文字への変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createDakutenHalfToClearList() {
        RudeArray array = new RudeArray();
        array.put("ぱ", "は");
        array.put("ぴ", "ひ");
        array.put("ぷ", "ふ");
        array.put("ぺ", "へ");
        array.put("ぽ", "ほ");
        array.put("パ", "ハ");
        array.put("ピ", "ヒ");
        array.put("プ", "フ");
        array.put("ペ", "ヘ");
        array.put("ポ", "ホ");
        return array;
    }

    /**
     * ひらがな/カタカナの小文字への変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createUpperToLowerJapaneseList() {
        RudeArray array = new RudeArray();
        array.put("あ", "ぁ");
        array.put("い", "ぃ");
        array.put("う", "ぅ");
        array.put("え", "ぇ");
        array.put("お", "ぉ");
        array.put("や", "ゃ");
        array.put("ゆ", "ゅ");
        array.put("よ", "ょ");
        array.put("つ", "っ");
        array.put("ア", "ァ");
        array.put("イ", "ィ");
        array.put("ウ", "ゥ");
        array.put("エ", "ェ");
        array.put("オ", "ォ");
        array.put("ヤ", "ャ");
        array.put("ユ", "ュ");
        array.put("ヨ", "ョ");
        array.put("ツ", "ッ");
        return array;
    }

    /**
     * ひらがな/カタカナの小文字から通常文字への変換リスト（半角カタカナは非対応）
     */
    private static RudeArray createLowerToUpperJapaneseList() {
        RudeArray array = new RudeArray();
        array.put("ぁ", "あ");
        array.put("ぃ", "い");
        array.put("ぅ", "う");
        array.put("ぇ", "え");
        array.put("ぉ", "お");
        array.put("ゃ", "や");
        array.put("ゅ", "ゆ");
        array.put("ょ", "よ");
        array.put("っ", "つ");
        array.put("ァ", "ア");
        array.put("ィ", "イ");
        array.put("ゥ", "ウ");
        array.put("ェ", "エ");
        array.put("ォ", "オ");
        array.put("ャ", "ヤ");
        array.put("ュ", "ユ");
        array.put("ョ", "ヨ");
        array.put("ッ", "ツ");
        return array;
    }

}
