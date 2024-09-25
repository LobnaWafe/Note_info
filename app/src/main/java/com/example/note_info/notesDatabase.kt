package com.example.note_info

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import androidx.core.database.getBlobOrNull
import java.lang.ClassCastException

class notesDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object
    {
        private val DATABASE_NAME="Notes.db"
        private val DATABASE_VERSION=7
        private val TABLE_FOLDER = "table_folder"
        private val COLUMEN_FOLDER_NAME="folder_name"
        private val COLUMEN_FOLDER_ID="folder_id"
        private val TABLE_NOTES="table_notes"
        private val COLUMEN_NOTE_CONTENT="note_content"
        private val COLUMEN_NOTE_F="note_f"
        private val COLUMEN_NOTE_TITEL="note_title"
        private val COLUMEN_NOTE_ID="note_id"
        private val TABLE_TASK="table_task"
        private val COLUMEN_TASK_ID="task_id"
        private val COLUMEN_TASK_CONTENT="task_content"
        private val COLUMN_TASK_CHECK="task_check"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableFolder = """
            CREATE TABLE $TABLE_FOLDER (
                $COLUMEN_FOLDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMEN_FOLDER_NAME TEXT
            )
        """.trimIndent()

        val createTableNotes = """
            CREATE TABLE $TABLE_NOTES (
            $COLUMEN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMEN_NOTE_F INTEGER,
                $COLUMEN_NOTE_TITEL TEXT,
                $COLUMEN_NOTE_CONTENT TEXT,
                FOREIGN KEY($COLUMEN_NOTE_F) REFERENCES $TABLE_FOLDER($COLUMEN_FOLDER_ID)
            )
        """.trimIndent()

        val createTableTask="""
            CREATE TABLE $TABLE_TASK($COLUMEN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMEN_TASK_CONTENT TEXT,$COLUMN_TASK_CHECK BOOLEAN)
        """.trimIndent()

        db?.execSQL(createTableFolder)
        db?.execSQL(createTableNotes)
        db?.execSQL(createTableTask)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableFolder= "DROP TABLE IF EXISTS $TABLE_FOLDER"
        val dropTableNotes= "DROP TABLE IF EXISTS $TABLE_NOTES"
        val dropTableTask ="DROP TABLE IF EXISTS $TABLE_TASK"
        db?.execSQL(dropTableFolder)
        db?.execSQL(dropTableNotes)
        db?.execSQL(dropTableTask)
        onCreate(db)
    }
 fun insertFolderName(name:String)
 {
     val db = writableDatabase
     val values=ContentValues().apply{
         put(COLUMEN_FOLDER_NAME,name)
  }
     db.insert(TABLE_FOLDER,null,values)
     db.close()
 }
    fun insertNotes(f_id:Int,title:String,content:String)
    {
        val db =writableDatabase
        val values = ContentValues().apply {
            put(COLUMEN_NOTE_F , f_id)
            put(COLUMEN_NOTE_TITEL,title)
            put(COLUMEN_NOTE_CONTENT,content)
        }
        db.insert(TABLE_NOTES,null,values)
        db.close()
    }
    fun insertTask(content:String)
    {
        val db=writableDatabase
        val values = ContentValues().apply {
            put(COLUMEN_TASK_CONTENT ,content)
            put(COLUMN_TASK_CHECK,0)
        }
        db.insert(TABLE_TASK,null,values)
        db.close()
    }
    fun readFolders():ArrayList<folder_list>
    {
        val folders=ArrayList<folder_list>()
        val db=readableDatabase
        val query = "SELECT * FROM $TABLE_FOLDER"
        val curser=db.rawQuery(query,null)
        while(curser.moveToNext())
        {
            val id=curser.getInt(curser.getColumnIndexOrThrow(COLUMEN_FOLDER_ID))
            val name = curser.getString(curser.getColumnIndexOrThrow(COLUMEN_FOLDER_NAME))
            val folder=folder_list(id,name)
            folders.add(folder)
        }
        curser.close()
        db.close()
        return folders
    }

    fun readNotes(id: Int): ArrayList<notes_list> {
        val notes = ArrayList<notes_list>()
        val db = readableDatabase

        // Adjusted query to select notes with a specific f_id
        val query = "SELECT * FROM $TABLE_NOTES WHERE $COLUMEN_NOTE_F = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        while (cursor.moveToNext()) {
            val id=cursor.getInt((cursor.getColumnIndexOrThrow(COLUMEN_NOTE_ID)))
            val f_id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMEN_NOTE_F))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMEN_NOTE_TITEL))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMEN_NOTE_CONTENT))
            val note = notes_list(id,f_id, title, content)
            notes.add(note)
        }

        cursor.close()
        db.close()
        return notes
    }

    fun readTask(): ArrayList<daily_task_list> {
        val Tasks = ArrayList<daily_task_list>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_TASK"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMEN_TASK_ID))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMEN_TASK_CONTENT))
            // Retrieve as integer and convert to boolean
            val check = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_CHECK)) == 1
            val task = daily_task_list(id, content, check)
            Tasks.add(task)
        }
        cursor.close()
        db.close()
        return Tasks
    }

    fun edit_folder_name(folder:folder_list)
    {
        val db=writableDatabase
        val values= ContentValues().apply {
            put(COLUMEN_FOLDER_NAME,folder.folder_name)
        }
        val whereCluase="$COLUMEN_FOLDER_ID=?"
        val whereArges= arrayOf(folder.Id.toString())
        db.update(TABLE_FOLDER,values,whereCluase,whereArges)
        db.close()
    }
    fun edit_note(note:notes_list)
    {
        val db=writableDatabase
        val values= ContentValues().apply {
            put(COLUMEN_NOTE_TITEL,note.title)
            put(COLUMEN_NOTE_CONTENT,note.content)

        }
        val whereCluase="$COLUMEN_NOTE_ID=?"
        val whereArges= arrayOf(note.id.toString())
        db.update(TABLE_NOTES,values,whereCluase,whereArges)
        db.close()
    }
    fun edit_task(id:Int,check:Boolean)
    {
        val db = writableDatabase
        val values=ContentValues().apply {
            put(COLUMN_TASK_CHECK,check)
        }
        val whereCluase="$COLUMEN_TASK_ID=?"
        val whereArges= arrayOf(id.toString())
        db.update(TABLE_TASK,values,whereCluase,whereArges)
        db.close()
    }

    fun delete_folder(id:Int)
    {
        val db=writableDatabase
        val notewhereCluase="$COLUMEN_NOTE_F=?"
        val notewhereArges= arrayOf(id.toString())
        db.delete(TABLE_NOTES,notewhereCluase,notewhereArges) // delete notes in folder

        val whereCluase="$COLUMEN_FOLDER_ID=?"
        val whereArges= arrayOf(id.toString())
        db.delete(TABLE_FOLDER,whereCluase,whereArges)
        db.close()
    }

    fun delete_note(id:Int)
    {
        val db=writableDatabase
        val whereCluase="$COLUMEN_NOTE_ID=?"
        val whereArges= arrayOf(id.toString())
        db.delete(TABLE_NOTES,whereCluase,whereArges)
        db.close()
    }
    fun delete_task(id:Int)
    {
        val db = writableDatabase
        val whereCluase="$COLUMEN_TASK_ID=?"
        val whereArges= arrayOf(id.toString())
        db.delete(TABLE_TASK,whereCluase,whereArges)
        db.close()
    }

}