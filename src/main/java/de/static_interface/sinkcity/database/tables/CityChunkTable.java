package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityChunkRow;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityChunkTable extends AbstractTable<CityChunkRow> {

    public static final String TABLE_NAME = "citychunks";

    public CityChunkTable(Database db) {
        super(TABLE_NAME, db);
    }

}
