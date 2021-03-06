package dmitry.man.task4recyclerview

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import dmitry.man.task4recyclerview.DatabaseHelper.Companion.COLUMN_NAME
import dmitry.man.task4recyclerview.DatabaseHelper.Companion.COLUMN_PRICE
import dmitry.man.task4recyclerview.DatabaseHelper.Companion.COLUMN_YEAR

class MainActivity : AppCompatActivity() {
    private lateinit var userList: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var userCursor: Cursor
    private lateinit var userAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList = findViewById(R.id.list)

        userList.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val intent = Intent(applicationContext, UserActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        databaseHelper = DatabaseHelper(applicationContext)
    }

    public override fun onResume() {
        super.onResume()
        // open the connection
        db = databaseHelper.readableDatabase
        // getting SharedPreferences that works with the settings file
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // getting data from db as a cursor, depending on key
        when (prefs.getString("sortList", "Модель")) {
            "Модель" -> userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE +
                    " ORDER BY " + COLUMN_NAME, null)
            "Год" -> userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE +
                    " ORDER BY " + COLUMN_YEAR, null)
            "Цена" -> userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE +
                    " ORDER BY " + COLUMN_PRICE, null)
        }
        // determining which columns from the cursor will be output to ListView
        val headers =
            arrayOf(COLUMN_NAME, COLUMN_YEAR, COLUMN_PRICE)
        // creating an adapter and passing the cursor to it
        userAdapter = SimpleCursorAdapter(this, R.layout.list_item,
            userCursor, headers, intArrayOf(R.id.text1, R.id.text2, R.id.text3), 0
        )
        userList.adapter = userAdapter
    }
    // click on the button to launch UserActivity to add data
    fun add(view: View?) {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }
    // click on the button to launch SettingsActivity
    fun sort(view: View?) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    public override fun onDestroy() {
        super.onDestroy()
        // closing the connection and cursor
        db.close()
        userCursor.close()
    }
}
