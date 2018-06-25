package fr.kwanko.model;

/**
 * Created by vfatu on 17.01.2017.
 */

public enum KwankoPlacementType {
    INLINE("inline"),
    INTERSTITIAL("interstitial"),
    OVERLY("overly");

    private String name;

    KwankoPlacementType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
