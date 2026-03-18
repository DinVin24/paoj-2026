package com.pao.laboratory03.enums;

public enum Priority {
    LOW(1, "green") {
        public String getEmoji() { return "🟢"; }
    },
    MEDIUM(2, "yellow") {
        public String getEmoji() { return "🟡"; }
    },
    HIGH(3, "orange") {
        public String getEmoji() { return "🟠"; }
    },
    CRITICAL(4, "red") {
        public String getEmoji() { return "🔴"; }
    };

    private final int level;
    private final String color;

    Priority(int level, String color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() { return level; }
    public String getColor() { return color; }

    public abstract String getEmoji();
}