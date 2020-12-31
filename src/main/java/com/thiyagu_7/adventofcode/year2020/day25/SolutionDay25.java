package com.thiyagu_7.adventofcode.year2020.day25;


public class SolutionDay25 {
    public long part1(long cardPublicKey, long doorPublicKey) {
        long cardLoopSize = findLoopSize(cardPublicKey, 7);
        long doorLoopSize = findLoopSize(doorPublicKey, 7);

        long s = 1;
        for (int i = 0; i < cardLoopSize; i++) {
            s = transform(s, doorPublicKey);
        }

        //should be same as above
        /*s = 1;
        for (int i = 0; i < doorLoopSize; i++) {
            s = transform(s, cardPublicKey);
        }
        */
        return s;
    }

    private long transform(long input, long subjectNumber) {
        long result = input;
        result = result * subjectNumber;
        result = result % 20201227;
        return result;
    }

    private long findLoopSize(long publicKey, long subjectNumber) {
        long loopSize = 0;
        long s = 1;
        while (true) {
            s = transform(s, subjectNumber);
            loopSize++;
            if (s == publicKey) {
                break;
            }
        }
        return loopSize;
    }
}
