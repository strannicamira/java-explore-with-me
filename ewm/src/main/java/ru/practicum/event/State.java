package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {

    PENDING(0, "PENDING"),
    PUBLISHED(1, "PUBLISHED"),
    CANCELED(2, "CANCELED");

    private final Integer id;
    private final String name;

    State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    @Override
    @JsonCreator
    public String toString() {
        return getName();
    }
}
