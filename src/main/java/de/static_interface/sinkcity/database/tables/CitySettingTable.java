package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CitySettingRow;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CitySettingTable extends AbstractTable<CitySettingRow> {

    public static final String TABLE_NAME = "citysetting";

    public CitySettingTable(Database db) {
        super(TABLE_NAME, db);
    }

}
