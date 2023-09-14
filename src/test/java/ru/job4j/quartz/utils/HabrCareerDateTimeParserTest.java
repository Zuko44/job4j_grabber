package ru.job4j.quartz.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    public void whenAddNewStringAndThenNewFormatOfString() {
        String date = "2023-09-13T19:50:05+03:00";
        LocalDateTime expected = LocalDateTime.of(2023,
                9, 13, 19, 50, 5);
        LocalDateTime parsed = new HabrCareerDateTimeParser().parse(date);
        assertThat(parsed).isEqualTo(expected);
    }

    @Test
    public void whenAddNewStringAndLengthIsNineteenAndThenNewFormatOfString() {
        String date = "2023-09-13T19:50:05";
        LocalDateTime parsed = new HabrCareerDateTimeParser().parse(date);
        assertThat(parsed).isEqualTo(date);
    }
}