package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityChunkTable;
import de.static_interface.sinkcity.database.tables.CityResident_City_Table;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityChunk_CityResident_Row implements Row {

    @Column(primaryKey = true)
    @ForeignKey(table = CityChunkTable.class, column = "coordinateX")
    public Integer coordinateX;

    @Column(primaryKey = true)
    @ForeignKey(table = CityChunkTable.class, column = "coordinateZ")
    public Integer coordinateZ;

    @Column(primaryKey = true)
    @ForeignKey(table = CityChunkTable.class, column = "worldId")
    public String worldId;

    @Column(primaryKey = true)
    @ForeignKey(table = CityResident_City_Table.class, column = "userId")
    public String userId;

}
