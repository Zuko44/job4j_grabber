package ru.job4j.kiss.fool;

import java.util.Scanner;

public class Fool {
    public static void main(String[] args) {
        System.out.println("Игра FizzBuzz.");
        var startAt = 1;
        var io = new Scanner(System.in);
        while (startAt < 100) {
            System.out.println(check(startAt));
            /**if (startAt % 3 == 0 && startAt % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (startAt % 3 == 0) {
                System.out.println("Fizz");
            } else if (startAt % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(startAt);
            }*/
            startAt++;
            var answer = io.nextLine();
            /**if (startAt % 3 == 0 && startAt % 5 == 0) {
                if (!"FizzBuzz".equals(answer)) {
                    System.out.println("Ошибка. Начинай снова.");
                    startAt = 0;
                }
            } else if (startAt % 3 == 0) {
                if (!"Fizz".equals(answer)) {
                    System.out.println("Ошибка. Начинай снова.");
                    startAt = 0;
                }
            } else if (startAt % 5 == 0) {
                if (!"Buzz".equals(answer)) {
                    System.out.println("Ошибка. Начинай снова.");
                    startAt = 0;
                }
            } else {
                if (!String.valueOf(answer).equals(answer)) {
                    System.out.println("Ошибка. Начинай снова.");
                    startAt = 0;
                }
            }*/
            if (!check(startAt).equals(answer)) {
                System.out.println("Ошибка. Начинай снова.");
                startAt = 0;
            }
            startAt++;
        }
    }

    public static String check(int num) {
        String out;
        if (num % 3 == 0 && num % 5 == 0) {
            out = "FizzBuzz";
        } else if (num % 3 == 0) {
            out = "Fizz";
        } else if (num % 5 == 0) {
            out = "Buzz";
        } else {
            out = Integer.toString(num);
        }
        return out;
    }
}
