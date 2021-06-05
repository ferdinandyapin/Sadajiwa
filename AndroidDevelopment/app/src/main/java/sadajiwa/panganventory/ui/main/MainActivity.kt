package sadajiwa.panganventory.ui.main

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivityMainBinding
import sadajiwa.panganventory.model.ModelAdd
import sadajiwa.panganventory.ui.add_inventory.AddActivity
import sadajiwa.panganventory.ui.add_inventory.AddInventoryActivity
import sadajiwa.panganventory.ui.notifications.NotificationActivity
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imguri : Uri
    private lateinit var mediaType: MediaType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        

        binding.floatingActionButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    //progressDialog
                    val pd = ProgressDialog(this)
                    pd.setTitle("Uploading image..")
                    pd.show()

                    imguri = data.data!!
                    val rename = UUID.randomUUID().toString()
                    val renamejpg = "$rename.jpg"
                    val storage = FirebaseStorage.getInstance("gs://ml-model-android").getReference("$renamejpg")
                    storage.putFile(imguri)
                        .addOnProgressListener { pl ->
                            val progress =(100.00 * pl.bytesTransferred / pl.totalByteCount)
                            pd.setMessage("Progress ${progress.toInt()}%")
                        }
                        .addOnSuccessListener {
                            Log.d("upload to storage","success")
                            postjson(renamejpg)
                            //download url
                            storage.downloadUrl.addOnSuccessListener {
                                val url = it.toString()
                                Log.d("url",url)
                                val intent = Intent(this, AddActivity::class.java)
                                intent.putExtra("urlimage",url)
                                startActivity(intent)
                            }
                        }
                }
            } else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postjson(url: String) {
        val jsonObject = JSONObject()
        jsonObject.put("IMPATH",url)
        mediaType = "application/json; charset=utf-8".toMediaType()
        val okHttpClient = OkHttpClient()
        val text = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://34.126.145.22/predict")
            .post(text)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val response = response.body!!.string()
                Log.d("database insert status",response)

                val responseObject = JSONObject(response)
                val fruit = responseObject.getJSONObject("predict")
                val resultFruit = fruit.getString("Fruit")
                Log.d("getfruitsuccess",resultFruit)

                //send fruit to add activity
                val sendFruit = Intent(this@MainActivity,AddActivity::class.java)
                sendFruit.putExtra("resultFruit",resultFruit)
                startActivity(sendFruit)

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