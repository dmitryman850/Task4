package dmitry.man.task4recyclerview

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(
        context, DATABASE_NAME, null, SCHEMA) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE cars (" + COLUMN_ID +
                " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME +
                " TEXT NOT NULL, " + COLUMN_YEAR + " INTEGER NOT NULL, " + COLUMN_PRICE +
                " INTEGER NOT NULL);")
        // adding initial data
        db.execSQL(
            "INSERT INTO " + TABLE + " (" + COLUMN_NAME + ", " +
                COLUMN_YEAR + ", " + COLUMN_PRICE + ") VALUES ('Lancer Evo 9', 2007, 20000);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "car.db" // Name db
        private const val SCHEMA = 1 // Version db
        const val TABLE = "cars" // Name table at db

        // name columns
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_YEAR = "year"
        const val COLUMN_PRICE = "price"
    }
}
