package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityChunkSettingRow;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityChunkSettingTable extends AbstractTable<CityChunkSettingRow> {

    public static String TABLE_NAME = "citychunksetting";

    public CityChunkSettingTable(Database db) {
        super(TABLE_NAME, db);
    }

}
