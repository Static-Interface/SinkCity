package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityChunk_CityResident_Row;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityChunk_CityResident_Table extends AbstractTable<CityChunk_CityResident_Row> {

    public static final String TABLE_NAME = "rel_citychunk_cityresident";

    public CityChunk_CityResident_Table(Database db) {
        super(TABLE_NAME, db);
    }

}
