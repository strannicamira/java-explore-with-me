package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {

    PENDING(0, "PENDING"),
    SEND_TO_REVIEW(1, "SEND_TO_REVIEW"),
    CANCEL_REVIEW(2, "CANCEL_REVIEW "),
    PUBLISHED(3, "PUBLISHED"),
    CANCELED(4, "CANCELED");

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

    //    @JsonCreator
    public static State forValues(//@JsonProperty("id")
                                  Integer id) {
        for (State state : State.values()) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public static State forValues(//@JsonProperty("name")
                                  String name) {
        for (State state : State.values()) {
            if (state.name == name) {
                return state;
            }
        }
        return null;
    }
}
