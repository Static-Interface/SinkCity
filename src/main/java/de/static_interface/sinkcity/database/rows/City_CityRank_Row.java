package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityRankTable;
import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinklibrary.database.CascadeAction;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class City_CityRank_Row implements Row {

    @ForeignKey(table = CityTable.class, column = "cityId", onDelete = CascadeAction.CASCADE)
    @Column(keyLength = 36)
    public String cityId;

    @Column
    @ForeignKey(table = CityRankTable.class, column = "rankId")
    public Integer rankId;

}
