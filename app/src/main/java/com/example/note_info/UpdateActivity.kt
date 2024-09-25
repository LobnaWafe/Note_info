package com.example.note_info

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UpdateActivity : AppCompatActivity() {
    private val db: notesDatabase = notesDatabase(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update)
       var title=intent.extras?.getString("NoteTitle")
        var content=intent.extras?.getString("NoteContent")
        val id = intent.extras?.getInt("ID") ?: -1
        val f_id = intent.extras?.getInt("F_ID") ?: -1

        var title_text:EditText=findViewById(R.id.note_title_up)
        var content_text:EditText=findViewById(R.id.note_content_up)
        var btnUp:ImageView=findViewById(R.id.doneBtnUp)

        title_text.setText(title)
        content_text.setText(content)

        btnUp.setOnClickListener {
            db.edit_note(notes_list(id,f_id,title_text.text.toString(),content_text.text.toString()))
            newArrayListNotes.firstOrNull { it.id==id }?.let{ newArrayListNotes ->
                newArrayListNotes.title=title_text.text.toString()
                newArrayListNotes.content=content_text.text.toString()
            }
            myadapter.setlist(newArrayListNotes)
            finish()
        }
    }
}