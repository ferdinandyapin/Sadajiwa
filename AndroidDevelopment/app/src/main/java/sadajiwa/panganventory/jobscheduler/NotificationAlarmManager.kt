package sadajiwa.panganventory.jobscheduler

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import sadajiwa.panganventory.R
import sadajiwa.panganventory.model.ModelNotif
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationAlarmManager : BroadcastReceiver() {

    companion object {
        const val notifId = 101
        private const val DATE_FORMAT = "yyyy-MM-dd"
        const val EXTRA_USERNAME = "username"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let { showNotification(p0, notifId) }
    }

    fun getNotificationFromDb(context: Context, date: String, ) {
        if (isDateInvalid(date, DATE_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationAlarmManager::class.java)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val allNotif = ArrayList<ModelNotif>()
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance("https://machinelearning-313314-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User/$username/Data/08-06-2021")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val notif = ModelNotif()
                        notif.title = item.key.toString()
                        Log.d("TITLE", item.key.toString())
                        notif.exp = item.child("expDate").value.toString()
                        Log.d("EXP", item.child("expDate").value.toString())
                        notif.last = item.child("alertDate").value.toString()
                        Log.d("ALERT", item.child("alertDate").value.toString())
                        allNotif.add(notif)

                        val dateArray = notif.last.split("-").toTypedArray()
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
                        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))

                        val pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0)
                        alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    private fun showNotification(context: Context, notifId: Int) {
        val ID = "id"
        val NAME = "Job scheduler"

        val intent =
            context.packageManager.getLaunchIntentForPackage("sadajiwa.panganventory")
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, ID)
            .setContentIntent(pendingIntent)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setSmallIcon(R.drawable.panganventory_logo)
            .setContentText(context.resources.getString(R.string.app_notif))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(ID)

            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(notifId, notification)
    }


    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}