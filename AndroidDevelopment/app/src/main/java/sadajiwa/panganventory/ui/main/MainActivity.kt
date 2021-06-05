package sadajiwa.panganventory.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivityMainBinding
import sadajiwa.panganventory.ui.notifications.NotificationActivity
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imguri: Uri
    private lateinit var mediaType: MediaType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imguri = data.data!!
                    val rename = UUID.randomUUID().toString()
                    val storage =
                        FirebaseStorage.getInstance("gs://machinelearning-313314.appspot.com")
                            .getReference("pic/$rename")
                    storage.putFile(imguri)
                        .addOnSuccessListener {
                            Log.d("upload to storage", "success")
                            postjson(rename)
                            //if need url
                            //storage.downloadUrl.addOnSuccessListener {
                            // Log.d("url",it.toString())
                            // postjson(it.toString())
                            // }
                        }

                    //Intent(this, AddInventoryActivity::class.java).also {
                    // startActivity(it)
                    // }
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postjson(url: String) {
        val jsonObject = JSONObject()
        jsonObject.put("name", url)
        mediaType = "application/json; charset=utf-8".toMediaType()
        val okHttpClient = OkHttpClient()
        val text = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://34.134.36.226:8080/post")
            .post(text)
            .build()

        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d("database insert status", response.body!!.string())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_notifications -> {
                Intent(this, NotificationActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}