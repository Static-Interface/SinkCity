package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityChunkTable;
import de.static_interface.sinkcity.database.tables.CityResident_City_Table;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;
import de.static_interface.sinklibrary.database.annotation.UniqueKey;

public class CityChunk_CityResident_Row implements Row {

    @Column
    @UniqueKey(combinationId = 2)
    @ForeignKey(table = CityChunkTable.class, column = "chunkId")
    public Integer chunkId;

    // This is a Java UUID (36 chars length)
    @Column(keyLength = 36)
    @UniqueKey(combinationId = 2)
    @ForeignKey(table = CityResident_City_Table.class, column = "userId")
    public String userId;

}
