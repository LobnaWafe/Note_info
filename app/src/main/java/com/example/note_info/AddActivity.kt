package com.example.note_info

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddActivity : AppCompatActivity() {
    private lateinit var db:notesDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        db = notesDatabase(this)
        var ID_note= ID
        var note_title:EditText=findViewById(R.id.note_title_enter)
        var note_content:EditText=findViewById(R.id.note_content_enter)
        var addbtn:ImageView=findViewById(R.id.doneBtn)

        addbtn.setOnClickListener {
            if(note_title.text.toString().isNotEmpty()&&note_content.text.toString().isNotEmpty())
            {
                db.insertNotes(ID_note,note_title.text.toString(),note_content.text.toString())
                newArrayListNotes.clear()
                newArrayListNotes=db.readNotes(ID_note)
                myadapter.setlist(newArrayListNotes)
                finish()
            }
            else
            {
                Toast.makeText(this,"inValid",Toast.LENGTH_SHORT).show()
            }
        }
    }

}