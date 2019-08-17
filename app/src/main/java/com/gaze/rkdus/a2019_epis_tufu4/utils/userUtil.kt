package com.gaze.rkdus.a2019_epis_tufu4.utils

import android.text.TextUtils
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class userUtil {
    companion object {
        /*
        스피너의 보여줄 수 있는 최대 크기 설정하기.
        */
        fun setSpinnerMaxHeight(spinner: Spinner, height: Int) {
            try {
                val popup = Spinner::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val popupWindow = popup.get(spinner) as android.widget.ListPopupWindow
                popupWindow.height = height
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }

        /*
        EditText의 값 중 공백 확인
        @return : boolean(true : 앞 뒤 공백 또는 전체 공백이 아님. false : 둘 중 하나라도 해당하는 경우)
        */
        fun checkEditText(editText: EditText): Boolean {
            val editStr = editText.text.toString()    // 검색어 임시 변수에 저장.
            if (TextUtils.isEmpty(editStr.trim())) { // 공백처리
                return false
            }
            return editStr == editStr.trim()
        }


    }
}