package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityRankTable;
import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityRank_City_Row implements Row {

    @Column(primaryKey = true)
    @ForeignKey(table = CityRankTable.class, column = "rankId")
    public Integer rankId;

    @Column(primaryKey = true)
    @ForeignKey(table = CityTable.class, column = "cityId")
    public Integer cityId;

}
