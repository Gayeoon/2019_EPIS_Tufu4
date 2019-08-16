package com.gaze.rkdus.a2019_epis_tufu4.popup

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.item.AddReviewData
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationData
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReservationBackgroundService
import com.gaze.rkdus.a2019_epis_tufu4.utils.ReviewService
import kotlinx.android.synthetic.main.activity_imagetext_popup.*
import com.gaze.rkdus.a2019_epis_tufu4.utils.userUtil.Companion.checkEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_imagetext_popup.cancelBtn
import kotlinx.android.synthetic.main.activity_imagetext_popup.okBtn
import kotlinx.android.synthetic.main.activity_review_popup.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReviewPopupActivity : BaseActivity() {
    var data: MyReservationData? = null
//    var hospital_key: Int = -1
    var content: String? = null
    var date: String? = null
    var score: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_popup)

        if (intent == null)
            finishPopup()

        intent.let {
            if(intent.hasExtra("data")) {
                data = intent.getSerializableExtra("data") as MyReservationData?
                // TODO : as MyReservationData가 안되면 바꿔버리자..
//                hospital_key = intent.getIntExtra("hospital_key", -1)
            }
            else
                finishPopup()
        }

        // 별로 평점 체크하는 이벤트
        starFirst.setOnTouchListener(touchListener)
        starSecond.setOnTouchListener(touchListener)
        starThird.setOnTouchListener(touchListener)
        starFourth.setOnTouchListener(touchListener)
        starFifth.setOnTouchListener(touchListener)

        // 안할래요 클릭 시
        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            //액티비티(팝업) 닫기
            finish()
        }

        // 리뷰 작성 완료 클릭 시
        okBtn.setOnClickListener {
            if (checkEditText(edReviewContent)) {
                content = edReviewContent.text.toString()

                val reviewService: ReviewService = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(SERVER_URL)
                        .client(OkHttpClient())
                        .build()
                        .create(ReviewService::class.java)

                // 현재 날짜 받아와서 String 변환
                var now = LocalDate.now()
                var nowDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                val addReviewData = AddReviewData(data!!.hospitaL_KEY, KAKAO_ID, nowDate, content!!, score)

                reviewService.resultAddReviewRepos(addReviewData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            // 서버 통신 성공
                            if (it.result == 1) {   // 로그인 성공
                                Log.d(TAG, "리뷰 작성 성공!")
                                val getIntent = Intent()
                                getIntent.putExtra("data", data)
                                setResult(RESULT_OK, getIntent)
                                //액티비티(팝업) 닫기
                                finish()
                            }
                            else {
                                Log.d(TAG, "리뷰 작성 실패!")
                                Toast.makeText(applicationContext, "리뷰 작성 실패했습니다. 잠시 후 다시 시도해주십시오.", Toast.LENGTH_LONG).show()
                            }
                        }, {
                            // 서버 통신 실패
                            Log.d(TAG, "Error : ${it.message}")
                            onDestroy()
                        })
            }
        }

    }

    private val touchListener = View.OnTouchListener { v, event ->
        // 전부 회색으로 초기화
        starFirst.setImageResource(R.drawable.review_notcoloredstar)
        starSecond.setImageResource(R.drawable.review_notcoloredstar)
        starThird.setImageResource(R.drawable.review_notcoloredstar)
        starFourth.setImageResource(R.drawable.review_notcoloredstar)
        starFifth.setImageResource(R.drawable.review_notcoloredstar)

        when (v.id) {
            R.id.starFirst -> {
                score = 1
                starFirst.setImageResource(R.drawable.review_coloredstar)
            }
            R.id.starSecond -> {
                score = 2
                starFirst.setImageResource(R.drawable.review_coloredstar)
                starSecond.setImageResource(R.drawable.review_coloredstar)
            }
            R.id.starThird -> {
                score = 3
                starFirst.setImageResource(R.drawable.review_coloredstar)
                starSecond.setImageResource(R.drawable.review_coloredstar)
                starThird.setImageResource(R.drawable.review_coloredstar)
            }
            R.id.starThird -> {
                score = 4
                starFirst.setImageResource(R.drawable.review_coloredstar)
                starSecond.setImageResource(R.drawable.review_coloredstar)
                starThird.setImageResource(R.drawable.review_coloredstar)
                starFourth.setImageResource(R.drawable.review_coloredstar)
            }
            R.id.starFourth -> {
                score = 5
                starFirst.setImageResource(R.drawable.review_coloredstar)
                starSecond.setImageResource(R.drawable.review_coloredstar)
                starThird.setImageResource(R.drawable.review_coloredstar)
                starFourth.setImageResource(R.drawable.review_coloredstar)
                starFifth.setImageResource(R.drawable.review_coloredstar)
            }
        }
        false
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
