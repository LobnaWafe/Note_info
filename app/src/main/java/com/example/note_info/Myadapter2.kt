import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.note_info.R
import com.example.note_info.UpdateActivity
import com.example.note_info.folder_list
import com.example.note_info.notesDatabase

import com.example.note_info.notes_list

class Myadapter2(private val context:Context,private var Noteslist: ArrayList<notes_list>) : RecyclerView.Adapter<Myadapter2.MyViewHolder>() {
    private val db: notesDatabase = notesDatabase(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notes, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return Noteslist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = Noteslist[position]
        holder.note_title.text = currentItem.title
        holder.note_content.text=currentItem.content
       holder.edit_note_btn.setOnClickListener{

          var intent=Intent(holder.itemView.context,UpdateActivity::class.java)
           intent.putExtra("NoteTitle",currentItem.title)
           intent.putExtra("NoteContent",currentItem.content)
           intent.putExtra("ID",currentItem.id)
           intent.putExtra("F_ID",currentItem.f_id)
           holder.itemView.context.startActivity(intent)
       }


        holder.delete_note_btn.setOnClickListener{
         db.delete_note(currentItem.id)
            Noteslist.remove(currentItem)
            setlist(Noteslist)
        }


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var note_title: TextView = itemView.findViewById(R.id.noteTitle)
        var note_content:TextView=itemView.findViewById(R.id.noteContent)
        var edit_note_btn:ImageView=itemView.findViewById(R.id.editNote)
        var delete_note_btn:ImageView=itemView.findViewById(R.id.deleteNote)

    }

    fun setlist(notelist: ArrayList<notes_list>) {
        this.Noteslist = notelist
        notifyDataSetChanged()
    }
}