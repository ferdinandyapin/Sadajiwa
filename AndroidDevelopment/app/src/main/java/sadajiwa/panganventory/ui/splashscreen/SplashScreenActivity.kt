package sadajiwa.panganventory.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import sadajiwa.panganventory.R
import sadajiwa.panganventory.databinding.ActivitySplashScreenBinding
import sadajiwa.panganventory.ui.login.Login


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            pvLogo.startAnimation(
                AnimationUtils.loadAnimation(
                    this@SplashScreenActivity,
                    R.anim.splash_in
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({
                pvLogo.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@SplashScreenActivity,
                        R.anim.splash_out
                    )
                )
                Handler(Looper.getMainLooper()).postDelayed({
                    pvLogo.visibility = View.GONE
                    startActivity(Intent(this@SplashScreenActivity, Login::class.java))
                    finish()
                }, 500)
            }, 1500)
        }

    }
}