package sadajiwa.panganventory.ui.notifications

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import sadajiwa.panganventory.adapter.NotificationAdapter
import sadajiwa.panganventory.databinding.ActivityNotificationBinding
import sadajiwa.panganventory.jobscheduler.NotificationAlarmManager
import sadajiwa.panganventory.model.ModelNotif

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var alarmReceiver: NotificationAlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var allNotif = ArrayList<ModelNotif>()
        showRecyclerView(allNotif)

        val username = intent.getStringExtra("email").toString()
        Log.d("DAYOFUSERNAME", username)

        val detaildate = intent.getStringExtra("datedetail").toString()
        Log.d("DAYOFDETAIL", detaildate)

        alarmReceiver = NotificationAlarmManager()
        alarmReceiver.getNotificationFromDb(this, detaildate)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showRecyclerView(data: ArrayList<ModelNotif>) {
        with(binding.rvDateNotif) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            notificationAdapter = NotificationAdapter(context, data)
            this.adapter = notificationAdapter
        }
    }
}