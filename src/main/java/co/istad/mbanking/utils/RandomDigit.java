package co.istad.mbanking.utils;

import java.util.Random;

public class RandomDigit {
    public static String generate9Digits() {
        Random random = new Random();
        return String.format("%09d", random.nextInt(1000000000));
    }

}
