package com.gaze.rkdus.a2019_epis_tufu4.popup

import android.app.Activity
import android.databinding.DataBindingUtil.setContentView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gaze.rkdus.a2019_epis_tufu4.BaseActivity
import com.gaze.rkdus.a2019_epis_tufu4.R
import kotlinx.android.synthetic.main.activity_imagetext_popup.*

class ImageTextPopupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagetext_popup)

        intent.let {
            if (intent.hasExtra("type"))
                when(intent.getIntExtra("type", 0)) {
                    1 -> {  // 중성화 수술 팝업
                        popupImage.setImageResource(R.drawable.neutralization_popupimage)
                        popupText.setText(R.string.neutralizationPopupMsg)
                        cancelBtn.text = "안할래요"
                        okBtn.text = "할래요"
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
            setResult(RESULT_OK)
            //액티비티(팝업) 닫기
            finish()
        }
    }
}
