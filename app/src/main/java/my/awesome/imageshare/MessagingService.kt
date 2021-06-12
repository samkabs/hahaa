package my.awesome.imageshare

import android.content.Intent
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {

        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Toast.makeText(applicationContext, "from ${remoteMessage.from}\n Body ${remoteMessage.notification!!.body} ", Toast.LENGTH_LONG)
            .show()
        val intent= Intent(this@MessagingService,ShowActivity::class.java)
        intent.putExtra("new_message",remoteMessage.notification?.body)
        startActivity(intent)

        super.onMessageReceived(remoteMessage)
    }
}
