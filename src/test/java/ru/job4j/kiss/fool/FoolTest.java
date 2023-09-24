package ru.job4j.kiss.fool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FoolTest {
    @Test
    public void opponentWhen3and4is0ThenFizzBuzz() {
        assertThat(Fool.check(15)).isEqualTo("FizzBuzz");
    }
    @Test
    public void opponentWhen3is0ThenFizz() {
        assertThat(Fool.check(3)).isEqualTo("Fizz");
    }

    @Test
    public void opponentWhen5is0ThenBuzz() {
        assertThat(Fool.check(5)).isEqualTo("Buzz");
    }

    @Test
    public void opponentWhen2Then2() {
        assertThat(Fool.check(2)).isEqualTo("2");
    }
}