package appvian.water.buddy.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intake")
data class Intake(
    @PrimaryKey(autoGenerate = false)
    val date: Long,

    @ColumnInfo(name = "category")
    val category: Int,

    @ColumnInfo(name = "amount")
    val amount: Int
) {
    constructor() : this(0L,-1,0)
}