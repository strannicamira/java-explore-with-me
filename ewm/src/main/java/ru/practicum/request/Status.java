package ru.practicum.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

//TODO: rename to EventRequestStatus or smth like this
public enum Status {
    PENDING(0, "PENDING"),
    REJECTED(1, "REJECTED"),
    CONFIRMED(2, "CONFIRMED"),
    CANCELED(3, "CANCELED");
    private final Integer id;
    private final String name;

    Status(Integer id, String name) {
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

    public static Status forValues(Integer id) {
        for (Status status : Status.values()) {
            if (status.id.equals(id)) {
                return status;
            }
        }
        return null;
    }

    public static Status forValues(String name) {
        for (Status status : Status.values()) {
            if (name.equals(status.name)) {
                return status;
            }
        }
        return null;
    }


    public static List<Status> forValues(String[] names) {
        List<Status> statuses = new ArrayList<>();
        for (String name : names) {
            statuses.add(forValues(name));
        }
        return statuses;
    }
}
