package de.static_interface.sinkcity;

public enum CitySettings {

    FIRE,
    MOBS,
    TNT_BUILD,
    TNT_EXPLODE,
    EXPLOSION,
    OPEN_CITY,
    MONSTERS;

    public static CitySettings forName(String name) {
        for (CitySettings citySettings : CitySettings.values()) {
            if (citySettings.equals(name.toUpperCase()))
                return citySettings;
        }
        return null;
    }

}
