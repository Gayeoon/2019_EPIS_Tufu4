package com.gaze.rkdus.a2019_epis_tufu4.user

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import kotlinx.android.synthetic.main.activity_select_message_type.*

class SelectMessageTypeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_message_type)
        var msgIntent: Intent? = null

        if (intent == null)
            finishPopup()

        intent.let {
            if (!intent.hasExtra("key"))
                finishPopup()
            if (!intent.hasExtra("hospitalName"))
                finishPopup()
        }

        // 반려동물 등록 예약 클릭 시
        applyTypeImg.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                msgIntent = Intent(applicationContext, MessageActivity::class.java)
                msgIntent!!.putExtra("key", intent.getIntExtra("key", 0))
                msgIntent!!.putExtra("hospitalName", intent.getStringExtra("hospitalName"))
                startActivity(msgIntent)
                finish()
            }
            false
        }

        // 예방접종 예약 클릭 시
        vaccinTypeImg.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                msgIntent = Intent(applicationContext, VaccinMessageActivity::class.java)
                msgIntent!!.putExtra("key", intent.getIntExtra("key", 0))
                msgIntent!!.putExtra("hospitalName", intent.getStringExtra("hospitalName"))
                startActivity(msgIntent)
                finish()
            }
            false
        }

        // 반려동물 등록 예약 이벤트 클릭 시
        healthcheckupTypeImg.setOnTouchListener { _, event ->
            if(event?.action == MotionEvent.ACTION_DOWN) {
                msgIntent = Intent(applicationContext, HealthCheckupMessageAcitivty::class.java)
                msgIntent!!.putExtra("key", intent.getIntExtra("key", 0))
                msgIntent!!.putExtra("hospitalName", intent.getStringExtra("hospitalName"))
                startActivity(msgIntent)
                finish()
            }
            false
        }
    }
}
