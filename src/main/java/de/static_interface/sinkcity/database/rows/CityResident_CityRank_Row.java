package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityRankTable;
import de.static_interface.sinkcity.database.tables.CityResident_City_Table;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityResident_CityRank_Row implements Row {

    // This is a Java UUID (36 chars length)
    @Column(keyLength = 36)
    @ForeignKey(table = CityResident_City_Table.class, column = "userId")
    public String userId;

    @Column
    @ForeignKey(table = CityRankTable.class, column = "rankId")
    public Integer rankId;

}
