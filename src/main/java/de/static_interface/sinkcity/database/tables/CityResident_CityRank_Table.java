package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityResident_CityRank_Row;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityResident_CityRank_Table extends AbstractTable<CityResident_CityRank_Row> {

    public static final String TABLE_NAME = "rel_cityresident_cityrank";

    public CityResident_CityRank_Table(Database db) {
        super(TABLE_NAME, db);
    }

}
