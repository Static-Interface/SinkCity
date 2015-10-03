package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinklibrary.database.CascadeAction;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;
import de.static_interface.sinklibrary.database.annotation.UniqueKey;

public class CityChunkRow implements Row {

    @Column(primaryKey = true, autoIncrement = true)
    public Integer chunkId;

    @UniqueKey(combinationId = 1)
    @Column
    public Integer coordinateX;

    @UniqueKey(combinationId = 1)
    @Column
    public Integer coordinateZ;

    @UniqueKey(combinationId = 1)
    @Column
    public String worldId;

    @Column(keyLength = 36)
    @ForeignKey(table = CityTable.class, column = "cityId", onDelete = CascadeAction.CASCADE)
    public String cityId;

}
