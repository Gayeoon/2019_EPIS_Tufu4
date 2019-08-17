package com.gaze.rkdus.a2019_epis_tufu4.item

import java.io.Serializable

data class MyReservationListData(var reservation_state: String, var reservation_type: Int, var hospital_key: Int,
                                 var hospital_name: String, var reservation_date: String, var data: MyReservationData? = null)
    : Serializable {

    fun getTypeToStr(): String? {   // 1: inner,  2: outer,  3: badge
        return when (reservation_type) {
            1 -> "반려동물 등록"
            2 -> "예방접종"
            3 -> "건강검진"
            else -> null
        }
    }
}