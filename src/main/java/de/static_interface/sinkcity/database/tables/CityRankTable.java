package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityRankRow;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityRankTable extends AbstractTable<CityRankRow> {

    public static final String TABLE_NAME = "cityrank";

    public CityRankTable(Database db) {
        super(TABLE_NAME, db);
    }

}
