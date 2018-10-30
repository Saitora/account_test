package ru.sberbank.helpers;

public class Generator {

    public static String generateAccountNumber() {
        String result = "";
        for (int i = 0; i < 16; ++i) {
            int num = (int) (Math.random() * 9.);
            result = result.concat(Integer.toString(num));
        }
        return result;
    }

}
