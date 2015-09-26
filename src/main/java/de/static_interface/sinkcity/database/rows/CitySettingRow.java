package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinkcity.database.tables.CityTable;
import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;
import de.static_interface.sinklibrary.database.annotation.ForeignKey;

public class CitySettingRow implements Row {

    @Column
    @ForeignKey(table = CityTable.class, column = "cityId")
    public String cityId;

    @Column
    public String settingName;

    @Column
    public String settingValue;

}
