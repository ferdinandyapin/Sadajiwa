package sadajiwa.panganventory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import sadajiwa.panganventory.R
import sadajiwa.panganventory.model.ModelDetail

class DetailAdapter(private val listDetail: ArrayList<ModelDetail>) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    class DetailViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var dimage: ImageView = itemView.findViewById(R.id.img_poster)
        var dtitle: TextView = itemView.findViewById(R.id.tv_item_title)
        var dlast: TextView = itemView.findViewById(R.id.tv_item_last_for)
        var dexp: TextView = itemView.findViewById(R.id.tv_item_exp_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_detail,parent,false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val details = listDetail[position]
        holder.dtitle.text = details.name
        holder.dlast.text = details.last
        holder.dexp.text = details.exp
        Glide.with(holder.itemView.context)
            .load(details.image)
            .apply(RequestOptions().override(100,100))
            .into(holder.dimage)
    }

    override fun getItemCount(): Int {
       return listDetail.size
    }

}