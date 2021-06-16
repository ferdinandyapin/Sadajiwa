package sadajiwa.panganventory.ui.add_inventory


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivityAddBinding
import sadajiwa.panganventory.model.AddDateChild
import sadajiwa.panganventory.ui.main.MainActivity
import java.text.SimpleDateFormat
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
        val email = intent.getStringExtra("email")
        if (email != null) {
            Log.d("isemailhere?",email)
        }


        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        with(binding){
            addname.text = resultFruit
            addtodaydate.text = currentDate

            if(url != null) {
                Glide.with(this@AddActivity)
                    .load(url.toString())
                    .override(400,250)
                    .centerCrop()
                    .into(photo)
            }
        }

        binding.finishButton.setOnClickListener{
            val expDate = binding.addexpdate.getText().toString()
            val alertDate = binding.addalertdate.getText().toString()
            addFruit(resultFruit,alertDate,expDate,email,currentDate)
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }
        binding.back.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }

    }

    private fun addFruit(
        resultFruit: String?,
        alertDate: String,
        expDate: String,
        email: String?,
        currentDate: String
    ) {
        val add = FirebaseDatabase.getInstance("https://machinelearning-313314-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User/$email/Data/$currentDate/$resultFruit")
        val addDateChild = AddDateChild(
            expDate = expDate,
            alertDate = alertDate
        )
        add.setValue(addDateChild)
    }
}