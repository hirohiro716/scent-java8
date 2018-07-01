package com.hirohiro716.barcode;

import com.hirohiro716.number.CalculationHelper;
import com.hirohiro716.validate.StringValidator;
import com.hirohiro716.validate.ValidationException;

/**
 * JAN13コードのチェックなど
 * @author hiro
 */
public class JAN13Helper {

    /**
     * JAN13コードの有効性チェックを行います.
     * @param barcode バーコード
     * @return 結果
     */
    public static boolean isValid(String barcode) {
        if (barcode == null) {
            return false;
        }
        if (barcode.length() == 13) {
            String checkDigit = computeCheckDigit(barcode);
            if (barcode.substring(12, 13).equals(checkDigit)) {
                return true;
            }
        }
        if (barcode.length() == 12) {
            String checkDigit = computeCheckDigit("0" + barcode);
            if (("0" + barcode).substring(12, 13).equals(checkDigit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * チェックデジットを除いた値を取得します.
     * @param barcode バーコード
     * @return チェックデジットを除いた値12桁
     */
    public static String extractExceptCheckDigit(String barcode) {
        if (isValid(barcode)) {
            String newBarcode = barcode;
            if (newBarcode.substring(0, 1).equals("0")) {
                newBarcode = "0" + newBarcode;
            }
            return newBarcode.substring(0, 12);
        }
        return "";
    }

    /**
     * チェックデジットを除いた値をノーチェックで取得します.
     * @param barcode バーコード
     * @return チェックデジットを除いた値12桁
     */
    public static String extractExceptCheckDigitNoCheck(String barcode) {
        String newBarcode = barcode;
        if (newBarcode.substring(0, 1).equals("0")) {
            newBarcode = "0" + newBarcode;
        }
        return newBarcode.substring(0, 12);
    }

    /**
     * チェックデジットを算出します.
     * @param barcode バーコード
     * @return チェックデジット
     */
    public static String computeCheckDigit(String barcode) {
        try {
            StringValidator validator = new StringValidator();
            validator.addMinLengthCheck(12);
            validator.addIntegerCheck();
            validator.execute(barcode);
            // バーコードの文字位置が奇数が偶数かで分ける
            String barcode12 = barcode.substring(0, 12);
            StringBuilder odd = new StringBuilder();
            StringBuilder even = new StringBuilder();
            for (int i = 0; i < barcode12.length(); i++) {
                String addVal = barcode12.substring(i, i + 1);
                if ((i + 1) % 2 != 0) {
                    odd.append(addVal);
                } else {
                    even.append(addVal);
                }
            }
            // 文字列内の値を集計する
            int oddVal = CalculationHelper.calculateAddingAllNumberStrings(odd.toString());
            int evenVal = CalculationHelper.calculateAddingAllNumberStrings(even.toString());
            // 偶数の結果を3倍する
            int evenValx3 = evenVal * 3;
            // 3倍した偶数と奇数の和を算出
            String evenValx3AndOddVal = String.valueOf(evenValx3 + oddVal);
            // 下一桁をとる
            String lastOne = evenValx3AndOddVal.substring(evenValx3AndOddVal.length() - 1);
            // 10から下一桁を引いた値を算出
            String tenAndLastOneDifference = String.valueOf(10 - Integer.parseInt(lastOne));
            // また下一桁をとる
            String tenAndLastOneDifferenceAtLastOne = tenAndLastOneDifference.substring(tenAndLastOneDifference.length() - 1);
            // 結果を返す
            return tenAndLastOneDifferenceAtLastOne;
        } catch (ValidationException exception) {
            return "";
        }
    }

    /**
     * 先頭の数字から左6桁のパリティ区分を特定する
     */
    protected static final int[][] LEFT_PARITIES_TYPES = {
            { 0, 0, 0, 0, 0, 0 }, // 0
            { 0, 0, 1, 0, 1, 1 }, // 1
            { 0, 0, 1, 1, 0, 1 }, // 2
            { 0, 0, 1, 1, 1, 0 }, // 3
            { 0, 1, 0, 0, 1, 1 }, // 4
            { 0, 1, 1, 0, 0, 1 }, // 5
            { 0, 1, 1, 1, 0, 0 }, // 6
            { 0, 1, 0, 1, 0, 1 }, // 7
            { 0, 1, 0, 1, 1, 0 }, // 8
            { 0, 1, 1, 0, 1, 0 } // 9
    };

    /**
     * 区分毎に分けられた左側6桁のパリティ
     */
    protected static final int[][][] LEFT_PARITIES = {
            {
                    // 奇数パリティ
                    { 0, 0, 0, 1, 1, 0, 1 }, // 0
                    { 0, 0, 1, 1, 0, 0, 1 }, // 1
                    { 0, 0, 1, 0, 0, 1, 1 }, // 2
                    { 0, 1, 1, 1, 1, 0, 1 }, // 3
                    { 0, 1, 0, 0, 0, 1, 1 }, // 4
                    { 0, 1, 1, 0, 0, 0, 1 }, // 5
                    { 0, 1, 0, 1, 1, 1, 1 }, // 6
                    { 0, 1, 1, 1, 0, 1, 1 }, // 7
                    { 0, 1, 1, 0, 1, 1, 1 }, // 8
                    { 0, 0, 0, 1, 0, 1, 1 } // 9
    },
            {
                    // 偶数パリティ
                    { 0, 1, 0, 0, 1, 1, 1 }, // 0
                    { 0, 1, 1, 0, 0, 1, 1 }, // 1
                    { 0, 0, 1, 1, 0, 1, 1 }, // 2
                    { 0, 1, 0, 0, 0, 0, 1 }, // 3
                    { 0, 0, 1, 1, 1, 0, 1 }, // 4
                    { 0, 1, 1, 1, 0, 0, 1 }, // 5
                    { 0, 0, 0, 0, 1, 0, 1 }, // 6
                    { 0, 0, 1, 0, 0, 0, 1 }, // 7
                    { 0, 0, 0, 1, 0, 0, 1 }, // 8
                    { 0, 0, 1, 0, 1, 1, 1 } // 9
    }
    };

    /**
     * 右側6桁のパリティ
     */
    protected static final int[][] RIGHT_PARITIES = {
            { 1, 1, 1, 0, 0, 1, 0 }, // 0
            { 1, 1, 0, 0, 1, 1, 0 }, // 1
            { 1, 1, 0, 1, 1, 0, 0 }, // 2
            { 1, 0, 0, 0, 0, 1, 0 }, // 3
            { 1, 0, 1, 1, 1, 0, 0 }, // 4
            { 1, 0, 0, 1, 1, 1, 0 }, // 5
            { 1, 0, 1, 0, 0, 0, 0 }, // 6
            { 1, 0, 0, 0, 1, 0, 0 }, // 7
            { 1, 0, 0, 1, 0, 0, 0 }, // 8
            { 1, 1, 1, 0, 1, 0, 0 }, // 9
    };

}