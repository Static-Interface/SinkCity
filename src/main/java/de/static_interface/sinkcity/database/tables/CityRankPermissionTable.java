package de.static_interface.sinkcity.database.tables;

import de.static_interface.sinkcity.database.rows.CityRankPermissionRow;
import de.static_interface.sinklibrary.database.AbstractTable;
import de.static_interface.sinklibrary.database.Database;

public class CityRankPermissionTable extends AbstractTable<CityRankPermissionRow> {

    public static final String TABLE_NAME = "cityrankpermission";

    public CityRankPermissionTable(Database db) {
        super(TABLE_NAME, db);
    }

}
