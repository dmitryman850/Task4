package dmitry.man.task4recyclerview

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class UserActivity : AppCompatActivity() {

    private lateinit var nameBox: EditText
    private lateinit var yearBox: EditText
    private lateinit var priceBox: EditText

    private lateinit var delButton: Button
    private lateinit var saveButton: Button

    private lateinit var sqlHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var userCursor: Cursor
    private var userId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        nameBox = findViewById(R.id.name)
        yearBox = findViewById(R.id.year)
        priceBox = findViewById(R.id.price)
        delButton = findViewById(R.id.deleteButton)
        saveButton = findViewById(R.id.saveButton)
        sqlHelper = DatabaseHelper(this)
        db = sqlHelper.writableDatabase
        val extras = intent.extras
        if (extras != null) {
            userId = extras.getLong("id")
        }
        if (userId > 0) {
            //getting an element by ID from the DB
            userCursor = db.rawQuery(
                "select * from " + DatabaseHelper.TABLE + " where " +
                        DatabaseHelper.COLUMN_ID + "=?", arrayOf(userId.toString())
            )
            userCursor.moveToFirst()
            nameBox.setText(userCursor.getString(1))
            yearBox.setText(userCursor.getInt(2).toString())
            priceBox.setText(userCursor.getInt(3).toString())
            userCursor.close()
        } else {
            //hiding the delete button
            delButton.visibility = View.GONE
        }
    }

    fun save(view: View?) {
        if (nameBox.text.toString() == "" || yearBox.text.toString() == "" ||
                priceBox.text.toString() == "") {
            val toast = Toast.makeText(applicationContext, "Введите данные", Toast.LENGTH_SHORT).show()
        } else {
            val cv = ContentValues()
            cv.put(DatabaseHelper.COLUMN_NAME, nameBox.text.toString())
            cv.put(DatabaseHelper.COLUMN_YEAR, yearBox.text.toString().toInt())
            cv.put(DatabaseHelper.COLUMN_PRICE, priceBox.text.toString().toInt())
            if (userId > 0) {
                db.update(
                    DatabaseHelper.TABLE,
                    cv,
                    DatabaseHelper.COLUMN_ID + "=" + userId.toString(),
                    null
                )
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv)
            }
            goHome()
        }
    }

    fun delete(view: View?) {
        db.delete(DatabaseHelper.TABLE, "_id = ?", arrayOf(userId.toString()))
        goHome()
    }

    private fun goHome() {
        //close the connection
        db.close()
        //go to the main activity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
}