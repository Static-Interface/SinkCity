package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityResident_City_Row;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityResident_City_Table extends AbstractTable<CityResident_City_Row> {

    public static final String TABLE_NAME = "rel_cityresident_city";

    public CityResident_City_Table(Database db) {
        super(TABLE_NAME, db);
    }

}
