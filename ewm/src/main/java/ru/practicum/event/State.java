package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {

    PENDING(1, "PENDING"),
    PUBLISHED(2, "PUBLISHED"),
    CANCELED(3, "CANCELED");

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
