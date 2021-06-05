package sadajiwa.panganventory.ui.add_inventory

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivityAddBinding
import sadajiwa.panganventory.databinding.ActivityAddInventoryBinding
import sadajiwa.panganventory.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultFruit = intent.getStringExtra("resultFruit")
        if (resultFruit != null) {
            Log.d("isfruithere?",resultFruit)
        }
        val url = intent.getStringExtra("urlimage")
        if (url != null) {
            Log.d("isurlhere?",url)
        }
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        with(binding){
            addname.text = resultFruit
            addtodaydate.text = currentDate


            //belom bisa get gambar gatau kenapa
//            if(url != null) {
//                Glide.with(this@AddActivity)
//                    .load(url)
//                    .into(photo)
//            }
        }

    }
}