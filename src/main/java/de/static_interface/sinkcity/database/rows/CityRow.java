package de.static_interface.sinkcity.database.rows;

import de.static_interface.sinklibrary.database.Row;
import de.static_interface.sinklibrary.database.annotation.Column;

public class CityRow implements Row {

    @Column(primaryKey = true, keyLength = 36)
    public String cityId;

    @Column
    public Integer spawnX;

    @Column
    public Integer spawnY;

    @Column
    public Integer spawnZ;

    @Column
    public String spawnWorldId;

}
