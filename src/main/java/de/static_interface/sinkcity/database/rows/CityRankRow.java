package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityRankRow implements Row {

    @Column(primaryKey = true, autoIncrement = true)
    public Integer rankId;

    @Column
    @ForeignKey(table = CityTable.class, column = "cityId")
    public String cityId;

    @Column
    public String rankName;

}
