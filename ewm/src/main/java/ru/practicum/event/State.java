package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public enum State {

    PENDING(0, "PENDING"),

    PUBLISHED(1, "PUBLISHED"),
    CANCELED(2, "CANCELED"),
    SEND_TO_REVIEW(3, "SEND_TO_REVIEW"),
    CANCEL_REVIEW(4, "CANCEL_REVIEW "),
    PUBLISH_EVENT(5, "PUBLISH_EVENT"),
    REJECT_EVENT(6, "REJECT_EVENT  ");
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
            if (name.equals(state.name)) {
                return state;
            }
        }
        return null;
    }


    public static List<State> forValues(//@JsonProperty("name")
                                        String[] names) {
        List<State> states = new ArrayList<>();
        for (String name : names) {
            states.add(forValues(name));
        }
        return states;
    }
}
