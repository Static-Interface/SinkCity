package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityChunkTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CityChunkSettingRow implements Row {

    @Column
    @ForeignKey(table = CityChunkTable.class, column = "chunkId")
    public Integer chunkId;

    @Column(primaryKey = true, keyLength = 255)
    public String settingName;

    @Column(keyLength = 255)
    public String settingValue;

}
