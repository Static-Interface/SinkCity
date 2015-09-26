package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityChunkTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityChunkSettingRow implements Row {

    @Column
    @ForeignKey(table = CityChunkTable.class, column = "coordinateX")
    public Integer coordinateX;

    @Column
    @ForeignKey(table = CityChunkTable.class, column = "coordinateZ")
    public Integer coordinateZ;

    @Column
    @ForeignKey(table = CityChunkTable.class, column = "worldId")
    public String worldId;

    @Column(primaryKey = true)
    public String settingName;

    @Column
    public String settingValue;

}
