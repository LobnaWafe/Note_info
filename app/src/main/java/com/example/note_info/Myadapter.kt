import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.note_info.R
import com.example.note_info.folder_list
import com.example.note_info.notesDatabase


class Myadapter(private val context:Context,private var Folderlist: ArrayList<folder_list>) : RecyclerView.Adapter<Myadapter.MyViewHolder>() {
    private val db: notesDatabase = notesDatabase(context)
    lateinit var mListener: onItemClickListner

    interface onItemClickListner {
        fun onItemClick(position: Int)
    }

    fun onItemClickListner(listener: onItemClickListner) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return Folderlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = Folderlist[position]
        holder.folder_text.text = currentItem.folder_name

        // Set click listener for folder_text
        holder.folder_text.setOnClickListener {
            mListener.onItemClick(position)
        }

        holder.editButoon.setOnClickListener{
            val dialogView = LayoutInflater.from(context).inflate(R.layout.add_activity2, null)
            val noteTitleEditText: EditText = dialogView.findViewById(R.id.note_add)
            noteTitleEditText.setText(currentItem.folder_name)

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(dialogView)
                .setTitle("Edit Name")
                .setPositiveButton("Save") { dialog, _ ->
                    // Handle saving changes

                   currentItem.folder_name=noteTitleEditText.text.toString()
                    setlist(Folderlist)
                    dialog.dismiss()
                    db.edit_folder_name(folder_list(currentItem.Id,currentItem.folder_name))
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            dialogBuilder.create().show()
        }

        holder.deleteButoon.setOnClickListener{
        db.delete_folder(currentItem.Id)
            Folderlist.remove(currentItem)
            setlist(Folderlist)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folder_text: TextView = itemView.findViewById(R.id.folder_text)
        var editButoon:ImageView=itemView.findViewById(R.id.edit_button)
        var deleteButoon:ImageView=itemView.findViewById(R.id.delete_button)

    }

    fun setlist(Folderlist: ArrayList<folder_list>) {
        this.Folderlist = Folderlist
        notifyDataSetChanged()
    }
}