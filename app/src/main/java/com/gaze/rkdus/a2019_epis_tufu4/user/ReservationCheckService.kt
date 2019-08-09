package com.gaze.rkdus.a2019_epis_tufu4.user

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity.SERVER_URL
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity.TAG
import com.gaze.rkdus.a2019_epis_tufu4.R.drawable.temp
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReservationBackgroundService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.SplashActivity
import com.gaze.rkdus.a2019_epis_tufu4.popup.ManagementPopupActivity


class ReservationCheckService : Service() {
    private val delayHandler: Handler by lazy {
        Handler()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind start!")
        throw UnsupportedOperationException("Not yet")
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand start!")
        val backgroundService:ReservationBackgroundService = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .client(OkHttpClient())
                .build()
                .create(ReservationBackgroundService::class.java)

        var tempString: String? = null
        intent.let { if (intent!!.hasExtra("NICKNAME")) {
            tempString = intent.getStringExtra("NICKNAME")
            Log.e(TAG, "string : $tempString")
            }
        }
        // TODO: getStartReservation으로 사용자의 예약 데이터를 가져와 초기 설정.

        startScanning(backgroundService, tempString!!)

        return START_REDELIVER_INTENT
    }

    private fun getManageMessage() {
        var intent = Intent(this, ManagementPopupActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun waitScanning(backgroundService: ReservationBackgroundService, str: String) {
        Log.e(TAG, "waitScanning start!")
        delayHandler.postDelayed({ startScanning(backgroundService, str) }, 10000)    // handler로 delay 시키기
    }

    private fun startScanning(backgroundService: ReservationBackgroundService, str: String) {
        Log.e(TAG, "startScanning start!")
        val map = hashMapOf(
                "NICKNAME" to str
        )

        backgroundService.resultRepos(map)
                .subscribeOn(Schedulers.io())   // 데이터를 보내는 쓰레드.
                .observeOn(AndroidSchedulers.mainThread())  // 데이터를 받아서 사용하는 쓰레드.
                .subscribe({    // 받은 데이터를 사용하는 함수. 받은 데이터 : it
                    // 서버 통신 성공
                    if (it.result == 1) {   // 로그인 성공
                        Log.e(TAG, "result success : ${it.result}")
                    }
                    else {
//                        showNotification(this, "VOWOW", "테스트", Intent(this, SplashActivity::class.java))
                        getManageMessage()
                        Log.e(TAG, "result failed : ${it.result}")
                    }
                    waitScanning(backgroundService, str) // 반복
                }, {
                    // 서버 통신 실패
                    Log.d(TAG, "Error : ${it.message}")
                    onDestroy()
                })
    }

    // 노티 알람 함수.
    fun showNotification(context: Context, title: String, body: String, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val channelId = "channel-01"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(body)
//                .setFullScreenIntent()
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        notificationManager.notify(notificationId, mBuilder.build())
    }
}
