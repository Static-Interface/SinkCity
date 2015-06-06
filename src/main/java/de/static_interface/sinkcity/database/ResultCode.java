package de.static_interface.sinkcity.database;

public enum ResultCode {

    INTERNAL_EXCEPTION,
    CITY_NAME_EXISTS,
    CITY_EXISTS_HERE,
    CITY_NAME_EQUALS_NULL,
    CITY_STORED,

    CITY_DOESNT_EXIST,
    CITY_UPDATED,

    CITY_DROPPED,

    CITY_RENAMED,

    PLAYER_ADDED,
}
