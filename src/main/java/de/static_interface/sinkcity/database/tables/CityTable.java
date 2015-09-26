package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityRow;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityTable extends AbstractTable<CityRow> {

    public static final String TABLE_NAME = "city";

    public CityTable(Database db) {
        super(TABLE_NAME, db);
    }

}
