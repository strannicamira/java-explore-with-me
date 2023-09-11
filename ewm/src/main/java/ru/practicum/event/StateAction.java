package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public enum StateAction {
    SEND_TO_REVIEW(1, "SEND_TO_REVIEW"),//send by private user to request admin to PUBLISHED or CANCELED event
    CANCEL_REVIEW(2, "CANCEL_REVIEW"),//send by private user to set status CANCELED event
    PUBLISH_EVENT(3, "PUBLISH_EVENT"),//send by admin to set status PUBLISHED event
    REJECT_EVENT(4, "REJECT_EVENT");//send by admin to set status CANCELED event
    private final Integer id;
    private final String name;

    StateAction(Integer id, String name) {
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
    public static StateAction forValues(Integer id) {
        for (StateAction state : StateAction.values()) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    public static StateAction forValues(String name) {
        for (StateAction state : StateAction.values()) {
            if (name.equals(state.name)) {
                return state;
            }
        }
        return null;
    }

    public static List<StateAction> forValues(String[] names) {
        List<StateAction> states = new ArrayList<>();
        for (String name : names) {
            states.add(forValues(name));
        }
        return states;
    }
}
