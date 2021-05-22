package sadajiwa.panganventory.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sadajiwa.panganventory.data.source.DataPerDateEntity
import sadajiwa.panganventory.databinding.ItemRvMainBinding
import sadajiwa.panganventory.ui.detail.DetailActivity

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val listPerDate = ArrayList<DataPerDateEntity>()

    fun setItem(item : List<DataPerDateEntity>? ) {
        if (item == null) return
        listPerDate.clear()
        listPerDate.addAll(item)
    }


    class MainViewHolder(private val binding: ItemRvMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : DataPerDateEntity) {
            with(binding) {
                rvDate.text = item.date
                rvArrow.setOnClickListener{
                    val intent = Intent(rvArrow.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_ITEM, item.date)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        val binding = ItemRvMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
        val item = listPerDate[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listPerDate.size
}