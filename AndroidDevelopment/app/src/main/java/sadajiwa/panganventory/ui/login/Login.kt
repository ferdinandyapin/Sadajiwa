package sadajiwa.panganventory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import sadajiwa.panganventory.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val Register:TextView = findViewById(R.id.registertext)
        Register.setOnClickListener{
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }

    }
}