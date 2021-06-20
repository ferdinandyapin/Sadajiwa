package sadajiwa.panganventory.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import sadajiwa.panganventory.databinding.ActivityRegisterBinding
import sadajiwa.panganventory.model.AddDateChild
import sadajiwa.panganventory.ui.main.MainActivity

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logintext.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        binding.regbutton.setOnClickListener {
            checkRegisterUser()
        }
    }

    private fun register() {
        with(binding) {
            val email = regemail.text.toString()
            val password = regpass.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){

                    Intent(this@Register, MainActivity::class.java).also {
                        it.putExtra("email",auth.uid)
                        startActivity(it)
                    }
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkRegisterUser() {
        if (binding.regemail.text.toString().isEmpty()) {
            binding.regemail.error = "Please enter email"
            binding.regemail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.regemail.text.toString()).matches()) {
            binding.regemail.error = "Please enter valid email"
            binding.regemail.requestFocus()
            return
        }
        if (binding.regpass.text.toString().isEmpty()) {
            binding.regpass.error = "Please enter password"
            binding.regpass.requestFocus()
            return
        }
        register()
    }
}