package com.hirohiro716;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 日付関連を取り扱うクラス.
 * @author hiro
 */
public class Datetime {
    
    private final static String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static String FORMAT_PATTERN_DATE = "yyyy-MM-dd";
    private final static String FORMAT_PATTERN_TIME = "HH:mm:ss";

    private String formatPattern = FORMAT_PATTERN;
    private Calendar calendar = (Calendar) Calendar.getInstance().clone();

    /**
     * 現在の日時を指定してインスタンス生成する.
     */
    public Datetime() {
    }

    /**
     * 日時を指定してインスタンス生成する.
     * @param date 日付
     */
    public Datetime(Date date) {
        this.calendar.setTime(date);
    }

    /**
     * 日時文字列を指定してインスタンス生成する.
     * @param datetimeString 日時文字列（yyyy-MM-dd HH:mm:ss）
     */
    public Datetime(String datetimeString) {
        this.setDatetime(datetimeString);
    }

    /**
     * 日時文字列とformatパターンを指定してインスタンス生成する.
     * @param datetimeString 日時文字列
     * @param formatPattern formatパターン
     */
    public Datetime(String datetimeString, String formatPattern) {
        this.setDatetime(datetimeString, formatPattern);
    }

    /**
     * 日時とformatパターンを指定してインスタンス生成する.
     * @param date 日時
     * @param formatPattern formatパターン
     */
    public Datetime(Date date, String formatPattern) {
        this.formatPattern = formatPattern;
        this.calendar.setTime(date);
    }

    /**
     * Date型の値を取得する.
     * @return 日時
     */
    public Date getDate() {
        return this.calendar.getTime();
    }

    /**
     * 日時文字列を取得する.
     * @return 日時文字列（初期パターンはyyyy-MM-dd HH:mm:ss）
     */
    public String toDatetimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.formatPattern);
        return dateFormat.format(this.calendar.getTime());
    }

    /**
     * 日時文字列をformatパターンを指定して取得する.
     * @param formatPattern formatパターン
     * @return 日時文字列
     */
    public String toDatetimeString(String formatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
        return dateFormat.format(this.calendar.getTime());
    }

    /**
     * 日付文字列を取得する.
     * @return 日付文字列（yyyy-MM-dd）
     */
    public String toDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_PATTERN_DATE);
        return dateFormat.format(this.calendar.getTime());
    }

    /**
     * 時刻文字列を取得する.
     * @return 時刻文字列（HH:mm:ss）
     */
    public String toTimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_PATTERN_TIME);
        return dateFormat.format(this.calendar.getTime());
    }

    /**
     * 日付をセットする.
     * @param date 日時
     */
    public void setDate(Date date) {
        this.calendar.setTime(date);
    }

    /**
     * 日時文字列をセットする.
     * @param datetimeString 日時文字列
     */
    public void setDatetime(String datetimeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.formatPattern);
        try {
            this.calendar.setTime(dateFormat.parse(datetimeString));
        } catch (ParseException exception) {
            this.calendar.setTime(new Date(0));
        }
    }

    /**
     * 日時文字列とformatパターンをセットする.
     * @param datetimeString
     * @param formatPattern
     */
    public void setDatetime(String datetimeString, String formatPattern) {
        this.formatPattern = formatPattern;
        this.setDatetime(datetimeString);
    }

    /**
     * 年月日時分秒を一括セットする.
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param minute 分
     * @param second 秒
     */
    public void setDatetime(int year, int month, int day, int hour, int minute, int second) {
        this.calendar.set(year, month - 1, day, hour, minute, second);
    }

    /**
     * 年を取得する.
     * @return 年
     */
    public int toYear() {
        return this.calendar.get(Calendar.YEAR);
    }

    /**
     * 月を取得する.（1～12）
     * @return 月
     */
    public int toMonth() {
        return this.calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 日を取得する.（1～31）
     * @return 日
     */
    public int toDay() {
        return this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 時刻を取得する.（0～23）
     * @return 時刻
     */
    public int toHour() {
        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 分を取得する.（0～59）
     * @return 分
     */
    public int toMinute() {
        return this.calendar.get(Calendar.MINUTE);
    }

    /**
     * 秒を取得する.（0～59）
     * @return 秒
     */
    public int toSecond() {
        return this.calendar.get(Calendar.SECOND);
    }

    /**
     * 週を取得する.
     * @return 各Calendar定数（Calendar.SUNDAYなど）
     */
    public int toWeek() {
        return this.calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 年をセットする.
     * @param year 年
     */
    public void modifyYear(int year) {
        this.calendar.set(Calendar.YEAR, year);
    }

    /**
     * 月をセットする.（1月ならば１）
     * @param month 月
     */
    public void modifyMonth(int month) {
        this.calendar.set(Calendar.MONTH, month - 1);
    }

    /**
     * 日をセットする.
     * @param day 日
     */
    public void modifyDay(int day) {
        this.calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    /**
     * 時刻をセットする.
     * @param hour 時刻
     */
    public void modifyHour(int hour) {
        this.calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    /**
     * 分をセットする.
     * @param minute 分
     */
    public void modifyMinute(int minute) {
        this.calendar.set(Calendar.MINUTE, minute);
    }

    /**
     * 秒をセットする.
     * @param second 秒
     */
    public void modifySecond(int second) {
        this.calendar.set(Calendar.SECOND, second);
    }

    /**
     * 年数を加算する.
     * @param add 加算年数
     */
    public void addYear(int add) {
        this.calendar.add(Calendar.YEAR, add);
    }

    /**
     * 月数を加算する.
     * @param add 加算月数
     */
    public void addMonth(int add) {
        this.calendar.add(Calendar.MONTH, add);
    }

    /**
     * 日数を加算する.
     * @param add 加算日数
     */
    public void addDay(int add) {
        this.calendar.add(Calendar.DAY_OF_MONTH, add);
    }

    /**
     * 時数を加算する.
     * @param add 加算時数
     */
    public void addHour(int add) {
        this.calendar.add(Calendar.HOUR, add);
    }

    /**
     * 分数を加算する.
     * @param add 加算分数
     */
    public void addMinute(int add) {
        this.calendar.add(Calendar.MINUTE, add);
    }

    /**
     * 秒数を加算する.
     * @param add 加算秒数
     */
    public void addSecond(int add) {
        this.calendar.add(Calendar.SECOND, add);
    }

    @Override
    public String toString() {
        return this.calendar.getTime().toString();
    }

    private static LinkedHashMap<Integer, String> weeksHashMap = new LinkedHashMap<>();
    
    /**
     * 曜日定数と日本語曜日名の連想配列を取得する.
     * @return 曜日の連想配列
     */
    public static LinkedHashMap<Integer, String> getWeeksHashMap() {
        if (weeksHashMap.size() == 0) {
            weeksHashMap.put(Calendar.SUNDAY, "日曜日");
            weeksHashMap.put(Calendar.MONDAY, "月曜日");
            weeksHashMap.put(Calendar.TUESDAY, "火曜日");
            weeksHashMap.put(Calendar.WEDNESDAY, "水曜日");
            weeksHashMap.put(Calendar.THURSDAY, "木曜日");
            weeksHashMap.put(Calendar.FRIDAY, "金曜日");
            weeksHashMap.put(Calendar.SATURDAY, "土曜日");
        }
        return weeksHashMap;
    }

    /**
     * Date型を文字列に変換する.
     * @param date 日付
     * @return 日付文字列（yyyy-MM-dd HH:mm:ss）
     */
    public static String dateToString(Date date) {
        try {
            Datetime datetime = new Datetime(date);
            return datetime.toDatetimeString();
        } catch (NullPointerException exception) {
            return null;
        }
    }

    /**
     * Date型を文字列に変換する.
     * @param date 日付
     * @param pattern 変換パターン
     * @return 日付文字列
     */
    public static String dateToString(Date date, String pattern) {
        try {
            Datetime datetime = new Datetime(date, pattern);
            return datetime.toDatetimeString();
        } catch (NullPointerException exception) {
            return null;
        }
    }

    /**
     * 日付文字列をDate型に変換する.
     * @param dateString 日付文字列
     * @return 日付
     */
    public static Date stringToDate(String dateString) {
        // yyyy/MM/dd
        if (dateString.matches("^[0-9]{4}/[0-1]{1}[1-9]{1}/[0-3]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "yyyy/MM/dd").getDate();
        }
        // yyyy-MM-dd
        if (dateString.matches("^[0-9]{4}\\-[0-1]{1}[1-9]{1}\\-[0-3]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "yyyy-MM-dd").getDate();
        }
        // yyyy/MM/dd HH:mm
        if (dateString.toString().matches("^[0-9]{4}/[0-1]{1}[1-9]{1}/[0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "yyyy/MM/dd HH:mm").getDate();
        }
        // yyyy-MM-dd HH:mm
        if (dateString.toString().matches("^[0-9]{4}\\-[0-1]{1}[1-9]{1}\\-[0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "yyyy-MM-dd HH:mm").getDate();
        }
        // yyyy/MM/dd HH:mm:ss
        if (dateString.toString().matches("^[0-9]{4}/[0-1]{1}[1-9]{1}/[0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "yyyy/MM/dd HH:mm:ss").getDate();
        }
        // yyyy-MM-dd HH:mm:ss
        if (dateString.toString().matches("^[0-9]{4}\\-[0-1]{1}[1-9]{1}\\-[0-3]{1}[0-9]{1} [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "yyyy-MM-dd HH:mm:ss").getDate();
        }
        // HH:mm:ss
        if (dateString.toString().matches("^[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "HH:mm:ss").getDate();
        }
        // HH:mm
        if (dateString.toString().matches("^[0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$")) {
            return new Datetime(dateString, "HH:mm").getDate();
        }
        return null;
    }

}
