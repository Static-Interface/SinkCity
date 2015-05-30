package de.static_interface.sinkcity;

import java.util.ArrayList;
import java.util.List;

public enum CitySettings {

    FIRE(0b0000_0001),
    MOBS(0b0000_0010),
    TNT_BUILD(0b0000_0100),
    TNT_EXPLODE(0b0000_1000),
    EXPLOSION(0b0001_0000),
    OPEN_CITY(0b0010_0000);

    private int activatedPattern;

    private CitySettings(int activatedPattern) {
        this.activatedPattern = activatedPattern;
    }

    public static List<CitySettings> getActivatedCitySettings(int citySettings) {
        List<CitySettings> activatedSettings = new ArrayList<CitySettings>();
        for (CitySettings settings : CitySettings.values()) {
            if ((citySettings & settings.activatedPattern) == settings.activatedPattern)
                activatedSettings.add(settings);
        }

        return activatedSettings;
    }

    public static boolean settingActivated(CitySettings setting, int citySettings) {
        return ((setting.activatedPattern & citySettings) == setting.activatedPattern);
    }
}
