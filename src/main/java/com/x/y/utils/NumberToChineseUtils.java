package com.x.y.utils;

import org.springframework.util.Assert;

import java.math.BigDecimal;

public final class NumberToChineseUtils {
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] CN_UPPER_MONEY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
    private static final String CN_FULL = "整";
    private static final String CN_NEGATIVE = "负";
    private static final int MONEY_PRECISION = 2;
    private static final double MONEY_MAX = 99999999999999.99d;
    private static final double MONEY_MIN = -99999999999999.99d;

    private NumberToChineseUtils() {
    }

    public static String convert(double money) {
        Assert.isTrue(money >= MONEY_MIN && money <= MONEY_MAX, "金额无效");
        BigDecimal numberOfMoney = BigDecimal.valueOf(money);
        StringBuilder sb = new StringBuilder();
        int sigNum = numberOfMoney.signum();
        if (sigNum == 0) {
            return "零元" + CN_FULL;
        }
        long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
        long scale = number % 100;
        int numIndex = 0;
        boolean getZero = false;
        if (!(scale > 0)) {
            numIndex = 2;
            number /= 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number /= 10;
            getZero = true;
        }
        int zeroSize = 0;
        int numUnit;
        while (number > 0) {
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                zeroSize++;
                if (!getZero) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex]);
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONEY_UNIT[numIndex]);
                }
                getZero = true;
            }
            number /= 10;
            numIndex++;
        }
        if (sigNum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        if (scale == 0) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
}