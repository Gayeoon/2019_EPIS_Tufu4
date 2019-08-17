package com.gaze.rkdus.a2019_epis_tufu4.item

import org.json.JSONObject
import java.io.Serializable

data class MyReservationListData(var reservation_state: String, var reservation_type: Int, var hospital_key: Int,
                                 var hospital_name: String, var reservation_date: String)
    : Serializable {

    fun getTypeToStr(): String? {   // 1: inner,  2: outer,  3: badge
        return when (reservation_type) {
            1 -> "반려동물 등록"
            2 -> "예방접종"
            3 -> "건강검진"
            else -> null
        }
    }

    fun getJSONObj(): JSONObject? {
        var jsonObj = JSONObject()
        jsonObj.accumulate("reservation_state", reservation_state)
        jsonObj.accumulate("reservation_type", reservation_type)
        jsonObj.accumulate("hospital_key", hospital_key)
        jsonObj.accumulate("hospital_name", hospital_name)
        jsonObj.accumulate("reservation_date", reservation_date)
        return jsonObj
    }
}