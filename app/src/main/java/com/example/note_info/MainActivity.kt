package com.example.note_info

import Myadapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
//lateinit var newArrayList: ArrayList<folder_list>
 var ID:Int=0
class MainActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<folder_list>
    private lateinit var db:notesDatabase
    private lateinit var myadapter: Myadapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        Initial()

        val addButton: FloatingActionButton = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            showAddNoteDialog()
        }

        setupBottomNavigation()
        distribution()

    }
    private fun Initial()
    {
        db=notesDatabase(this)
        newArrayList = ArrayList()

        newRecyclerView = findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
    }

private fun distribution(){

addArray()
    newRecyclerView.adapter = Myadapter(this,newArrayList)
   myadapter =Myadapter(this,newArrayList)
    newRecyclerView.adapter=myadapter
    myadapter.onItemClickListner(object:Myadapter.onItemClickListner{
        override fun onItemClick(position: Int) {
            var intent = Intent(this@MainActivity,NotesActivity::class.java)
           // intent.putExtra("ID",newArrayList[position].Id)
            ID=newArrayList[position].Id
            startActivity(intent)
        }
    })
}
    private fun showAddNoteDialog() {
        // Inflate the dialog view
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_activity2, null)
        val noteTitleEditText: EditText = dialogView.findViewById(R.id.note_add)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Add Note")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val noteTitle = noteTitleEditText.text.toString()
                // Handle adding the note title (e.g., save it to your list)
                if(noteTitle.isNotEmpty())
                {
                    //add to database
                    db.insertFolderName(noteTitle)
                    addArray()
                    myadapter.setlist(newArrayList)

                }
                else
                {
                    Toast.makeText(this, "inValid", Toast.LENGTH_SHORT).show()

                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancle") { dialog, _ ->
                dialog.dismiss()
            }

        // Show the dialog
        dialogBuilder.create().show()
    }
    private fun addArray()
    {
        newArrayList.clear()
        newArrayList=db.readFolders()

       /* for (folder in newArrayList) {
            Log.d("Folder ID", folder.Id.toString())
        }*/
    }

    private fun setupBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_butoon)
        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    // Stay on MainActivity (optional: show a toast)
                    true // return true to indicate the item selection was handled
                }
                R.id.dailytask -> {
                    // Navigate to DailyTaskActivity
                    val intent = Intent(this, DailyTaskActivity::class.java)
                    startActivity(intent)
                    finish()
                    true // return true to indicate the item selection was handled
                }
                // Handle other menu items if any
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Set the selected item to home when resuming this activity
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_butoon)
        bottomNavigation.menu.findItem(R.id.home).isChecked = true
    }

}