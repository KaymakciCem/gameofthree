package org.ck.takeaway.domain.gameaggregate.valueobjects;

import java.util.Objects;
import java.util.Random;

import org.ck.takeaway.domain.common.ValueObject;

import lombok.Getter;

public class OutputNumber implements ValueObject {

    @Getter
    private final int value;

    public OutputNumber(final int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutputNumber that = (OutputNumber) o;
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
