package com.gaze.rkdus.a2019_epis_tufu4.popup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity

import com.gaze.rkdus.a2019_epis_tufu4.R

import com.gaze.rkdus.a2019_epis_tufu4.user.MessageActivity.checkEditText
import kotlinx.android.synthetic.main.activity_filter_popup.*

class FilterPopupActivity : BaseActivity() {
    var megalopolis: String? = null
    var city: String? = null
    var dong: String? = null
    var location: String? = null
    var context: Context? = null

    // 0: 필터 해제.  1 : 최다 예약 순.  2 : 평점 순.  3 : 리뷰 개수 순
    var type = 0
    var locationFilter = false // true : 지역 별 적용. false : 지역 별 적용 X.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_popup)
        context = applicationContext

        intent.let {
            if (it.hasExtra("filter")) {
                type = it.getIntExtra("filter", 0)
                locationFilter = it.getBooleanExtra("locationFilter", false)
            }
            else
                finishPopup()

            if (locationFilter)
                filterLocation.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                location = it.getStringExtra("location")

            when (type) {
                1 -> reservationCount.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                2 -> reviewScore.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                3 -> reviewCount.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
            }

            filterLocation.setOnClickListener {
                nothing.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                startLocationFilterLayout()    // Location Layout 시작
            }
            reservationCount.setOnClickListener {
                if (type == 1) {
                    type = 0
                    reservationCount.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                }
                else {
                    type = 1
                    setFilterOff()
                    reservationCount.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                }
            }
            reviewScore.setOnClickListener {
                if (type == 2) {
                    type = 0
                    reviewScore.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                }
                else {
                    type = 2
                    setFilterOff()
                    reviewScore.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                }
            }
            reviewCount.setOnClickListener {
                if (type == 3) {
                    type = 0
                    reviewCount.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                }
                else {
                    type = 3
                    setFilterOff()
                    reviewCount.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                }
            }
            nothing.setOnClickListener {
                type = 0
                setFilterOff()
                filterLocation.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
                nothing.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
                locationFilter = false
            }


        }

        intent = Intent()

        okBtn.setOnClickListener {
            //데이터 전달하기
            intent.putExtra("result", type)
            if (locationFilter) { // 지역 별 필터 선택
                intent.putExtra("locationFilter", locationFilter)
                intent.putExtra("location", location)
            }
            setResult(Activity.RESULT_OK, intent)
            //액티비티(팝업) 닫기
            finish()
        }

        cancelBtn.setOnClickListener {
            //데이터 전달하기
            setResult(Activity.RESULT_CANCELED)
            //액티비티(팝업) 닫기
            finish()
        }
    }

    /*
    지역 별 필터를 제외한 나머지 TextView 검은색으로 변경
     */
    private fun setFilterOff() {
        reviewCount.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        reviewScore.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        reservationCount.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
        nothing.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
    }

    /*
    Location Filter Layout 시작
     */
    private fun startLocationFilterLayout() {
        locationFilterLayout.visibility = View.VISIBLE
        selectFilterLayout.visibility = View.GONE

        // TODO SearchActivity에서 가져온 location 값을 불러와 EditText에 뿌려주기. location을 띄어쓰기가 아니라 다른 문자로 변경한 다음 SearchActivity에서 가공해야 할 듯.

        Log.d(TAG, "oKLocationBtn click")
        okLocationBtn.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "oKLocationBtn click")
            // editText 값 Null 체크
            if (checkEditText(megalopolisEditText)) {
                megalopolis = megalopolisEditText.text.toString()
                location = megalopolis
            } else {
                Toast.makeText(applicationContext, "최소 시/도는 입력해야 합니다.", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            if (checkEditText(cityEditText)) {
                city = cityEditText.text.toString()
                location += " $city"
            }
            if (checkEditText(dongEditText)) {
                dong = dongEditText.text.toString()
                location += " $dong"
            }

            // 레이아웃, type 설정
            locationFilterLayout.visibility = View.GONE
            selectFilterLayout.visibility = View.VISIBLE
            filterLocation.setTextColor(ContextCompat.getColor(context!!, R.color.pinkTextColor))
            locationFilter = true
            Log.d(TAG, "location : $location")
        })

        cancelLocationBtn.setOnClickListener {
            Log.d(TAG, "cancelLocationBtn click")
            // Filter Popup Layout으로 돌아가기
            // editText 초기화
            megalopolisEditText.text = null
            cityEditText.text = null
            dongEditText.text = null

            location = ""
            locationFilterLayout.visibility = View.GONE
            selectFilterLayout.visibility = View.VISIBLE
            filterLocation.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
            locationFilter = false
            Toast.makeText(applicationContext, "지역 별 필터를 해제합니다.", Toast.LENGTH_LONG).show()
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
