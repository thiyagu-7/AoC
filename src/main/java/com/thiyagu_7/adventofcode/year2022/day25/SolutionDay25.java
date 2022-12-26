package com.thiyagu_7.adventofcode.year2022.day25;

import java.util.List;
import java.util.Optional;

public class SolutionDay25 {
    public String part1(List<String> input) {
        long decimal = 0;
        for (String snafuNumber : input) {
            decimal += convertSNAFUToDecimal(snafuNumber);
        }
        return convertDecimalToSNAFU(decimal);
    }

    private long convertSNAFUToDecimal(String snafu) {
        long result = 0;
        int n = snafu.length();
        for (int i = n - 1; i >= 0; i--) {
            char c = snafu.charAt(i);
            result += Math.pow(5, n - i - 1) * getDecimalDigit(c);
        }
        return result;
    }

    // SNAFU to decimal digit
    private int getDecimalDigit(char c) {
        return switch (c) {
            case '-' -> -1;
            case '=' -> -2;
            default -> c - '0'; //2, 1 or 0
        };
    }

    private String convertDecimalToSNAFU(long snafu) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        while (snafu > 0) {
            int rem = (int) (snafu % 5);
            snafu = snafu / 5;

            int curr = rem + carry;
            if (curr == 5) { // curr cannot be more than 5 (as carry is always 1)
                result.append('0');
                carry = curr / 5; // will always be 1
                continue;
            }
            DigitAndCarry digitAndCarry = getSNAFUDigit(curr);
            result.append(digitAndCarry.snafuDigit());
            carry = digitAndCarry.carry().orElse(0);
        }
        if (carry != 0) {
            result.append(carry);
        }
        result.reverse();
        return result.toString();
    }

    // decimal digit to SNAFU digit
    private DigitAndCarry getSNAFUDigit(int rem) {
        return switch (rem) {
            case 3 -> new DigitAndCarry('=', Optional.of(1));
            case 4 -> new DigitAndCarry('-', Optional.of(1));
            default -> new DigitAndCarry((char) (rem + '0'), Optional.empty()); //2, 1 or 0 case
        };
    }

    record DigitAndCarry(char snafuDigit, Optional<Integer> carry) {

    }
}
