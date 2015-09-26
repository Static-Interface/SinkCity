package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityRank_City_Row;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityRank_City_Table extends AbstractTable<CityRank_City_Row> {

    public static final String TABLE_NAME = "rel_cityrank_city";

    public CityRank_City_Table(Database db) {
        super(TABLE_NAME, db);
    }

}
