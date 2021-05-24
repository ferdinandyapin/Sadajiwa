package sadajiwa.panganventory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sadajiwa.panganventory.R
import sadajiwa.panganventory.model.ModelDate

class HomeAdapter(private val listDate: ArrayList<ModelDate>) : RecyclerView.Adapter<HomeAdapter.DateViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallBack (onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class DateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var vdate: TextView = itemView.findViewById(R.id.rv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_main,parent,false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val datee = listDate[position]
        holder.vdate.text = datee.date
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listDate[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listDate.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ModelDate)
    }

}