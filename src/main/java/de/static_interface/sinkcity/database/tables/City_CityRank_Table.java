package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.City_CityRank_Row;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class City_CityRank_Table extends AbstractTable<City_CityRank_Row> {

    public static final String TABLE_NAME = "rel_city_cityrank";

    public City_CityRank_Table(Database db) {
        super(TABLE_NAME, db);
    }

}
