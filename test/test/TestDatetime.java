package test;

import com.hirohiro716.datetime.Datetime;
import com.hirohiro716.datetime.Datetime.Span;
import com.hirohiro716.datetime.SpanOfCutoffDates;

@SuppressWarnings({ "all"})
public class TestDatetime {

    public static void main(String[] args) {
        
        Datetime startLimitDate = new Datetime();
        Datetime endLimitDate = new Datetime();
        endLimitDate.addDay(60);
        SpanOfCutoffDates spans = new SpanOfCutoffDates(startLimitDate.getDate(), endLimitDate.getDate(), 5, 10, 15, 20, 25, 33);
        for (Span span: spans.getSpans()) {
            System.out.println(span.getStartDatetime().toDatetimeString() + "〜" + span.getEndDatetime().toDatetimeString());
        }
        
    }

}
