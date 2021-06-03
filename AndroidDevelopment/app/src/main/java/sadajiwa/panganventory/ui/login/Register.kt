package sadajiwa.panganventory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import sadajiwa.panganventory.R

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val Login: TextView = findViewById(R.id.logintext)
        Login.setOnClickListener{
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
    }
}
}