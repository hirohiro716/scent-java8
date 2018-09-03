package com.hirohiro716.datetime;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 会計月のクラス.
 * @author hiro
 */
public class FiscalMonth {
    
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
    
    private BaseDay baseDay = BaseDay.MONTHLY_DAYS_END;
    
    /**
     * 月度の決定基準定数を取得する.
     * @return BaseDay
     */
    public BaseDay getBaseDay() {
        return this.baseDay;
    }
    
    /**
     * 月度の決定基準定数をセットする.
     * @param baseDay
     */
    public void setBaseDay(BaseDay baseDay) {
        this.baseDay = baseDay;
    }
    
    private int cutoffDay = 31;
    
    /**
     * 締め日を取得する.
     * @return 締め日（1〜28）28を超える場合は末締め扱い
     */
    public int getCutoffDay() {
        return this.cutoffDay;
    }
    
    /**
     * 締め日をセットする.
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     */
    public void setCutoffDay(int cutoffDay) {
        this.cutoffDay = cutoffDay;
    }
    
    private int year = 1970;
    
    /**
     * 年を取得する.
     * @return 年
     */
    public int getYear() {
        return this.year;
    }
    
    /**
     * 年をセットする.
     * @param year 西暦
     */
    public void setYear(int year) {
        this.year = year;
    }
    
    private int month = 1;
    
    /**
     * 月を取得する.
     * @return 月
     */
    public int getMonth() {
        return this.month;
    }
    
    /**
     * 月をセットする.
     * @param month 1〜12
     */
    public void setMonth(int month) {
        this.month = month;
    }
    
    private Datetime startDatetime = null;
    
    /**
     * 開始日を取得する.
     * @return 開始日
     */
    public Datetime getStartDatetime() {
        if (this.startDatetime == null) {
            this.caclulateStartAndEndDate();
        }
        return this.startDatetime;
    }
    
    private Datetime endDatetime;
    
    /**
     * 終了日を取得する.
     * @return 終了日
     */
    public Datetime getEndDatetime() {
        if (this.endDatetime == null) {
            this.caclulateStartAndEndDate();
        }
        return this.endDatetime;
    }

    /**
     * その月の開始日と終了日を計算して適用する.
     */
    private void caclulateStartAndEndDate() {
        Datetime defaultDate = new Datetime("1971-01-01 00:00:00");
        Datetime startDate = new Datetime(defaultDate.getDate());
        Datetime endDate = new Datetime(defaultDate.getDate());
        if (this.cutoffDay > 28) {
            startDate.modifyYear(this.year);
            startDate.modifyMonth(this.month);
            startDate.modifyDay(1);
            endDate.setDate(startDate.getDate());
            endDate.addDay(-1);
        } else {
            switch (this.baseDay) {
            case MONTHLY_DAYS_START:
                startDate.modifyYear(this.year);
                startDate.modifyMonth(this.month);
                startDate.modifyDay(this.cutoffDay);
                startDate.addDay(1);
                endDate.modifyYear(this.year);
                endDate.modifyMonth(this.month);
                endDate.modifyDay(this.cutoffDay);
                endDate.addMonth(1);
                break;
            case MONTHLY_DAYS_END:
                startDate.modifyYear(this.year);
                startDate.modifyMonth(this.month);
                startDate.modifyDay(this.cutoffDay);
                startDate.addMonth(-1);
                startDate.addDay(1);
                endDate.modifyYear(this.year);
                endDate.modifyMonth(this.month);
                endDate.modifyDay(this.cutoffDay);
                break;
            }
        }
        this.startDatetime = startDate;
        this.endDatetime = endDate;
    }
    
    /**
     * 月度インスタンスを生成する.
     * @param baseDay 月度の決定基準
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     * @param year 年
     * @param month 月度
     * @return Monthly
     */
    public static FiscalMonth create(BaseDay baseDay, int cutoffDay, int year, int month) {
        FiscalMonth monthly = new FiscalMonth();
        monthly.setBaseDay(baseDay);
        monthly.setCutoffDay(cutoffDay);
        monthly.setYear(year);
        monthly.setMonth(month);
        return monthly;
    }
    
    /**
     * Dateから月度を特定する.
     * @param baseDay 月度の決定基準
     * @param cutoffDay 締め日（1〜28）28を超える場合は末締め扱い
     * @param date 対象日
     * @return Monthly
     */
    public static FiscalMonth findMonthlyFromOneDay(BaseDay baseDay, int cutoffDay, Date date) {
        Datetime datetime = new Datetime(date);
        datetime.modifyHour(0);
        datetime.modifyMinute(0);
        datetime.modifySecond(0);
        Datetime temporaryDatetime = new Datetime(datetime.getDate());
        FiscalMonth monthly = create(baseDay, cutoffDay, temporaryDatetime.toYear(), temporaryDatetime.toMonth());
        if (monthly.getStartDatetime().getDate().getTime() <= datetime.getDate().getTime() && monthly.getEndDatetime().getDate().getTime() >= datetime.getDate().getTime()) {
            return monthly;
        }
        temporaryDatetime.addMonth(-1);
        monthly = create(baseDay, cutoffDay, temporaryDatetime.toYear(), temporaryDatetime.toMonth());
        if (monthly.getStartDatetime().getDate().getTime() <= datetime.getDate().getTime() && monthly.getEndDatetime().getDate().getTime() >= datetime.getDate().getTime()) {
            return monthly;
        }
        temporaryDatetime.addMonth(2);
        monthly = create(baseDay, cutoffDay, temporaryDatetime.toYear(), temporaryDatetime.toMonth());
        if (monthly.getStartDatetime().getDate().getTime() <= datetime.getDate().getTime() && monthly.getEndDatetime().getDate().getTime() >= datetime.getDate().getTime()) {
            return monthly;
        }
        throw new IllegalArgumentException("月度を特定することができませんでした。");
    }

    /**
     * すべての月の連想配列を取得する.
     * @return LinkedHashMap
     */
    public static LinkedHashMap<Integer, String> createLinkedHashMap() {
        LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            hashMap.put(i, i + "月");
        }
        return hashMap;
    }
    
}
