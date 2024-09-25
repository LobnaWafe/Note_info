package com.example.note_info

import Myadapter
import Myadapter2
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
 lateinit var newArrayListNotes: ArrayList<notes_list>
lateinit var myadapter: Myadapter2
class NotesActivity : AppCompatActivity() {

  private lateinit var newRecyclerView: RecyclerView

  private lateinit var db:notesDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes)
        Initial()
       // var ID=intent.extras?.getInt("ID")
        var Addbtn:FloatingActionButton=findViewById(R.id.add_button_notes)
        Addbtn.setOnClickListener {
            var intent=Intent(this@NotesActivity,AddActivity::class.java)
            startActivity(intent)

        }
        disribution()
    }
    fun Initial()
    {
        db=notesDatabase(this)
        newArrayListNotes=ArrayList()
        newRecyclerView=findViewById(R.id.recyclerViewNotes)
        newRecyclerView.layoutManager=LinearLayoutManager(this)
    }
   fun disribution()
   {
      addArray()
       myadapter =Myadapter2(this,newArrayListNotes)
       newRecyclerView.adapter=myadapter

   }
    fun addArray()
    {
        newArrayListNotes.clear()
        newArrayListNotes=db.readNotes(ID)

    }
}