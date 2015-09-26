package de.static_interface.sinkcity;

public enum CityPermissions {

    /**
     * Allows to place blocks (includes using a hoe)
     */
    CREATE,
    /**
     * Allows to break blocks
     */
    DESTROY,
    /**
     * Allows to change blocks (e.g. place coal in a furnace)
     */
    BLOCKUSE,
    /**
     * Allows using items (e.g. lighters)
     */
    ITEMUSE,
    /**
     * Allows modifying the city (e.g. allow monsters, enable fire...)
     */
    MODIFY_CITY,
    /**
     * Allows administrating the city (e.g. add ranks, invite users)
     */
    ADMINISTRATE_CITY;

    /**
     * Finds the {@link CityPermissions} that matches <code>name</code> the
     * most. This is means that even "c" as <code>name</code> would return
     * {@link CityPermissions#CREATE}.
     * 
     * @param name
     *            An identifier for the wanted {@link CityPermissions} instance.
     * @return The requested {@link CityPermissions} or <code>null</code> if
     *         none was found.
     */
    public static CityPermissions forName(String name) {
        for (CityPermissions cityPermissions : CityPermissions.values()) {
            if (cityPermissions.toString().toLowerCase().startsWith(name.toLowerCase()))
                return cityPermissions;
        }

        return null;
    }
}
