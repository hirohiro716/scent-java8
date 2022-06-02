package com.hirohiro716.database;

import java.util.ArrayList;
import java.util.Date;

/**
 * Where文を作成するクラス。
 *
 * @author hiro
 */
public class WhereSet implements Cloneable {

    /**
     * 比較演算子
     * @author hiro
     */
    public enum Comparison {
        /**
         * 等しい
         */
        EQUAL("="),
        /**
         * 等しくない
         */
        NOT_EQUAL("!="),
        /**
         * 小なり
         */
        LESS("<"),
        /**
         * 小なりイコール
         */
        LESS_EQUAL("<="),
        /**
         * 大なり
         */
        GREATER(">"),
        /**
         * 大なりイコール
         */
        GREATER_EQUAL(">="),
        /**
         * IN
         */
        IN("IN"),
        /**
         * IS NULL
         */
        IS_NULL("IS NULL"),
        /**
         * LIKE ワイルドカード
         */
        LIKE("LIKE"),
        /**
         * BETWEEN 範囲指定
         */
        BETWEEN("BETWEEN"),
        /**
         * SIMILAR TO 正規表現によるパターンマッチ
         */
        SIMILARTO("SIMILAR TO"),
        /**
         * REGEXP 正規表現によるパターンマッチ
         */
        REGEXP("REGEXP"),
        ;

        private Comparison(String string) {
            this.string = string;
        }

        private String string;

        /**
         * データベース用比較演算子を取得する。
         *
         * @return 比較演算子
         */
        @Override
        public String toString() {
            return this.string;
        }

    }

    private ArrayList<Where> wheres = new ArrayList<>();
    
    /**
     * Where条件を追加する。
     *
     * @param where Where条件
     */
    public void add(Where where) {
    	this.wheres.add(where);
    }

    /**
     * Where条件を追加する。
     *
     * @param column カラム名
     * @param comparison 比較演算子
     * @param value 比較値
     */
    public void add(String column, Comparison comparison, Object value) {
        this.wheres.add(new Where(column, comparison, value));
    }

    /**
     * Where条件を追加する。
     *
     * @param column カラム名
     * @param comparison 比較演算子
     * @param isNot NOTかどうか
     * @param value 比較値
     */
    public void add(String column, Comparison comparison, boolean isNot, Object value) {
        Where where = new Where(column, comparison, value);
        where.setNot(isNot);
        this.wheres.add(where);
    }

    /**
     * Where条件を追加する。
     *
     * @param column カラム名
     * @param comparison 比較演算子
     * @param value 比較値
     */
    public void addWithNot(String column, Comparison comparison, Object value) {
        Where where = new Where(column, comparison, value);
        where.setNot(true);
        this.wheres.add(where);
    }

    /**
     * BETWEEN演算子でWhere条件を追加する。
     *
     * @param column カラム名
     * @param value1 比較値1
     * @param value2 比較値2
     */
    public void addBetween(String column, Object value1, Object value2) {
        this.wheres.add(new Where(column, Comparison.BETWEEN, value1, value2));
    }

    /**
     * BETWEEN演算子でWhere条件を追加する。
     *
     * @param column カラム名
     * @param isNot NOTかどうか
     * @param value1 比較値1
     * @param value2 比較値2
     */
    public void addBetween(String column, boolean isNot, Object value1, Object value2) {
        Where where = new Where(column, Comparison.BETWEEN, value1, value2);
        where.setNot(isNot);
        this.wheres.add(where);
    }

    /**
     * BETWEEN演算子でWhere条件を追加する。
     *
     * @param column カラム名
     * @param value1 比較値1
     * @param value2 比較値2
     */
    public void addBetweenWithNot(String column, Object value1, Object value2) {
        Where where = new Where(column, Comparison.BETWEEN, value1, value2);
        where.setNot(true);
        this.wheres.add(where);
    }

    /**
     * IN演算子でWhere条件を追加する。
     *
     * @param column カラム名
     * @param values 値
     */
    public void addIn(String column, Object... values) {
        this.wheres.add(new Where(column, Comparison.IN, values));
    }

    /**
     * IN演算子でWhere条件を追加する。
     *
     * @param column カラム名
     * @param values 値
     */
    public void addInWithNot(String column, Object... values) {
        Where where = new Where(column, Comparison.IN, values);
        where.setNot(true);
        this.wheres.add(where);
    }
    
    /**
     * IS NULL演算子でWhere条件を追加する。
     *
     * @param column カラム名
     */
    public void addIsNull(String column) {
        this.wheres.add(new Where(column, Comparison.IS_NULL));
    }
    
    /**
     * IS NULL演算子でWhere条件を追加する。
     *
     * @param column カラム名
     */
    public void addIsNullWithNot(String column) {
        Where where = new Where(column, Comparison.IS_NULL);
        where.setNot(true);
        this.wheres.add(where);
    }

    /**
     * 「カラム1 = ? AND カラム2 = ?」のようなパラメータWhere句を生成する。
     *
     * @return パラメータWhere句
     */
    public String buildParameterClause() {
        StringBuilder builder = new StringBuilder();
        for (Where where: this.wheres) {
            if (builder.length() > 0) {
                builder.append(" AND ");
            }
            builder.append(where.buildParameterClause());
        }
        return builder.toString();
    }

    /**
     * buildParameterClauseメソッドで作成したWhere句に対するパラメータを生成する。
     *
     * @return パラメータ
     */
    public Object[] buildParameters() {
        ArrayList<Object> parameters = new ArrayList<>();
        for (Where where: this.wheres) {
            switch (where.getComparison()) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
            case LIKE:
            case SIMILARTO:
            case REGEXP:
                parameters.add(where.getValue());
                break;
            case BETWEEN:
                parameters.add(where.getValue());
                parameters.add(where.getValue2());
                break;
            case IN:
                for (Object value: where.getValues()) {
                    parameters.add(value);
                }
                break;
            case IS_NULL:
                break;
            }
        }
        return parameters.toArray();
    }

    /**
     * 内部で保持しているWhereクラスリストを取得する。
     *
     * @return Whereクラスリスト
     */
    public ArrayList<Where> getWheres() {
        return this.wheres;
    }

    /**
     * 内部で保持しているWhereオブジェクトを検索する。
     *
     * @param column Whereオブジェクトに設定されているcolumn
     * @return 該当するWhereオブジェクト
     */
    public Where findWhereFromColumn(String column) {
        for (Where where: this.wheres) {
            if (where.getColumn().equals(column)) {
                return where;
            }
        }
        return null;
    }

    /**
     * 内部で保持しているWhereクラスリストを破棄する。
     *
     */
    public void clear() {
        this.wheres.clear();
    }

    /**
     * 内部で保持しているWhereの数を取得する。
     *
     * @return Where数
     */
    public int size() {
        return this.wheres.size();
    }

    @Override
    public WhereSet clone() {
        WhereSet clone = new WhereSet();
        ArrayList<Where> cloneWheres = new ArrayList<>();
        for (Where where: this.wheres) {
            cloneWheres.add((Where) where.clone());
        }
        clone.getWheres().clear();
        clone.getWheres().addAll(cloneWheres);
        return clone;
    }

    /**
     * Whereの１つの条件を表すクラス。
     *
     * @author hiro
     */
    public static class Where implements Cloneable {
    	
        /**
         * コンストラクタ。
         *
         * @param column カラム名
         * @param comparison 比較演算子
         * @param values 比較値
         */
        public Where(String column, Comparison comparison, Object... values) {
            this.column = column;
            this.comparison = comparison;
            this.values = new ArrayList<>();
            for (Object value: values) {
                this.values.add(value);
            }
        }
        
        /**
         * 「カラム = ?」のようなパラメータWhere句を生成する。
         *
         * @return パラメータWhere句
         */
        public String buildParameterClause() {
            StringBuilder builder = new StringBuilder();
            if (this.isNot) {
                builder.append("NOT ");
            }
            builder.append(this.column);
            builder.append(" ");
            switch (this.comparison) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
            case LIKE:
            case SIMILARTO:
            case REGEXP:
                builder.append(this.comparison.toString());
                builder.append(" ?");
                break;
            case BETWEEN:
                builder.append(this.comparison.toString());
                builder.append(" ? AND ?");
                break;
            case IN:
                builder.append(this.comparison.toString());
                builder.append(" (");
                for (int index = 0; index < this.values.size(); index++) {
                    if (index > 0) {
                        builder.append(", ");
                    }
                    builder.append("?");
                }
                builder.append(")");
                break;
            case IS_NULL:
                builder.append(this.comparison.toString());
                break;
            }
            return builder.toString();
        }

        private String column;

        /**
         * カラム名を取得する。
         *
         * @return column
         */
        public String getColumn() {
            return this.column;
        }

        /**
         * カラム名をセットする。
         *
         * @param column
         */
        public void setColumn(String column) {
            this.column = column;
        }

        private Comparison comparison;

        /**
         * 比較演算子を取得する。
         *
         * @return comparison
         */
        public Comparison getComparison() {
            return this.comparison;
        }

        /**
         * 比較演算子をセットする。
         *
         * @param comparison
         */
        public void setComparison(Comparison comparison) {
            this.comparison = comparison;
        }

        private ArrayList<Object> values;

        /**
         * 比較値を取得する。
         *
         * @return value
         */
        public Object getValue() {
            try {
                return this.values.get(0);
            } catch (Exception exception) {
                return null;
            }
        }

        /**
         * 比較値その２を取得する。
         *
         * @return value
         */
        public Object getValue2() {
            try {
                return this.values.get(1);
            } catch (Exception exception) {
                return null;
            }
        }

        /**
         * 比較値をセットする。
         *
         * @param value
         */
        public void setValue(Object value) {
            this.values = new ArrayList<>();
            this.values.add(value);
        }

        /**
         * 比較値を全て取得する.(BETWEENやINの場合のみ)
         * @return values
         */
        public ArrayList<Object> getValues() {
            return this.values;
        }

        /**
         * 比較値を全てセットする.(BETWEENやINの場合のみ)
         * @param values
         */
        public void setValues(Object... values) {
            this.values = new ArrayList<>();
            for (Object value: values) {
                this.values.add(value);
            }
        }

        private boolean isNot;

        /**
         * 条件がNOTかどうか
         * @return isNot
         */
        public boolean isNot() {
            return this.isNot;
        }

        /**
         * 条件がNOTかどうかをセットする。
         *
         * @param isNot
         */
        public void setNot(boolean isNot) {
            this.isNot = isNot;
        }

        @Override
        public Object clone() {
            Where clone = new Where(this.getColumn(), this.getComparison(), this.getValue());
            clone.setNot(this.isNot());
            ArrayList<Object> cloneValues = new ArrayList<>();
            for (Object value: this.getValues()) {
                // Dateインスタンスのコピー
                if (value instanceof Date) {
                    try {
                        @SuppressWarnings("unchecked")
                        Class<Date> dateClass = (Class<Date>) Class.forName(value.getClass().getName());
                        Date cloneDate = dateClass.newInstance();
                        cloneDate.setTime(((Date) value).getTime());
                        cloneValues.add(cloneDate);
                        continue;
                    } catch (Exception exception) {
                    }
                }
                cloneValues.add(value);
            }
            clone.setValues(cloneValues.toArray());
            return clone;
        }

    }

}
