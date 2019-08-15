package com.gaze.rkdus.a2019_epis_tufu4.utils

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationBackgroundService {
    @POST("/checkNickname")
    fun resultRepos(@Body params: HashMap<String, String>) : Single<ResultData>
}
data class ResultData(var result: Int)