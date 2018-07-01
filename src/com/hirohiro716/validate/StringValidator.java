package com.hirohiro716.validate;

import static com.hirohiro716.StringConverter.join;

import java.util.Date;

import com.hirohiro716.StringConverter;
import com.hirohiro716.Datetime;
import com.hirohiro716.RudeArray;

/**
 * 値をチェックするクラス.
 * @author hiro
 */
public class StringValidator {

    /**
     * 一括変換に使用するパラメータ
     */
    public final RudeArray params = new RudeArray();

    /**
     * エラーメッセージの変動する箇所用置き換え文字列
     */
    private final static String ERROR_MESSAGE_ARGUMENT = "dHHTy-uN79";

    /**
     * チェックパターン
     * @author hiro
     */
    private enum Pattern {
        BLANK("が空欄です。"),
        INTEGER("に数字以外の文字列が含まれています。"),
        DECIMAL("に数字と少数点以外の文字列が含まれています。"),
        TELEPHONE_NUMBER("が不正です。"),
        LENGTH(join("は", ERROR_MESSAGE_ARGUMENT, "桁である必要があります。")),
        MAX_LENGTH(join("の文字数がオーバーしています。", ERROR_MESSAGE_ARGUMENT, "文字まで入力できます。")),
        MIN_LENGTH(join("の文字数が足りません。", ERROR_MESSAGE_ARGUMENT, "文字必要です。")),
        ZERO("にゼロは入力できません。"),
        MAX_VALUE(join("は最大で「", ERROR_MESSAGE_ARGUMENT, "」まで入力できます。")),
        MIN_VALUE(join("は「", ERROR_MESSAGE_ARGUMENT, "」以上である必要があります。")),
        DATETIME("が不正です。"),
        REGEX("が不正です。"),
        REGEX_REVERSE("が不正です。"),
        ;

        private Pattern(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        private String errorMessage;

        public String getErrorMessage() {
            return this.errorMessage;
        }

    }

    /**
     * コンストラクタ
     */
    public StringValidator() {
    }

    /**
     * コンストラクタでエラーメッセージに使われるチェック対象の名前を指定する.
     * @param targetName
     */
    public StringValidator(String targetName) {
        this.targetName = targetName;
    }

    private String targetName;

    /**
     * エラーメッセージに使用されるチェック対象の名前を指定する.
     * @param targetName
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * 文字列の空欄チェックを予約する.
     */
    public void addBlankCheck() {
        this.params.put(Pattern.BLANK, null);
    }

    /**
     * 文字列の数値有効性チェックを予約する.
     */
    public void addIntegerCheck() {
        this.params.put(Pattern.INTEGER, null);
    }

    /**
     * 文字列の少数値有効性チェックを予約する.
     */
    public void addDecimalCheck() {
        this.params.put(Pattern.DECIMAL, null);
    }

    /**
     * 文字列の電話番号有効性チェックを予約する.
     */
    public void addTelephoneNumberCheck() {
        this.params.put(Pattern.TELEPHONE_NUMBER, null);
    }

    /**
     * 文字列の文字数チェックを予約する.
     * @param length 決定文字数
     */
    public void addLengthCheck(int length) {
        this.params.put(Pattern.LENGTH, length);
    }

    /**
     * 文字列の最大文字数チェックを予約する.
     * @param length 最大文字数
     */
    public void addMaxLengthCheck(int length) {
        this.params.put(Pattern.MAX_LENGTH, length);
    }

    /**
     * 文字列の最小文字数チェックを予約する.
     * @param length 最小文字数
     */
    public void addMinLengthCheck(int length) {
        this.params.put(Pattern.MIN_LENGTH, length);
    }

    /**
     * 数値のゼロチェックを予約する.
     */
    public void addZeroCheck() {
        this.params.put(Pattern.ZERO, null);
    }

    /**
     * 数値の最大値チェックを予約する.
     * @param maxValue 最大値
     */
    public void addMaxValueCheck(double maxValue) {
        this.params.put(Pattern.MAX_VALUE, maxValue);
    }

    /**
     * 数値の最小値チェックを予約する.
     * @param minValue 最小値
     */
    public void addMinValueCheck(double minValue) {
        this.params.put(Pattern.MIN_VALUE, minValue);
    }

    /**
     * 日付形式チェックを予約する. java.util.Dateか日付として有効な文字列かをチェックする.
     */
    public void addDatetimeCheck() {
        this.params.put(Pattern.DATETIME, null);
    }

    /**
     * 正規表現パターンチェックを予約する.
     * @param regex 正規表現
     */
    public void addRegexCheck(java.util.regex.Pattern regex) {
        this.params.put(Pattern.REGEX, regex);
    }

    /**
     * 正規表現に一致する場合にエラーとするチェックを予約する.
     * @param regex 正規表現
     */
    public void addRegexReverseCheck(java.util.regex.Pattern regex) {
        this.params.put(Pattern.REGEX_REVERSE, regex);
    }

    /**
     * セットしたチェックパターンをすべて解除する.
     */
    public void clear() {
        this.params.clear();
    }

    private String buildErrorMessage(Pattern pattern) {
        if (this.params.size() == 0) {
            return "";
        }
        String parameter = StringConverter.nullReplace(this.params.get(pattern), "");
        return pattern.getErrorMessage().replace(ERROR_MESSAGE_ARGUMENT, parameter);
    }

    /**
     * 値のチェックを実行します.
     * @param target チェック対象
     * @throws ValidationException
     */
    public void execute(Object target) throws ValidationException {
        String stringValue = StringConverter.nullReplace(target, "");
        for (Object patternObject: this.params.getKeys()) {
            Pattern pattern = (Pattern) patternObject;
            switch (pattern) {
            case BLANK:
                if (isBlank(stringValue)) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case INTEGER:
                if (isInteger(stringValue) == false) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case DECIMAL:
                if (isDecimal(stringValue) == false) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case TELEPHONE_NUMBER:
                if (isTelephoneNumber(stringValue) == false) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case LENGTH:
                if (stringValue.length() != 0 && stringValue.length() != this.params.getInteger(pattern).intValue() && this.params.getInteger(pattern).intValue() > -1) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case MAX_LENGTH:
                if (stringValue.length() > this.params.getInteger(pattern).intValue() && this.params.getInteger(pattern).intValue() > -1) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case MIN_LENGTH:
                if (stringValue.length() != 0 && stringValue.length() < this.params.getInteger(pattern).intValue() && this.params.getInteger(pattern).intValue() > -1) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case ZERO:
                if (StringConverter.stringToDouble(stringValue) != null && StringConverter.stringToDouble(stringValue) == 0) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case MAX_VALUE:
                if (StringConverter.stringToDouble(stringValue) != null && StringConverter.stringToDouble(stringValue) > this.params.getDouble(pattern).doubleValue()) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case MIN_VALUE:
                if (StringConverter.stringToDouble(stringValue) != null && StringConverter.stringToDouble(stringValue) < this.params.getDouble(pattern).doubleValue()) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case DATETIME:
                if (isDatetime(target) == false) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case REGEX:
                java.util.regex.Pattern regexPattern = (java.util.regex.Pattern) this.params.get(pattern);
                if (stringValue.length() > 0 && regexPattern.matcher(stringValue).find() == false) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            case REGEX_REVERSE:
                java.util.regex.Pattern regexPatternReverse = (java.util.regex.Pattern) this.params.get(pattern);
                if (stringValue.length() > 0 && regexPatternReverse.matcher(stringValue).find()) {
                    throw new ValidationException(this.targetName + this.buildErrorMessage(pattern));
                }
                break;
            }
        }
    }

    /**
     * 空欄かどうかをチェックする.
     * @param value チェック対象
     * @return 結果
     */
    public static boolean isBlank(Object value) {
        if (value == null) {
            return true;
        }
        if (value.toString().length() == 0) {
            return true;
        }
        return false;
     }

    /**
     * 数値かどうかをチェックする.
     * @param value チェック対象
     * @return 結果
     */
    public static boolean isInteger(Object value) {
        if (value != null) {
            if (value.toString().matches("^[0-9]{0,}$") == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 少数値かどうかをチェックする.
     * @param value チェック対象
     * @return 結果
     */
    public static boolean isDecimal(Object value) {
        if (value != null) {
            if (value.toString().matches("^[0-9\\.]{0,}$") == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 電話番号かどうかをチェックする.
     * @param value チェック対象
     * @return 結果
     */
    public static boolean isTelephoneNumber(Object value) {
        if (value != null && value.toString().length() > 0) {
            if (value.toString().matches("^[0-9]{1,}-[0-9]{1,}-[0-9]{1,}$") == false) {
                return false;
            }
            if (value.toString().matches("^[0-9\\-]{10,}$") == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 日時として有効かどうか
     * @param value チェック対象
     * @return 結果
     */
    public static boolean isDatetime(Object value) {
        if (value != null && value.toString().length() > 0) {
            String testValue = value.toString();
            if (value instanceof Date) {
                testValue = Datetime.dateToString((Date) value);
            }
            // yyyy/MM/dd、yyyy-MM-dd
            if (testValue.toString().matches("^[0-9]{4}[/\\-][0-1]{1}[1-9]{1}[/\\-][0-3]{1}[0-9]{1}$")) {
                return true;
            }
            // yyyy/MM/dd HH:mm、yyyy-MM-dd HH:mm
            if (testValue.toString().matches("^[0-9]{4}[/\\-][0-1]{1}[1-9]{1}[/\\-][0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
                return true;
            }
            // yyyy/MM/dd HH:mm:ss、yyyy-MM-dd HH:mm:ss
            if (testValue.toString().matches("^[0-9]{4}[/\\-][0-1]{1}[1-9]{1}[/\\-][0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
                return true;
            }
            // HH:mm:ss
            if (testValue.toString().matches("^[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
                return true;
            }
            // HH:mm
            if (testValue.toString().matches("^[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
                return true;
            }
        }
        return true;
    }

}
