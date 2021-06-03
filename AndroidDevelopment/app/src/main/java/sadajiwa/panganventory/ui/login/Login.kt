package sadajiwa.panganventory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivityLoginBinding
import sadajiwa.panganventory.ui.main.MainActivity

class Login : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Login"

        binding.registertext.setOnClickListener{
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

    }

    fun login(view: View) {
        with(binding) {
            val email = binding.loginemail.text.toString()
            val password = binding.loginpass.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Intent(this@Login, MainActivity::class.java).also {
                        startActivity(it)
                    }
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}