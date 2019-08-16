package com.gaze.rkdus.a2019_epis_tufu4.popup

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import com.gaze.rkdus.a2019_epis_tufu4.item.MyReservationData
import kotlinx.android.synthetic.main.activity_imagetext_popup.*

class ImageTextPopupActivity : BaseActivity() {
    var popuptype: Int = 0
    var data: MyReservationData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagetext_popup)

        intent.let {
            if (intent.hasExtra("popupType"))
                popuptype = intent.getIntExtra("popupType", 0)
                when(popuptype) {
                    1 -> {  // 중성화 수술 팝업
                        popupImage.setImageResource(R.drawable.neutralization_popupimage)
                        popupText.setText(R.string.neutralizationPopupMsg)
                        cancelBtn.text = "안할래요"
                        okBtn.text = "할래요"
                    }
                    2 -> { // 예약확정 팝업
                        data = intent!!.getSerializableExtra("data") as MyReservationData?
                        popupImage.setImageResource(R.drawable.mypage_comfirmpopupimg)
                        popupText.setText(R.string.registConfirmPopupMsg)
                        cancelBtn.text = "취소"
                        okBtn.text = "확인"
                    }
                }
        }


        // 안할래요 클릭 시
        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            //액티비티(팝업) 닫기
            finish()
        }

        // 할래요 클릭 시
        okBtn.setOnClickListener {
            var resultIntent = Intent()
            if (popuptype == 2) {   // 예약 확정 팝업
                data!!.reservatioN_STATE = "CONFIRM"
                resultIntent.putExtra("data", data)
            }
            setResult(RESULT_OK, resultIntent)
            //액티비티(팝업) 닫기
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
