package com.example.note_info
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Myadapter3(private val context: Context, private var Tasklist:ArrayList<daily_task_list>) : RecyclerView.Adapter<Myadapter3.MyViewHolder>() {
    private val db: notesDatabase = notesDatabase(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_daily_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return Tasklist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = Tasklist[position]
        holder.task.text = currentItem.content

        // Initialize the checkbox state
        holder.checkbox.isChecked = currentItem.isChecked

        holder.checkbox.setOnClickListener {
            // Toggle the checked state
            currentItem.isChecked = !currentItem.isChecked

            // Update the database
            db.edit_task(currentItem.id, currentItem.isChecked)

            // Update the UI (if necessary)
            setlist(Tasklist)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
   var checkbox:CheckBox=itemView.findViewById(R.id.checkbox)
  var task:TextView=itemView.findViewById(R.id.text_task)

    }


    fun setlist(tasklist: ArrayList<daily_task_list>) {
        this.Tasklist = tasklist
        notifyDataSetChanged()
    }

    fun deleteItem(i:Int)
    {
        db.delete_task(Tasklist[i].id)
        Tasklist.removeAt(i)
        notifyDataSetChanged()
    }
}