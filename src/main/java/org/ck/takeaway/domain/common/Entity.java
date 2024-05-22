package org.ck.takeaway.domain.common;

import java.util.Objects;
import java.util.UUID;

public abstract class Entity {

    private UUID id;

    public UUID getId() {
        return id;
    }

    protected Entity(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof UUID uuid && id.equals(uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
