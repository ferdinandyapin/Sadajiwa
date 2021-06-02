package sadajiwa.panganventory.ui.add_inventory

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import sadajiwa.panganventory.databinding.ActivityAddInventoryBinding
import sadajiwa.panganventory.ui.detail.DetailActivity

class AddInventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            addMoreButton.setOnClickListener {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }

            finishButton.setOnClickListener {

                Intent(this@AddInventoryActivity, DetailActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}