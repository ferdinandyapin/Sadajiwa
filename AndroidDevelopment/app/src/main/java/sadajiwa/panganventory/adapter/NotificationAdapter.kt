package sadajiwa.panganventory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView

import androidx.recyclerview.widget.RecyclerView
import sadajiwa.panganventory.R
import sadajiwa.panganventory.model.ModelNotif

class NotificationAdapter(context: Context, private val listNotif: ArrayList<ModelNotif>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var nimage: ImageView = itemView.findViewById(R.id.notif_image)
        var ntitle: TextView = itemView.findViewById(R.id.tv_title_notif)
        var nexp: TextView = itemView.findViewById(R.id.notif_exp_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = listNotif[position]
        with(holder) {
            ntitle.text = notification.title
            nexp.text = notification.exp
        }
    }

    override fun getItemCount(): Int = listNotif.size
}