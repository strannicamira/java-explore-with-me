package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public enum EventSort {

    EVENT_DATE(0, "EVENT_DATE"),

    VIEWS(1, "VIEWS");
    private final Integer id;
    private final String name;

    EventSort(Integer id, String name) {
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
    public static EventSort forValues(//@JsonProperty("id")
                                      Integer id) {
        for (EventSort state : EventSort.values()) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public static EventSort forValues(//@JsonProperty("name")
                                      String name) {
        for (EventSort state : EventSort.values()) {
            if (state.name.equals(name)) {
                return state;
            }
        }
        return null;
    }


    public static List<EventSort> forValues(//@JsonProperty("name")
                                            String[] names) {
        List<EventSort> states = new ArrayList<>();
        for (String name : names) {
            states.add(forValues(name));
        }
        return states;
    }
}
