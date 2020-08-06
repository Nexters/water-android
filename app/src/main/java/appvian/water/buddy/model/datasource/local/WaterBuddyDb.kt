package appvian.water.buddy.model.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import appvian.water.buddy.model.data.Intake

@Database(entities = [Intake::class], version = 1)
abstract class WaterBuddyDb : RoomDatabase() {

    abstract fun intakeDao(): intakeDao

    companion object {
        private var instance: WaterBuddyDb? = null

        fun getInstance(context: Context): WaterBuddyDb? {
            if (instance == null) {
                synchronized(WaterBuddyDb::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WaterBuddyDb::class.java, "intake"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}