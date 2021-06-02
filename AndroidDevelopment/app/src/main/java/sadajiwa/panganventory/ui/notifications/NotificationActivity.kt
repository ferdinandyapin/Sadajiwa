package sadajiwa.panganventory.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sadajiwa.panganventory.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}