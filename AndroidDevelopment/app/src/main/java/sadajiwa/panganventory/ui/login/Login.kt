package sadajiwa.panganventory.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivityLoginBinding
import sadajiwa.panganventory.ui.main.MainActivity
import kotlin.math.log

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

        binding.loginbutton.setOnClickListener {
            checkLogin()
        }

    }

    private fun login() {
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

    private fun checkLogin() {
        if (binding.loginemail.text.toString().isEmpty()) {
            binding.loginemail.error = "Please enter email"
            binding.loginemail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.loginemail.text.toString()).matches()) {
            binding.loginemail.error = "Please enter valid email"
            binding.loginemail.requestFocus()
            return
        }
        if (binding.loginpass.text.toString().isEmpty()) {
            binding.loginpass.error = "Please enter password"
            binding.loginpass.requestFocus()
            return
        }
        login()
    }
}