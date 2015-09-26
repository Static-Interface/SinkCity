package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityRankTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityRankPermissionRow implements Row {

    @Column(primaryKey = true, autoIncrement = true)
    public Integer rankPermissionId;

    @Column(primaryKey = true)
    @ForeignKey(table = CityRankTable.class, column = "rankId")
    public Integer rankId;

    @Column
    public String rankPermissionName;

}
