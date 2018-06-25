package fr.kwanko.model;

/**
 * Created by vfatu on 17.01.2017.
 */

public enum KwankoAdState {
    LOADING("loading"),
    DEFAULT("default"),
    EXPANDED("expanded"),
    RESIZED("resized"),
    HIDDEN("hidden");

    private String name;

    KwankoAdState(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
