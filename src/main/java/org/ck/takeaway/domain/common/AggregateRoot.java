package org.ck.takeaway.domain.common;

import java.util.UUID;

public abstract class AggregateRoot extends Entity {

    protected AggregateRoot(UUID id) {
        super(id);
    }

}