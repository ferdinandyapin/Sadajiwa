package sadajiwa.panganventory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

import sadajiwa.panganventory.R
import sadajiwa.panganventory.adapter.DetailAdapter
import sadajiwa.panganventory.databinding.ActivityDetail2Binding
import sadajiwa.panganventory.model.ModelDetail

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetail2Binding
    private lateinit var rvDetail : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //get date
        val username = intent.getStringExtra("email").toString()
        Log.d("DAYOFUSERNAME",username)

        val detaildate = intent.getStringExtra("datedetail").toString()
        Log.d("DAYOFDETAIL",detaildate)

        binding.dateDetail.text = detaildate

        //get from database
        var alldetail = ArrayList<ModelDetail>()
        val ref: DatabaseReference = FirebaseDatabase.getInstance("https://machinelearning-313314-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User/$username/Data/08-06-2021")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        val detail = ModelDetail()
                        detail.name = item.key.toString()
                        Log.d("NAME",item.key.toString())
                        detail.exp = item.child("expDate").value.toString()
                        Log.d("EXP",item.child("expDate").value.toString())
                        detail.last = item.child("alertDate").value.toString()
                        Log.d("ALERT",item.child("alertDate").value.toString())
                        alldetail.add(detail)
                        showRecyclerList(alldetail)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun showRecyclerList(alldetail:ArrayList<ModelDetail>) {
        rvDetail = findViewById(R.id.rv_detail)
        rvDetail.layoutManager = LinearLayoutManager(this)
        val listAdapter = DetailAdapter(this,alldetail)
        rvDetail.adapter = listAdapter
    }
}