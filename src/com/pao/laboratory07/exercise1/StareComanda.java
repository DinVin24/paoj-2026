package com.pao.laboratory07.exercise1;

public enum StareComanda {
    PLACED, PROCESSED, SHIPPED, DELIVERED, CANCELED;

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELED;
    }

    public StareComanda next() {
        return switch (this) {
            case PLACED -> PROCESSED;
            case PROCESSED -> SHIPPED;
            case SHIPPED -> DELIVERED;
            default -> this;
        };
    }
}
