package com.hirohiro716;

import java.util.regex.Pattern;

/**
 * 正規表現に関するメソッド
 * @author hiro
 */
public class RegexHelper {

    /**
     * 正規表現のパターン
     * @author hiro
     */
    public enum RegexPattern {

        /**
         * 半角数値のみ
         */
        INTEGER_NARROW_ONLY("^[0-9]{0,}$"),
        /**
         * 全角数値のみ
         */
        INTEGER_WIDE_ONLY("^[０-９]{0,}$"),
        /**
         * 小数のみ
         */
        DECIMAL("^[0-9\\.]{0,}$"),
        /**
         * 正負小数のみ
         */
        DECIMAL_NEGATIVE("^[0-9\\.\\-]{0,}$"),
        /**
         * 電話番号のみ(半角数字及びハイフン)
         */
        TEL_NUMBER_ONLY("^[0-9\\-]{0,}$"),
        /**
         * 日時のみ(半角数字、ハイフン及びスラッシュ及びコロン)
         */
        DATETIME_ONLY("^[0-9\\-\\/: ]{0,}$"),
        /**
         * 日付のみ(半角数字、ハイフン及びスラッシュ)
         */
        DATE_ONLY("^[0-9\\-\\/]{0,}$"),
        /**
         * 時刻のみ(半角数字及びコロン)
         */
        TIME_ONLY("^[0-9:]{0,}$"),
        /**
         * アルファベットのみ
         */
        ALPHABET_ONLY("^[a-zA-Zａ-ｚＡ-Ｚ]{0,}$"),
        /**
         * アルファベット半角のみ
         */
        ALPHABET_NARROW_ONLY("^[a-zA-Z]{0,}$"),
        /**
         * アルファベット全角のみ
         */
        ALPHABET_WIDE_ONLY("^[ａ-ｚＡ-Ｚ]{0,}$"),
        /**
         * アルファベット半角小文字のみ
         */
        ALPHABET_NARROW_LOWER_ONLY("^[a-z]{0,}$"),
        /**
         * アルファベット半角大文字のみ
         */
        ALPHABET_NARROW_UPPER_ONLY("^[A-Z]{0,}$"),
        /**
         * アルファベット全角小文字のみ
         */
        ALPHABET_WIDE_LOWER_ONLY("^[ａ-ｚ]{0,}$"),
        /**
         * アルファベット全角大文字のみ
         */
        ALPHABET_WIDE_UPPER_ONLY("^[Ａ-Ｚ]{0,}$"),
        /**
         * 半角カタカナのみ
         */
        KATAKANA_NARROW_ONLY("^[ｦ-ﾟ]{0,}$"),
        /**
         * 全角カタカナのみ
         */
        KATAKANA_WIDE_ONLY("^[ァ-ヴー]{0,}$"),
        /**
         * ひらがなのみ
         */
        HIRAGANA_ONLY("^[ぁ-んー]{0,}$"),
        /**
         * 改行のみ
         */
        LINE_SEPARATOR("^(\\r\\n|\\r|\\n){0,}$"),
        /**
         * タブ文字のみ
         */
        TAB("^\\t{0,}$"),
        /**
         * スペース文字のみ
         */
        SPACE("^(　| ){0,}$"),
        /**
         * 半角文字のみ
         */
        HALF("^[\\x01-\\x7E]{0,}$"),
        /**
         * 全角文字のみ
         */
        WIDE("^[^\\x01-\\x7E]{0,}$"),
        ;

        private Pattern pattern;

        private RegexPattern(String compileText) {
            this.pattern = Pattern.compile(compileText);
        }

        /**
         * 正規表現パターン
         * @return Pattern
         */
        public Pattern getPattern() {
            return this.pattern;
        }
    }

    /**
     * 文字列が正規表現に一致するかを取得する。
     *
     * @param value 値
     * @param compileText 正規表現
     * @return 結果
     */
    public static boolean isMatch(String value, String compileText) {
        Pattern regexPattern = Pattern.compile(compileText);
        if (regexPattern.matcher(value).matches() == false) {
            return false;
        }
        return true;
    }

    /**
     * 正規表現によって全角半角大小英数/ひらがな/全角カタカナを区別しない比較用文字列に変換する。
     *
     * @param value 検索値
     * @return 正規表現による比較用文字列
     */
    public static String makeBroadCompareValue(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            String one = value.substring(i, i + 1);
            convert: {
                if (RegexPattern.HIRAGANA_ONLY.pattern.matcher(one).matches()) {
                    result.append("(");
                    result.append(one);
                    result.append("|");
                    result.append(StringConverter.hiraganaToKatakana(one));
                    result.append(")");
                    break convert;
                }
                if (RegexPattern.KATAKANA_WIDE_ONLY.pattern.matcher(one).matches()) {
                    result.append("(");
                    result.append(one);
                    result.append("|");
                    result.append(StringConverter.katakanaToHiragana(one));
                    result.append(")");
                    break convert;
                }
                if (RegexPattern.INTEGER_NARROW_ONLY.pattern.matcher(one).matches()) {
                    result.append("(");
                    result.append(one);
                    result.append("|");
                    result.append(StringConverter.narrowToWide(one));
                    result.append(")");
                    break convert;
                }
                if (RegexPattern.INTEGER_WIDE_ONLY.pattern.matcher(one).matches()) {
                    result.append("(");
                    result.append(one);
                    result.append("|");
                    result.append(StringConverter.wideToNarrow(one));
                    result.append(")");
                    break convert;
                }
                if (RegexPattern.ALPHABET_ONLY.pattern.matcher(one).matches()) {
                    // 半角小文字に変換
                    String alphabet = StringConverter.wideToNarrow(one);
                    alphabet = alphabet.toLowerCase();
                    // 4パターン追加
                    result.append("(");
                    result.append(alphabet);
                    result.append("|");
                    result.append(alphabet.toUpperCase());
                    result.append("|");
                    result.append(StringConverter.narrowToWide(alphabet));
                    result.append("|");
                    result.append(StringConverter.narrowToWide(alphabet.toUpperCase()));
                    result.append(")");
                    break convert;
                }
                result.append(one);
            }
        }
        return result.toString();
    }
}
