package com.hirohiro716;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.hirohiro716.datetime.Datetime;

/**
 * LinkedHashMapに雑な使い方ができるよう機能を追加したクラス.<br>
 * キーにはプリミティブ型かString型を指定します. 値はObject型.<br>
 * @author hiro
 */
public class RudeArray implements Cloneable, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1692187503079204210L;

    /**
     * コンストラクタ.
     */
    public RudeArray() {
        this.baseArray = new LinkedHashMap<>();
    }

    // 値の保持用
    private LinkedHashMap<Object, Object> baseArray;

    /**
     * 内部的に保持しているLinkedHashMapを取得する.
     * @param <K>
     * @param <V> 
     * @return HashMap
     */
    @SuppressWarnings("unchecked")
    public <K, V> LinkedHashMap<K, V> getLinkedHashMap() {
        try {
            return (LinkedHashMap<K, V>) this.baseArray;
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 内部的に保持しているHashMapを上書きする.
     * @param hashMap
     */
    @SuppressWarnings("unchecked")
    public void setLinkedHashMap(LinkedHashMap<?, ?> hashMap) {
        try {
            this.baseArray = (LinkedHashMap<Object, Object>) hashMap;
        } catch (Exception exception) {
        }
    }

    /**
     * キーを指定して値をセットする.
     * @param key キー
     * @param value 値
     */
    public void put(Object key, Object value) {
        this.baseArray.put(key, value);
    }

    private int autoKey = 0;

    /**
     * キーを指定せずに値をセットする. キーは0以上の自動採番.
     * @param value 値
     */
    public void add(Object value) {
        if (this.size() == 0) {
            this.autoKey = 0;
        }
        while (this.containsKey(this.autoKey)) {
            this.autoKey += 1;
        }
        this.baseArray.put(this.autoKey, value);
        this.autoKey += 1;
    }

    /**
     * Object配列をキーを指定せずにセットする. キーは0以上の自動採番.
     * @param values 追加する値配列
     */
    public void addMultiple(Object[] values) {
        if (values != null) {
            for (Object o : values) {
                this.add(o);
            }
        }
    }

    /**
     * セットした値を取得する.
     * @param key キー
     * @return 値
     */
    public Object get(Object key) {
        return this.baseArray.get(key);
    }

    /**
     * セットした値をStringとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public String getString(Object key) {
        try {
            return (String) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return this.baseArray.get(key).toString();
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をByteとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Byte getByte(Object key) {
        try {
            return (byte) this.baseArray.get(key);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をNumberとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Number getNumber(Object key) {
        try {
            return (Number) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToDouble(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をShortとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Short getShort(Object key) {
        try {
            return (Short) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToShort(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をIntegerとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Integer getInteger(Object key) {
        try {
            return (Integer) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToInteger(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をFloatとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Float getFloat(Object key) {
        try {
            return (Float) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToFloat(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をLongとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Long getLong(Object key) {
        try {
            return (Long) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToLong(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をDoubleとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Double getDouble(Object key) {
        try {
            return (Double) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToDouble(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をBooleanとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Boolean getBoolean(Object key) {
        try {
            return (Boolean) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            return StringConverter.stringToBoolean(this.baseArray.get(key).toString());
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をDateとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Date getDate(Object key) {
        // Date
        try {
            return (Date) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            // Timestamp
            try {
                return new Date(((Timestamp) this.baseArray.get(key)).getTime());
            } catch (ClassCastException e2) {
                return Datetime.stringToDate(this.baseArray.get(key).toString());
            }
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をjava.sql.Dateとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public java.sql.Date getDateSql(Object key) {
        // java.sql.Date
        try {
            return (java.sql.Date) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            // Date
            try {
                return new java.sql.Date(((Date) this.baseArray.get(key)).getTime());
            } catch (ClassCastException e2) {
                // Timestamp
                try {
                    return new java.sql.Date(((Timestamp) this.baseArray.get(key)).getTime());
                } catch (ClassCastException e3) {
                    return new java.sql.Date(Datetime.stringToDate(this.baseArray.get(key).toString()).getTime());
                }
            }
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をTimeとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Time getTimeSql(Object key) {
        // Time
        try {
            return (Time) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            // Date
            try {
                return new Time(((Date) this.baseArray.get(key)).getTime());
            } catch (ClassCastException e2) {
                // Timestamp
                try {
                    return new Time(((Timestamp) this.baseArray.get(key)).getTime());
                } catch (ClassCastException e3) {
                    return new Time(Datetime.stringToDate(this.baseArray.get(key).toString()).getTime());
                }
            }
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をTimestampとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public Timestamp getTimestampSql(Object key) {
        // Timestamp
        try {
            return (Timestamp) this.baseArray.get(key);
        } catch (ClassCastException exception) {
            // Date
            try {
                return new Timestamp(((Date) this.baseArray.get(key)).getTime());
            } catch (ClassCastException e2) {
                return new Timestamp(Datetime.stringToDate(this.baseArray.get(key).toString()).getTime());
            }
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * セットした値をRudeArrayとして取得する.
     * @param key キー
     * @return 値 存在しない場合や型が違った場合はnullを返す.
     */
    public RudeArray getRudeArray(Object key) {
        try {
            return (RudeArray) this.baseArray.get(key);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * キーが存在するか確認する.
     * @param key キー
     * @return 結果
     */
    public boolean containsKey(Object key) {
        return this.baseArray.containsKey(key);
    }

    /**
     * 値が存在するか確認する.
     * @param value 値
     * @return 結果
     */
    public boolean containsValue(Object value) {
        return this.baseArray.containsValue(value);
    }

    /**
     * 指定されたキーの要素を削除する.
     * @param key キー
     * @return 結果
     */
    public boolean removeKey(Object key) {
        if (this.containsKey(key)) {
            this.baseArray.remove(key);
            return true;
        }
        return false;
    }

    /**
     * 指定された値の要素を削除する.
     * @param value 値
     * @return 結果
     */
    public boolean removeValue(Object value) {
        for (Object key : this.getKeys()) {
            if (this.get(key).equals(value)) {
                this.baseArray.remove(key);
                return true;
            }
        }
        return false;
    }

    /**
     * 保持しているすべての値を消去する.
     */
    public void clear() {
        this.autoKey = 0;
        this.baseArray.clear();
    }

    /**
     * 配列数を取得する.
     * @return 配列数
     */
    public int size() {
        return this.baseArray.size();
    }

    /**
     * 配列内のキーをすべて取得する.
     * @return すべてのキー
     */
    public Object[] getKeys() {
        Iterator<Object> iterator = this.baseArray.keySet().iterator();
        List<Object> arrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            arrayList.add(iterator.next());
        }
        return arrayList.toArray();
    }

    /**
     * 配列内のキーをString[]としてすべて取得する.
     * @return すべてのキー
     */
    public String[] getKeysAtString() {
        Iterator<Object> iterator = this.baseArray.keySet().iterator();
        String[] keys = new String[this.size()];
        int i = 0;
        while (iterator.hasNext()) {
            try {
                keys[i] = (String) iterator.next();
            } catch (Exception exception) {
                keys[i] = iterator.next().toString();
            }
            i++;
        }
        return keys;
    }

    /**
     * 配列内の値をすべて取得する.
     * @return すべての値
     */
    public Object[] getValues() {
        List<Object> arrayList = new ArrayList<>();
        for (Object key : this.getKeys()) {
            arrayList.add(this.get(key));
        }
        return arrayList.toArray();
    }

    /**
     * 配列内の値をString[]してすべて取得する.
     * @return すべての値
     */
    public String[] getValuesAtString() {
        String[] values = new String[this.size()];
        int i = 0;
        for (Object key : this.getKeys()) {
            try {
                values[i] = (String) this.get(key);
            } catch (Exception exception) {
                values[i] = this.get(key).toString();
            }
            i++;
        }
        return values;
    }

    /**
     * 配列内の値をRudeArray[]としてすべて取得する.
     * @return すべての値
     */
    public RudeArray[] getValuesAtRudeArray() {
        RudeArray[] values = new RudeArray[this.size()];
        int i = 0;
        for (Object key : this.getKeys()) {
            try {
                values[i] = (RudeArray) this.get(key);
            } catch (ClassCastException exception) {
                return null;
            }
            i++;
        }
        return values;
    }

    /**
     * 配列を結合する. キーが重複していた場合は後の値で上書きされる.
     * @param arrays 配列
     */
    public void merge(RudeArray... arrays) {
        for (RudeArray array: arrays) {
            for (Object key: array.getKeys()) {
                this.put(key, array.get(key));
            }
        }
    }

    /**
     * すべての値を連結した文字列を取得する.
     * @param separator 連結文字列
     * @return 連結後文字列
     */
    public String join(String separator) {
        boolean firstDone = false;
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Object> iterator = this.baseArray.keySet().iterator();
        while (iterator.hasNext()) {
            if (firstDone) {
                stringBuilder.append(separator);
            } else {
                firstDone = true;
            }
            Object value = this.baseArray.get(iterator.next());
            if (value != null) {
                stringBuilder.append(value);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 配列のコピーを新しいインスタンスで作成する.
     * @return 配列
     */
    @Override
    public final RudeArray clone() {
        RudeArray array;
        try {
            array = (RudeArray) super.clone();
            array.setLinkedHashMap(new LinkedHashMap<>(this.baseArray));
            for (Object key : array.getKeys()) {
                Object value = array.get(key);
                if (value != null) {
                    if (value instanceof RudeArray) {
                        RudeArray myArray = (RudeArray) value;
                        array.put(myArray.clone(), key);
                    }
                }
            }
        } catch (CloneNotSupportedException exception) {
            array = new RudeArray();
        }
        return array;
    }

    /**
     * シリアライズを行いByte配列を取得する. 値の部分に入っているオブジェクトのシリアライズはサポートしない.
     * @return シリアライズしたByte配列
     * @throws IOException
     */
    public byte[] toSerialize() throws IOException {
        return ByteConverter.serialize(this);
    }

    /**
     * Byte配列からデシリアライズを行いインスタンスを復元する. 値の部分に入っているオブジェクトのシリアライズはサポートしない.
     * @param serialized byte配列
     * @return RudeArray
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static RudeArray desirialize(byte[] serialized) throws IOException, ClassNotFoundException {
        return ByteConverter.desirialize(serialized);
    }

    /**
     * 内部HashMapのtoStringを出力する.
     * @return キーと値の組み合わせ
     */
    @Override
    public String toString() {
        return this.baseArray.toString();
    }

    /**
     * 複数のRudeArrayを含むArrayListをRudeArray内部のキーを基準に昇順で並び替えを行い新しいArrayListを取得する.
     * @param source 並び替え対象ArrayList
     * @param sortKey 基準値が含まれるRudeArray内のキー
     * @return 並び替え後の新規ArrayList
     */
    public static ArrayList<RudeArray> sortAsc(Iterable<RudeArray> source, String sortKey) {
        ArrayList<RudeArray> result = new ArrayList<>();
        for (RudeArray array: source) {
            result.add(array);
        }
        Collections.sort(result, new RudeArrayComparatorAsc(sortKey));
        return result;
    }

    /**
     * 複数のRudeArrayを含むArrayListをRudeArray内部のキーを基準に降順で並び替えを行い新しいArrayListを取得する.
     * @param source 並び替え対象ArrayList
     * @param sortKey 基準値が含まれるRudeArray内のキー
     * @return 並び替え後の新規ArrayList
     */
    public static ArrayList<RudeArray> sortDesc(Iterable<RudeArray> source, String sortKey) {
        ArrayList<RudeArray> result = new ArrayList<>();
        for (RudeArray array: source) {
            result.add(array);
        }
        Collections.sort(result, new RudeArrayComparatorDesc(sortKey));
        return result;
    }

    /**
     * RudeArrayを昇順で並び替えるソート条件クラス.
     * @author hiro
     */
    private static class RudeArrayComparatorAsc implements Comparator<RudeArray> {

        private Object sortKey;

        /**
         * コンストラクタで並び替えの基準とするキーを指定する.
         * @param sortKey
         */
        public RudeArrayComparatorAsc(Object sortKey) {
            this.sortKey = sortKey;
        }

        @Override
        public int compare(RudeArray array1, RudeArray array2) {
            if (array1.get(this.sortKey) instanceof Number) {
                Number value1 = array1.getNumber(this.sortKey);
                Number value2 = array2.getNumber(this.sortKey);
                if (value1.doubleValue() > value2.doubleValue()) {
                    return 1;
                } else if (value1.doubleValue() == value2.doubleValue()) {
                    return 0;
                } else {
                    return -1;
                }
            }
            return 0;
        }

    }

    /**
     * RudeArrayを降順で並び替えるソート条件クラス.
     * @author hiro
     */
    private static class RudeArrayComparatorDesc implements Comparator<RudeArray> {

        private Object sortKey;

        /**
         * コンストラクタで並び替えの基準とするキーを指定する.
         * @param sortKey
         */
        public RudeArrayComparatorDesc(Object sortKey) {
            this.sortKey = sortKey;
        }

        @Override
        public int compare(RudeArray array1, RudeArray array2) {
            if (array1.get(this.sortKey) instanceof Number) {
                Number value1 = array1.getNumber(this.sortKey);
                Number value2 = array2.getNumber(this.sortKey);
                if (value1.doubleValue() > value2.doubleValue()) {
                    return -1;
                } else if (value1.doubleValue() == value2.doubleValue()) {
                    return 0;
                } else {
                    return 1;
                }
            }
            return 0;
        }

    }

}
