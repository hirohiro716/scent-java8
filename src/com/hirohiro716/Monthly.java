package com.hirohiro716;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 月度のクラス.
 * @author hiro
 */
public class Monthly {
    
    /**
     * 月度の決定基準.
     * @author hiro
     */
    @SuppressWarnings("javadoc")
    public enum BaseDay {
        MONTHLY_DAYS_START(1, "月度の初日を基準とする"),
        MONTHLY_DAYS_END(2, "月度の末日を基準とする"),
        ;
        
        /**
         * コンストラクタ.
         * @param value 列挙体を表す数値
         * @param description 説明
         */
        private BaseDay(int value, String description) {
            this.value = value;
            this.description = description;
        }
        
        private int value;
        
        /**
         * 列挙体を表す数値を取得する.
         * @return 列挙体を表す数値
         */
        public int getValue() {
            return this.value;
        }
        
        private String description;
        
        /**
         * 列挙体の説明を取得する.
         * @return 説明
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * すべての列挙体で値と説明の連想配列を作成する.
         * @return 連想配列
         */
        public static LinkedHashMap<Integer, String> createLinkedHashMap() {
            LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
            for (BaseDay type: BaseDay.values()) {
                hashMap.put(type.getValue(), type.getDescription());
            }
            return hashMap;
        }
        
        /**
         * 列挙体を表す数値から列挙体を取得する.
         * @param value 列挙体を表す数値
         * @return 見つかった列挙体 またはnull
         */
        public static BaseDay find(Integer value) {
            for (BaseDay type: BaseDay.values()) {
                if (value != null && type.getValue() == value) {
                    return type;
                }
            }
            return null;
        }

    }

    /**
     * コンストラクタ.
     * @param baseDay 月度の決定基準
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     * @param year 年
     * @param month 月度
     * @throws SQLException 
     */
    public Monthly(BaseDay baseDay, int cutoffDay, int year, int month) {
        this.applyMonth(baseDay, cutoffDay, year, month);
    }
    
    /**
     * コンストラクタ.
     * @param baseDay 月度の決定基準
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     * @param date 対象日
     * @throws SQLException
     */
    public Monthly(BaseDay baseDay, int cutoffDay, Date date) {
        this.applyDate(baseDay, cutoffDay, date);
    }

    private BaseDay baseDay;
    
    private int cutoffDay;
    
    /**
     * その月の開始日と終了日を計算して適用する.
     * @param baseDay 月度の決定基準
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     * @param year 年
     * @param month 月度
     */
    protected void applyMonth(BaseDay baseDay, int cutoffDay, int year, int month) {
        this.baseDay = baseDay;
        this.cutoffDay = cutoffDay;
        Date defaultDate = new Date(0);
        Datetime startDate = new Datetime(defaultDate);
        Datetime endDate = new Datetime(defaultDate);
        if (this.cutoffDay > 28) {
            startDate.modifyYear(year);
            startDate.modifyMonth(month);
            startDate.modifyDay(1);
            endDate.setDate(startDate.getDate());
            endDate.addDay(-1);
        } else {
            switch (this.baseDay) {
            case MONTHLY_DAYS_START:
                startDate.modifyYear(year);
                startDate.modifyMonth(month);
                startDate.modifyDay(this.cutoffDay);
                startDate.addDay(1);
                endDate.modifyYear(year);
                endDate.modifyMonth(month);
                endDate.modifyDay(this.cutoffDay);
                endDate.addMonth(1);
                break;
            case MONTHLY_DAYS_END:
                startDate.modifyYear(year);
                startDate.modifyMonth(month);
                startDate.modifyDay(this.cutoffDay);
                startDate.addMonth(-1);
                startDate.addDay(1);
                endDate.modifyYear(year);
                endDate.modifyMonth(month);
                endDate.modifyDay(this.cutoffDay);
                break;
            }
        }
        this.year = year;
        this.month = month;
        this.startDatetime = startDate;
        this.endDatetime = endDate;
    }
    
    /**
     * Dateから月度を特定する.
     * @param baseDay 月度の決定基準
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     * @param date 対象日
     */
    protected void applyDate(BaseDay baseDay, int cutoffDay, Date date) {
        Datetime datetime = new Datetime(date);
        datetime.modifyHour(0);
        datetime.modifyMinute(0);
        datetime.modifySecond(0);
        Datetime temporaryDatetime = new Datetime(date);
        this.applyMonth(baseDay, cutoffDay, temporaryDatetime.toYear(), temporaryDatetime.toMonth());
        if (this.startDatetime.getDate().getTime() <= datetime.getDate().getTime() && this.endDatetime.getDate().getTime() >= datetime.getDate().getTime()) {
            return;
        }
        temporaryDatetime.addMonth(-1);
        this.applyMonth(baseDay, cutoffDay, temporaryDatetime.toYear(), temporaryDatetime.toMonth());
        if (this.startDatetime.getDate().getTime() <= datetime.getDate().getTime() && this.endDatetime.getDate().getTime() >= datetime.getDate().getTime()) {
            return;
        }
        temporaryDatetime.addMonth(2);
        this.applyMonth(baseDay, cutoffDay, temporaryDatetime.toYear(), temporaryDatetime.toMonth());
        if (this.startDatetime.getDate().getTime() <= datetime.getDate().getTime() && this.endDatetime.getDate().getTime() >= datetime.getDate().getTime()) {
            return;
        }
        throw new IllegalArgumentException("月度を特定することができませんでした。");
    }
    
    private int year;
    
    /**
     * 年を取得する.
     * @return 年
     */
    public int getYear() {
        return this.year;
    }
    
    private int month;
    
    /**
     * 月を取得する.
     * @return 月
     */
    public int getMonth() {
        return this.month;
    }
    
    private Datetime startDatetime;
    
    /**
     * 開始日を取得する.
     * @return 開始日
     */
    public Datetime getStartDatetime() {
        return this.startDatetime;
    }
    
    private Datetime endDatetime;
    
    /**
     * 終了日を取得する.
     * @return 終了日
     */
    public Datetime getEndDatetime() {
        return this.endDatetime;
    }
    
    
}
