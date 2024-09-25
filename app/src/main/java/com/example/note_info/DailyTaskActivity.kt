package com.example.note_info

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DailyTaskActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<daily_task_list>
    private lateinit var db:notesDatabase
    private lateinit var myadapter: Myadapter3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_task)
        Initial()
        val addBtn:FloatingActionButton = findViewById(R.id.add_button_task_daily)
        addBtn.setOnClickListener {
            showAddNoteDialog()
        }
        setupBottomNavigation()
disribution()
    }
    private fun Initial()
    {
        db=notesDatabase(this)
        newArrayList = ArrayList()

        newRecyclerView = findViewById(R.id.recyclerView2)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    fun disribution()
    {

        // swipe to delete
        val swipeItem=object:Swipe(this,0,ItemTouchHelper.LEFT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myadapter.deleteItem(viewHolder.adapterPosition)
            }
        }
      val touchHelper=ItemTouchHelper(swipeItem)
        touchHelper.attachToRecyclerView(newRecyclerView)

///////////////////////////////////////////////////////////////

        addArray()
        myadapter =Myadapter3(this,newArrayList)
        newRecyclerView.adapter=myadapter
    }

fun showAddNoteDialog(){
    val dailogView=LayoutInflater.from(this).inflate(R.layout.gialog_daily_task,null)
    val Taskadd:EditText=dailogView.findViewById(R.id.task_add)
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setTitle("Add Task")
        .setView(dailogView)
        .setPositiveButton("Save"){ dialog, _ ->
            val task=Taskadd.text.toString()
        if(task.isNotEmpty())
        {

            db.insertTask(Taskadd.text.toString())
            addArray()
            myadapter.setlist(newArrayList)
        }else
        {
            Toast.makeText(this,"inValid",Toast.LENGTH_SHORT).show()
        }
            dialog.dismiss()
           // Log.d("ID",newArrayList[newArrayList.size-1].id.toString())
    }
        .setNegativeButton("Cancle"){dialog,_ ->dialog.dismiss()}
    dialogBuilder.create().show()
}
    fun addArray()
    {
        newArrayList.clear()
        newArrayList=db.readTask()
    }
    private fun setupBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_butoon)
        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true // return true to indicate the item selection was handled
                }
                R.id.dailytask -> {

                    true // return true to indicate the item selection was handled
                }
                // Handle other menu items if any
                else -> false
            }
        }
    }
    override fun onResume() {
        super.onResume()
        // Set the selected item to daily tasks when resuming this activity
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_butoon)
        bottomNavigation.menu.findItem(R.id.dailytask).isChecked = true
    }

}