package sadajiwa.panganventory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sadajiwa.panganventory.databinding.ActivityDetail2Binding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetail2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}