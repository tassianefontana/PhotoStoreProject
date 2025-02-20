import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.sayurienterprise.photostore.R
import java.io.File

class ImageAdapter(
    private var files: List<String>,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filePath = files[position]
        val file = File(filePath)
        holder.fileNameTextView.text = file.name
        holder.itemView.setOnClickListener {
            onItemClickListener(filePath)
        }
    }

    override fun getItemCount(): Int = files.size

    fun updateList(newList: List<String>) {
        files = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView =
            itemView.findViewById(R.id.fileNameTextView)
    }

}