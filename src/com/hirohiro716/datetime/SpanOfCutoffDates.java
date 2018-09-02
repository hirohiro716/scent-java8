package com.hirohiro716.datetime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hirohiro716.ArrayHelper;
import com.hirohiro716.datetime.Datetime.Span;

/**
 * 締め日と締め日の間の期間のクラス.
 * @author hiro
 */
public class SpanOfCutoffDates {
    
    /**
     * コンストラクタ.
     * @param startLimitDate 求める期間を制限する下限
     * @param endLimitDate 求める期間を制限する上限
     * @param cutoffDates 締め日
     */
    public SpanOfCutoffDates(Date startLimitDate, Date endLimitDate, Integer... cutoffDates) {
        if (cutoffDates.length == 0) {
            throw new IllegalArgumentException();
        }
        this.startLimitDate = startLimitDate;
        this.endLimitDate = endLimitDate;
        for (Integer cutoffDate: cutoffDates) {
            this.addCutoffDate(cutoffDate);
        }
        this.sortCutoffDates();
        this.calculateSpans();
    }
    
    /**
     * コンストラクタ.
     * @param startLimitDate 求める期間を制限する下限
     * @param endLimitDate 求める期間を制限する上限
     * @param cutoffDates 締め日
     */
    public SpanOfCutoffDates(Date startLimitDate, Date endLimitDate, List<Integer> cutoffDates) {
        if (cutoffDates.size() == 0) {
            throw new IllegalArgumentException();
        }
        this.startLimitDate = startLimitDate;
        this.endLimitDate = endLimitDate;
        for (Integer cutoffDate: cutoffDates) {
            this.addCutoffDate(cutoffDate);
        }
        this.sortCutoffDates();
        this.calculateSpans();
    }
    
    private Date startLimitDate;
    
    /**
     * 求める期間を制限する下限を取得する.
     * @return Date 求める期間を制限するための下限日
     */
    public Date getStartLimitDate() {
        return this.startLimitDate;
    }
    
    private Date endLimitDate;
    
    /**
     * 求める期間を制限する上限を取得する.
     * @return Date 求める期間を制限するための上限日
     */
    public Date getEndLimitDate() {
        return this.endLimitDate;
    }
    
    private ArrayList<Integer> cutoffDates = new ArrayList<>();
    
    /**
     * 締め日を追加する.
     * @param cutoffDate
     */
    private void addCutoffDate(Integer cutoffDate) {
        if (cutoffDate != null) {
            if (cutoffDate >= 1 && cutoffDate <= 28) {
                this.cutoffDates.add(cutoffDate);
            }
            if (cutoffDate > 28) {
                this.cutoffDates.add(31);
            }
        }
    }
    
    /**
     * 締め日を昇順に並び替える.
     */
    private void sortCutoffDates() {
        ArrayList<Integer> newCutoffDates = new ArrayList<>();
        Integer[] cutoffDates = this.getCutoffDates();
        ArrayHelper.sort(cutoffDates);
        for (Integer cutoffDate: cutoffDates) {
            newCutoffDates.add(cutoffDate);
        }
        this.cutoffDates = newCutoffDates;
    }
    
    /**
     * コンストラクタで指定した締め日を取得する.
     * @return 締め日の配列（1〜28）28を超える場合は末締め扱い
     */
    public Integer[] getCutoffDates() {
        return this.cutoffDates.toArray(new Integer[] {});
    }
    
    private ArrayList<Span> spans;

    /**
     * 締め日で分割した期間の配列を取得する.
     */
    private void calculateSpans() {
        this.spans = new ArrayList<>();
        Datetime increment = new Datetime(this.startLimitDate);
        while (increment.getDate().getTime() <= this.endLimitDate.getTime()) {
            
            Datetime startDatetime = new Datetime(increment.getDate());
            startDatetime.modifyHour(0);
            startDatetime.modifyMinute(0);
            startDatetime.modifySecond(0);
            int nearCutoffDate = this.calculateNearCutoffDate(startDatetime);
            while (increment.toDay() != nearCutoffDate) {
                increment.addDay(1);
            }
            Datetime endDatetime = new Datetime(increment.getDate());
            if (increment.getDate().getTime() > this.endLimitDate.getTime()) {
                endDatetime = new Datetime(this.endLimitDate);
            }
            endDatetime.modifyHour(23);
            endDatetime.modifyMinute(59);
            endDatetime.modifySecond(59);
            this.spans.add(new Span(startDatetime, endDatetime));
            increment.addDay(1);
        }
    }
    
    /**
     * 対象日に最も近い締め日を取得する.
     * @param datetime
     * @return 締め日
     */
    private int calculateNearCutoffDate(Datetime datetime) {
        Datetime compare = new Datetime(datetime.getDate());
        for (Integer cutoffDate: this.cutoffDates) {
            if (compare.toDay() < cutoffDate) {
                return cutoffDate;
            }
        }
        return this.cutoffDates.get(0);
    }
    
    /**
     * 締め日で分割した期間の配列を取得する.
     * @return 締め日で分割した期間の配列
     */
    public Span[] getSpans() {
        return this.spans.toArray(new Span[] {});
    }
    
}
