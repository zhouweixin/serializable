package org.zwx;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Sex {
    MALE("男"), FEMALE("女");

    private String name;

    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonValue
    public int getOrdinal() {
        return super.ordinal();
    }

//    @JsonValue
//    public String getOrdinal() {
//        return name;
//    }
}
