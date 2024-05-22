package org.ck.takeaway.domain.gameaggregate.entities;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.ck.takeaway.domain.common.Entity;
import org.ck.takeaway.domain.gameaggregate.valueobjects.InputNumber;
import org.ck.takeaway.domain.gameaggregate.valueobjects.OutputNumber;

import lombok.Getter;

public class Player extends Entity {

    private static final List<Integer> VALID_MOVES = List.of(-1, 0, 1);

    @Getter
    private String name;

    @Getter
    private Integer addedNumber;

    public Player(final UUID id, final String name) {
        super(id);
        this.name = name;
    }

    public OutputNumber move(final InputNumber number) {
        final Random random = new Random();
        int low = 1;
        int high = 3;
        int result = random.nextInt(high-low) + low;

        this.addedNumber = VALID_MOVES.get(result);

        return new OutputNumber((number.getValue() + addedNumber) / 3);
    }
}
