package sadajiwa.panganventory.ui.main

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ButtonBarLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import sadajiwa.panganventory.R
import sadajiwa.panganventory.adapter.HomeAdapter
import sadajiwa.panganventory.databinding.ActivityMainBinding
import sadajiwa.panganventory.model.ModelDate
import sadajiwa.panganventory.ui.add_inventory.AddActivity
import sadajiwa.panganventory.ui.detail.DetailActivity
import sadajiwa.panganventory.ui.notifications.NotificationActivity
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imguri : Uri
    private lateinit var mediaType: MediaType
    private lateinit var rvDates : RecyclerView
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ambil data
        username = intent.getStringExtra("email").toString()
        Log.d("get username",username)
        if(username!=null)
        {
        GetData(username)
        }

        //kalo klik add
        binding.floatingActionButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun GetData(username: String) {
        var allDate = ArrayList<ModelDate>()
        val ref: DatabaseReference = FirebaseDatabase.getInstance("https://machinelearning-313314-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User/$username/Data")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (item in snapshot.children){
                        val date = ModelDate()
                        date.date = item.key.toString()
                        Log.d("DATA",item.key.toString())
                        allDate.add(date)
                        showRecyclerList(allDate)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun showRecyclerList(allDate: ArrayList<ModelDate>) {
        rvDates = findViewById(R.id.rv_date)
        rvDates.layoutManager = LinearLayoutManager(this)
        val listAdapter = HomeAdapter(this,allDate)
        rvDates.adapter = listAdapter

        listAdapter.setOnItemClickCallBack(object :HomeAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ModelDate) {
                getTwo(data.date)
            }
        })

    }

    private fun getTwo(date: String) {
        username = intent.getStringExtra("email").toString()
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("email",username)
        intent.putExtra("datedetail",date)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_CANCELED) {
            Log.d("requestCodee", requestCode.toString())

            when(requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        //progressDialog
                        val pd = ProgressDialog(this)
                        pd.setTitle("Uploading image..")
                        pd.show()

                        imguri = data.data!!
//                        imguri = data.get
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
                                //download url
                                storage.downloadUrl.addOnSuccessListener {
                                    val url = it.toString()
                                    Log.d("url",url)
                                    //post json
                                    postjson(renamejpg,url)
                                }
                            }
                    }
                } else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
                }
            }
        }
//        when(requestCode) {
//            REQUEST_IMAGE_CAPTURE -> {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    //progressDialog
//                    val pd = ProgressDialog(this)
//                    pd.setTitle("Uploading image..")
//                    pd.show()
//
//                    imguri = data.data!!
//                    val rename = UUID.randomUUID().toString()
//                    val renamejpg = "$rename.jpg"
//                    val storage = FirebaseStorage.getInstance("gs://ml-model-android").getReference("$renamejpg")
//                    storage.putFile(imguri)
//                        .addOnProgressListener { pl ->
//                            val progress =(100.00 * pl.bytesTransferred / pl.totalByteCount)
//                            pd.setMessage("Progress ${progress.toInt()}%")
//                        }
//                        .addOnSuccessListener {
//                            Log.d("upload to storage","success")
//                            //download url
//                            storage.downloadUrl.addOnSuccessListener {
//                                val url = it.toString()
//                                Log.d("url",url)
//                                //post json
//                                postjson(renamejpg,url)
//                            }
//                        }
//                }
//            } else -> {
//                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun postjson(rename: String, url: String) {
        val jsonObject = JSONObject()
        jsonObject.put("IMPATH",rename)
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
                val email = intent.getStringExtra("email")
                if (email != null) {
                    Log.d("getemailsuccess",email)
                }
                val sendFruit = Intent(this@MainActivity,AddActivity::class.java)
                sendFruit.putExtra("email",email)
                sendFruit.putExtra("resultFruit",resultFruit)
                sendFruit.putExtra("urlimage",url)
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
                val email = intent.getStringExtra("email")
                val intent = Intent(this, NotificationActivity::class.java)
                intent.putExtra("email",email)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}