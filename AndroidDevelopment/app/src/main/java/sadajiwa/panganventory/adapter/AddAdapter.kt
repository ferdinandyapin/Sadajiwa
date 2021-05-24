package sadajiwa.panganventory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sadajiwa.panganventory.R
import sadajiwa.panganventory.model.ModelAdd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class AddAdapter(private val listAdd: ArrayList<ModelAdd>) : RecyclerView.Adapter<AddAdapter.AddViewHolder>() {

    class AddViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var atitle: TextView = itemView.findViewById(R.id.name_item_added)
        var aimage: ImageView = itemView.findViewById(R.id.image_item_added)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_added_item,parent,false)
        return AddViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        val adds = listAdd[position]
        holder.atitle.text = adds.name
        Glide.with(holder.itemView.context)
            .load(adds.image)
            .apply(RequestOptions().override(100,100))
            .into(holder.aimage)
    }

    override fun getItemCount(): Int {
        return listAdd.size
    }

}