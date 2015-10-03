package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityResident_City_Row implements Row {

    // This is a Java UUID (36 chars length)
    @Column(primaryKey = true, keyLength = 36)
    public String userId;

    @Column(keyLength = 36)
    @ForeignKey(table = CityTable.class, column = "cityId")
    public String cityId;

}
