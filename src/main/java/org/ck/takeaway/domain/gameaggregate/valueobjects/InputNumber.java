package org.ck.takeaway.domain.gameaggregate.valueobjects;

import java.util.Objects;
import java.util.Random;

import org.ck.takeaway.domain.common.ValueObject;

import lombok.Getter;

public class InputNumber implements ValueObject {

    private static Integer MIN_POSSIBLE_INPUT_NUMBER = 10;
    private static Integer MAX_POSSIBLE_INPUT_NUMBER = 100;

    @Getter
    private final int value;

    public InputNumber() {
        this.value = new Random().nextInt(MAX_POSSIBLE_INPUT_NUMBER - MIN_POSSIBLE_INPUT_NUMBER) + MIN_POSSIBLE_INPUT_NUMBER;
    }

    public InputNumber(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputNumber that = (InputNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
