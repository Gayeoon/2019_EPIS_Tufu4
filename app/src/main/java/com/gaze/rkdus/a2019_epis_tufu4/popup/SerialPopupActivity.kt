package com.gaze.rkdus.a2019_epis_tufu4.popup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.utils.SerialService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_serial_popup.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SerialPopupActivity : BaseActivity() {
    var serialWord : String? = null
    var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serial_popup)

        if (intent == null) {
            Toast.makeText(applicationContext, "일련번호의 강아지가 존재하지 않습니다.", Toast.LENGTH_LONG).show()
            finish()
        }

        intent.let {
            if (intent.hasExtra("serialWord")) {
                serialWord = intent.getStringExtra("serialWord")
            }
            else
                finishPopup()
        }

        val serialService: SerialService = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .client(OkHttpClient())
                .build()
                .create(SerialService::class.java)

        val map = hashMapOf(
                "serialWord" to serialWord!!
        )

        serialService.resultSerialRepos(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 서버 통신 성공
                    it.let {
                        tvOwnerName.text = "주인 이름 : ${it.owner_name}"
                        tvPetName.text = "반려동물 이름 : ${it.pet_name}"
                        tvOwnerPhone.text = "주인 전화번호 : ${it.owner_phone}"
                        phoneNumber = it.owner_phone
                    }
                }, {
                    // 서버 통신 실패
                    Log.d(TAG, "Error : ${it.message}")
                    Toast.makeText(applicationContext, "서버 오류입니다. 다시 조회해주세요.", Toast.LENGTH_LONG).show()
                    finish()
                })

        okBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        }

        cancelBtn.setOnClickListener {
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //바깥레이어 클릭시 안닫히게
        return event.action != MotionEvent.ACTION_OUTSIDE
    }

    override fun onBackPressed() {
        //안드로이드 백버튼 막기
        return
    }
}
